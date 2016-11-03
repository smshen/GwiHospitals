package com.gwi.selfplatform.ui.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.beans.OrderParamater;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.OrderResult;
import com.gwi.selfplatform.module.pay.common.PayMode;
import com.gwi.selfplatform.module.pay.zhifubao.Product;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.GWISupportedPayTypeWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 诊疗卡充值页面
 */
public class MedicalCardChargeActivity extends HospBaseActivity {

    private static final String TAG = MedicalCardChargeActivity.class.getSimpleName();
    @Bind(R.id.m_card_charge_name)
    TextView mTvName;

    @Bind(R.id.m_card_charge_card_no)
    TextView mTvCardNo;

    @Bind(R.id.m_card_charge_card_balance)
    TextView mTvCardBalance;

    @Bind(R.id.m_card_charge_value)
    EditText mEtChargeValue;


    @Bind(R.id.m_card_charge_card_types)
    GWISupportedPayTypeWidget mPayTypeWidget;

    private String mPayMode = Constants.PAY_MODE_ALIPAY;

    G1011 mPatientInfo;
    ExT_Phr_CardBindRec mCardInfo;
    T_Phr_BaseInfo mCurMember;


    @Override
    protected void initViews() {
        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        mTvName.setText(mCurMember.getName());

        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
        List<String> payTypes = new ArrayList<>();
        payTypes.addAll(HospitalParams.getFields(HospitalParams.getValue(params,
                HospitalParams.CODE_PAY_TYPE)));
        payTypes.remove(Constants.PAY_MODE_CARD_MEDICAL);
        payTypes.remove(Constants.PAY_MODE_NONE);
        payTypes.remove(Constants.PAY_FOR_FREE);
        PayMode.getInstance().addPayMode(payTypes);
        mPayTypeWidget.handleHospitalParams(payTypes);
    }

    @Override
    protected void initEvents() {
        mPayTypeWidget.setSelectedListener(new GWISupportedPayTypeWidget.PayTypeSelectedListener() {
            @Override
            public void onPayTypeSelected(String payMode) {
                mPayMode = payMode;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_card_charge);
        initViews();
        initEvents();
        loadBindedCardAsync();
    }

    @OnClick({R.id.m_card_charge_10, R.id.m_card_charge_20, R.id.m_card_charge_50,
            R.id.m_card_charge_100, R.id.m_card_charge_200, R.id.m_card_charge_500})
    void onFixedMoneySelected(View view) {
        switch (view.getId()) {
            case R.id.m_card_charge_10:
                mEtChargeValue.setText("10");
                break;
            case R.id.m_card_charge_20:
                mEtChargeValue.setText("20");
                break;
            case R.id.m_card_charge_50:
                mEtChargeValue.setText("50");
                break;
            case R.id.m_card_charge_100:
                mEtChargeValue.setText("100");
                break;
            case R.id.m_card_charge_200:
                mEtChargeValue.setText("200");
                break;
            case R.id.m_card_charge_500:
                mEtChargeValue.setText("500");
                break;
        }
        mEtChargeValue.setSelection(mEtChargeValue.getText().length());
    }

    @OnClick(R.id.btn_card_charge)
    void onChargeValue() {
        if (TextUtils.isEmpty(mEtChargeValue.getText())) {
            showToast("充值金额不能为空！");
            return;
        }
        if (mPatientInfo == null) {
            showToast("未绑定诊疗卡，请先绑定");
            return;
        }
        if (PayMode.getInstance().isPayModeSupport(mPayMode)) {
            createOrderAsync();
        } else {
            DemoGenerator.showUnderConstruction(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 72) {
            finish();
        }
    }

    private void loadBindedCardAsync() {
        ApiCodeTemplate.loadBindedCardAsync(this, TAG, mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    mTvCardNo.setText(mCardInfo.getCardNo());
                    loadPatientInfo(mCardInfo);
                } else {
                    CommonUtils.showNoCardDialog(MedicalCardChargeActivity.this, new INoCardCallback() {

                        @Override
                        public void isBindNow(boolean isBind) {
                            if (isBind) {
                                openActivityForResult(MyMedicalCardActivity.class, 1);
                            } else {
                                finish(R.anim.push_right_in, R.anim.push_right_out);
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MedicalCardChargeActivity.this, (Exception) error.getException());
            }
        });
    }

    private void loadPatientInfo(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mPatientInfo = result;
                mTvCardBalance.setText(CommonUtils.formatCash(Double.valueOf(result.getMoney()), "元"));
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MedicalCardChargeActivity.this, (Exception) error.getException());
            }
        });
    }

    private void createOrderAsync() {

        final OrderParamater paramater = new OrderParamater();
        paramater.setUserInfo(GlobalSettings.INSTANCE.getCurrentUser());
        paramater.setBaseInfo(mCurMember);
        paramater.setPayType(mPayMode);
        paramater.setBussinessType(BankUtil.BusinessType_CARD_CHARGE);
        paramater.setTransactionValue(Double.valueOf(mEtChargeValue.getText().toString()));
        paramater.setCardInfo(mCardInfo);
        paramater.setPatientInfo(mPatientInfo);

        LogUtil.i(TAG, "mPayMode = " + mPayMode);
        ApiCodeTemplate.createOrderAsync(this, TAG, paramater, new RequestCallback<OrderResult>() {
            @Override
            public void onRequestSuccess(OrderResult result) {
                if (result != null) {
                    Product product = new Product();
                    product.setSubject("预交金充值");
                    product.setBody(GlobalSettings.INSTANCE.getHospitalName());
                    product.setPrice(mEtChargeValue.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderNo", result.getOrderNo());
                    bundle.putString("IDCard", mCurMember.getIDCard());
                    bundle.putString("Name", mCurMember.getName());
                    bundle.putSerializable("Product", product);
                    bundle.putString("CardValue", mCardInfo.getCardNo());
                    bundle.putString("Type", paramater.getBussinessType());
                    bundle.putString("PrepayId", result.getPrepayId());
                    PayMode.getInstance().pay(MedicalCardChargeActivity.this, mPayMode, bundle);
                } else {
                    showToast(R.string.msg_service_disconnected);
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }
}
