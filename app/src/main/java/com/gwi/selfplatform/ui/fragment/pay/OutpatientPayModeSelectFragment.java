package com.gwi.selfplatform.ui.fragment.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.OrderParamater;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1612;
import com.gwi.selfplatform.module.net.response.OrderResult;
import com.gwi.selfplatform.module.net.response.RecipeList;
import com.gwi.selfplatform.module.pay.common.PayMode;
import com.gwi.selfplatform.module.pay.zhifubao.Product;
import com.gwi.selfplatform.ui.base.HospBaseFragment;
import com.gwi.selfplatform.ui.view.GWISupportedPayTypeWidget;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by 毅 on 2016/1/12.
 */
public class  OutpatientPayModeSelectFragment extends HospBaseFragment {

    private static final String TAG = OutpatientPayModeSelectFragment.class.getSimpleName();

    @Bind(R.id.outpatient_pay_type_widget)
    GWISupportedPayTypeWidget mPayTypeWidget;

    private List<RecipeList> recpieList;
    private String fee;
    private T_Phr_BaseInfo baseInfo;
    private T_Phr_CardBindRec cardInfo;
    private G1011 patientInfo;

    private String mPayMode;

    @OnClick(R.id.outpatient_charge_commit)
    void onClick() {
        if (Constants.PAY_MODE_CARD_MEDICAL.equalsIgnoreCase(mPayMode)) {
            executeMedicalPay();
        } else if (PayMode.getInstance().isPayModeSupport(mPayMode)) {
            creatOrderAsync();
        } else {
            DemoGenerator.showUnderConstruction(getActivity());
        }
    }

    public OutpatientPayModeSelectFragment() {
    }

    public static OutpatientPayModeSelectFragment newInstance(String fee, List<RecipeList> recpieList, T_Phr_BaseInfo baseInfo, T_Phr_CardBindRec cardInfo, G1011 patientInfo) {
        Bundle args = new Bundle();
        args.putSerializable("baseInfo", baseInfo);
        args.putSerializable("cardInfo", cardInfo);
        args.putSerializable("patientInfo", patientInfo);
        args.putString("fee", fee);
        args.putSerializable("recpieList", (Serializable) recpieList);
        OutpatientPayModeSelectFragment fragment = new OutpatientPayModeSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        baseInfo = (T_Phr_BaseInfo) b.getSerializable("baseInfo");
        cardInfo = (T_Phr_CardBindRec) b.getSerializable("cardInfo");
        patientInfo = (G1011) b.getSerializable("patientInfo");
        fee = (String) b.getSerializable("fee");
        recpieList = (List<RecipeList>) b.getSerializable("recpieList");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outpatient_pay_mode, container, false);
        ButterKnife.bind(this, view);

        Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
        mPayTypeWidget.handleHospitalParams(
                HospitalParams.getFields(HospitalParams.getValue(params, HospitalParams.CODE_PAY_TYPE))
        );

        mPayTypeWidget.setSelectedListener(new GWISupportedPayTypeWidget.PayTypeSelectedListener() {
            @Override
            public void onPayTypeSelected(String payMode) {
                mPayMode = payMode;
            }
        });
        return view;
    }

    private void executeMedicalPay() {
        ApiCodeTemplate.doPayTreatmentOrdersAsync(getContext(), TAG, null, cardInfo, Double.valueOf(fee), patientInfo, getRegIDs(), new RequestCallback<G1612>() {
            @Override
            public void onRequestSuccess(G1612 result) {
                getBaseActivity().showToast("缴费成功！");
                EventBus.getDefault().post(result);
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });

    }

    private String getRegIDs() {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (; i < recpieList.size() - 1; i++) {
            RecipeList recipe = recpieList.get(i);
            result.append(recipe.getRecipeInfo().getRecipeID()).append("|");
        }
        result.append(recpieList.get(i).getRecipeInfo().getRecipeID());
        return result.toString();
    }


    private void creatOrderAsync() {
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        final OrderParamater params = new OrderParamater();
        params.setUserInfo(userInfo);
        params.setBaseInfo(baseInfo);
        params.setRegIds(getRegIDs());
        params.setBussinessType(BankUtil.BusinessType_Recipe);
        params.setPatientInfo(patientInfo);
        params.setCardInfo(cardInfo);
        params.setPayType(mPayMode);
        params.setTransactionValue(Double.valueOf(fee));

        ApiCodeTemplate.createOrderAsync(getActivity(), TAG, params, new RequestCallback<OrderResult>() {
            @Override
            public void onRequestSuccess(OrderResult result) {
                if (result != null) {
                    Product product = new Product();
                    product.setSubject("缴费");
                    product.setBody(GlobalSettings.INSTANCE.getHospitalName());
                    product.setPrice(CommonUtils.getDoubleData(fee));
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderNo", result.getOrderNo());
                    bundle.putSerializable("Product", product);
                    bundle.putSerializable("CardValue", cardInfo.getCardNo());
                    bundle.putSerializable("Name", baseInfo.getName());
                    //TODO:
                    bundle.putString("RegID", params.getRegIds());
                    bundle.putString("Type", BankUtil.BusinessType_Recipe);
                    // openActivity(ExternalPartner.class, bundle);
                    bundle.putString("PrepayId", result.getPrepayId());
                    PayMode.getInstance().pay(getBaseActivity(), mPayMode, bundle);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }


}
