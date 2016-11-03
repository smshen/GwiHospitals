package com.gwi.selfplatform.ui.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.beans.OrderParamater;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1213;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.module.net.response.OrderResult;
import com.gwi.selfplatform.module.pay.common.PayMode;
import com.gwi.selfplatform.module.pay.zhifubao.Product;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.GWISupportedPayTypeWidget;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 挂号确认页面
 *
 * @version 2.0
 * @since 2015-12-12
 */
public class RegistConfirmV2Activity extends HospBaseActivity {

    /**
     * Key for personal info.
     */
    public static final String KEY_PERSONAL_INFO = "key_personal_info";
    /**
     * Key for card info.
     */
    public static final String KEY_CARD_INFO = "key_card_info";
    /**
     * key for pay mode.
     */
    public static final String KEY_PAY_MODE = "key_pay_mode";

    private static final String TAG = RegistConfirmV2Activity.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;


    @Bind(R.id.regist_confirm_v2_card_balance)
    protected TextView mTvCardBalance;

    @Bind(R.id.regist_confirm_v2_card_no)
    protected TextView mTvCardNo;

    @Bind(R.id.regist_confirm_v2_dct_duty)
    protected TextView mTvDctRankName;

    @Bind(R.id.regist_confirm_v2_dct_name)
    protected TextView mTvDctName;

    @Bind(R.id.regist_confirm_v2_depart)
    protected TextView mTvDepart;

    @Bind(R.id.regist_confirm_v2_depart_position)
    protected TextView mTvDepartPosition;

    @Bind(R.id.regist_confirm_v2_order_date)
    protected TextView mTvOrderDate;

    @Bind(R.id.regist_confirm_v2_patient_name)
    protected TextView mTvPatientName;

    @Bind(R.id.regist_confirm_v2_totalRegFee)
    protected TextView mTvTotalRegFee;


    @Bind(R.id.regist_confirm_v2_submit)
    protected Button mBtnSubmit;

    @Bind(R.id.regist_conform_v2_pay_type_widget)
    protected GWISupportedPayTypeWidget mPayTypeWidget;

    //---From intent [start]---
    private G1211 mFromIntentDept;
    private G1417 mFromIntentDoctor;
    private boolean mFromIntentIsRegist;
    private String mFromIntentOrderDate;
    private boolean mFromIntentHasDetailTime;
    private G1017 mFromIntentSubHos;
    private String mFromIntentTypeID;
    //---From intent [end]---

    private G1011 mPatientInfo;

    private T_Phr_BaseInfo mCurMember;
    private T_Phr_CardBindRec mCardInfo;

    private String mPayType;

    @Override
    protected void initViews() {
        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
        if (!mFromIntentIsRegist) {
            mPayTypeWidget.handleHospitalParams(
                    HospitalParams.getFields(HospitalParams.getValue(params,
                            HospitalParams.CODE_ORDER_REGISTER_PAY_TYPE))
            );
        } else {
            mPayTypeWidget.handleHospitalParams(
                    HospitalParams.getFields(HospitalParams.getValue(params,
                            HospitalParams.CODE_REGISTER_PAY_TYPE))
            );
        }
    }

    @Override
    protected void initEvents() {
        mPayTypeWidget.setSelectedListener(new GWISupportedPayTypeWidget.PayTypeSelectedListener() {
            @Override
            public void onPayTypeSelected(String payMode) {
                mPayType = payMode;
                if (payMode.equalsIgnoreCase(Constants.PAY_MODE_CARD_MEDICAL)) {
                    checkMoneyAffodable();
                } else {
                    mBtnSubmit.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_confirm_v2);
        addHomeButton();
        addBackListener();
        handleIntent();
        initViews();
        initEvents();
        loadCardBindAsync();
    }

    /**
     * Handle intent stuff.
     */
    private void handleIntent() {
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            //解决BUG:进入挂号确认页面，再返回，然后点击home键回到主页，在点击图标进入app后，会报错是
            // 该页面的bundle为空导致的，因此在此进行处理。
            if (i.getExtras() == null) {
                finish();
            }
            mFromIntentIsRegist = b.getBoolean(Constants.KEY_IS_TYPE_REGIST);
            mFromIntentSubHos = (G1017) b.getSerializable(Constants.KEY_SUB_HOSPITAL);
            mFromIntentDept = (G1211) b.getSerializable(Constants.KEY_DEPT);
            mFromIntentDoctor = (G1417) b.getSerializable(Constants.KEY_DCT);
            mFromIntentTypeID = (String) b.getSerializable(Constants.KEY_TYPE_ID);
            mFromIntentHasDetailTime = b.getBoolean(Constants.KEY_HAS_DETAIL_TIME, true);
            mFromIntentOrderDate = b.getString(Constants.KEY_ORDER_DATE);

            if (mFromIntentDept != null) {
                mTvDepart.setText(mFromIntentDept.getDeptName());
                mTvDepartPosition.setText(mFromIntentDept.getLocation());
            }
            if (mFromIntentDoctor != null) {
                mTvTotalRegFee.setText(CommonUtils.formatCash(mFromIntentDoctor.getTotalRegFee()));
                mTvDctName.setText(mFromIntentDoctor.getDoctName());
                mTvDctRankName.setText(mFromIntentDoctor.getRankName());
                String timepoint = null;
                if (!TextUtil.isEmpty(mFromIntentDoctor.getStartTime())) {
                    timepoint = mFromIntentDoctor.getStartTime();
                }
                if (!TextUtil.isEmpty(mFromIntentDoctor.getEndTime())) {
                    if (timepoint != null) {
                        timepoint += " - " + mFromIntentDoctor.getEndTime();
                    } else {
                        timepoint = mFromIntentDoctor.getEndTime();
                    }
                }
                mTvOrderDate.setText(String.format("%s %s", mFromIntentOrderDate, timepoint));

                mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
                mTvPatientName.setText(mCurMember.getName());
            }

        } else {
            CommonUtils.restartApp(this);
            finish();
        }
    }

    /**
     * 提交按钮点击事件
     */
    @OnClick(R.id.regist_confirm_v2_submit)
    void onSubmit() {
        if (Constants.PAY_MODE_CARD_MEDICAL.equalsIgnoreCase(mPayType)
                || Constants.PAY_FOR_FREE.equalsIgnoreCase(mPayType)) {
            //是否需要进行锁号处理
            Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
            if (mFromIntentIsRegist
                    && HospitalParams
                        .getValue(params, HospitalParams.CODE_NEED_LOCK_REGISTER)
                        .equals(HospitalParams.VALUE_ONE)) {
                lockRegistRegResourceAsync();
                return;
            }
            commitRequest();
        } else if (PayMode.getInstance().isPayModeSupport(mPayType)) {
            createOrderAsync();
        } else {
            DemoGenerator.showUnderConstruction(this);
        }
    }

    /**
     * 提交业务请求
     */
    private void commitRequest() {
        Bundle b = getIntent().getExtras();
        b.putSerializable(KEY_PERSONAL_INFO, mCurMember);
        b.putSerializable(Constants.KEY_PATIENT_INFO, mPatientInfo);
        b.putSerializable(KEY_CARD_INFO, mCardInfo);
        b.putString(KEY_PAY_MODE, mPayType);
        b.putSerializable(Constants.KEY_DCT, mFromIntentDoctor);
        openActivity(RegistCompleteActivity.class, b);
    }

    /**
     * 锁号
     */
    private void lockRegistRegResourceAsync() {
        showLoadingDialog("正在提交锁号...");
        ApiCodeTemplate.lockRegistRegResourceAsync(this, TAG, getLoadingDialog(),
                mFromIntentTypeID, mFromIntentDoctor, mFromIntentDept,
                mPatientInfo, new RequestCallback<G1213>() {
                    @Override
                    public void onRequestSuccess(G1213 result) {
                        if (mFromIntentDoctor != null) {
                            mFromIntentDoctor.setRegSourceID(result.getTranSerNo());
                        }
                        commitRequest();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegistConfirmV2Activity.this, (Exception) error.getException());
                    }
                });
    }

    private void loadCardBindAsync() {
        ApiCodeTemplate.loadBindedCardAsync(this, TAG, mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    mTvCardNo.setText(mCardInfo.getCardNo());
                    loadPatientInfoAsnc(mCardInfo);
                } else {
                    CommonUtils.showNoCardDialog(RegistConfirmV2Activity.this, new INoCardCallback() {

                        @Override
                        public void isBindNow(boolean isBind) {
                            if (isBind) {
                                //TODO:
                                openActivityForResult(MyMedicalCardActivity.class,
                                        REQUEST_CODE_BIND);
                            } else {
                                finish(R.anim.push_right_in, R.anim.push_right_out);
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegistConfirmV2Activity.this, (Exception) error.getException());
            }
        });
    }

    private void loadPatientInfoAsnc(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mPatientInfo = result;
                mTvCardBalance.setText(CommonUtils.formatCash(Double.valueOf(mPatientInfo.getMoney()), "元"));
                checkMoneyAffodable();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegistConfirmV2Activity.this, (Exception) error.getException());
            }
        });
    }

    private void createOrderAsync() {
        final OrderParamater param = new OrderParamater();
        param.setPayType(mPayType);

        if (Constants.PAY_MODE_ALIPAY.equalsIgnoreCase(mPayType)
                || Constants.PAY_MODE_CCB.equals(mPayType)) {
            if (mFromIntentIsRegist) {
                param.setBussinessType(BankUtil.BusinessType_Rgt);
            }
        }
        param.setBaseInfo(mCurMember);
        param.setUserInfo(GlobalSettings.INSTANCE.getCurrentUser());
        param.setTransactionValue(mFromIntentDoctor.getTotalRegFee());
        param.setCardInfo(mCardInfo);
        param.setDct(mFromIntentDoctor);
        param.setOrderDate(mFromIntentOrderDate);
        param.setPatientInfo(mPatientInfo);
        param.setDept(mFromIntentDept);
        param.setSubHosp(mFromIntentSubHos);
        param.setTypeID(mFromIntentTypeID);
        ApiCodeTemplate.createOrderAsync(this, TAG, param, new RequestCallback<OrderResult>() {
            @Override
            public void onRequestSuccess(OrderResult result) {
                if (result != null) {
                    Product product = new Product();
                    if (mFromIntentIsRegist) {
                        product.setSubject("挂号");
                    } else {
                        product.setSubject("预约挂号");
                    }
                    product.setBody(GlobalSettings.INSTANCE.getHospitalName());
                    product.setPrice(String.valueOf(CommonUtils.formatCash(mFromIntentDoctor.getTotalRegFee())));
                    Bundle bundle = new Bundle();
                    bundle.putString("OrderNo", result.getOrderNo());
                    bundle.putSerializable("Product", product);
                    bundle.putSerializable("CardInfo", mCardInfo);
                    bundle.putSerializable(Constants.KEY_DEPT, mFromIntentDept);
                    bundle.putSerializable("Dct", mFromIntentDoctor);
                    bundle.putString("OrderDate", mFromIntentOrderDate);
                    bundle.putSerializable("PersonInfo", mCurMember);
                    if (mFromIntentIsRegist) { //挂号
                        bundle.putString("Type", BankUtil.BusinessType_Rgt);
                    } else { //预约挂号
                        bundle.putString("Type", BankUtil.BusinessType_Rgt_Order);
                    }
                    // openActivity(ExternalPartner.class, bundle);
                    bundle.putString("PrepayId", result.getPrepayId());
                    PayMode.getInstance().pay(RegistConfirmV2Activity.this, mPayType, bundle);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegistConfirmV2Activity.this, (Exception) error.getException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 72) {
            AppManager.getInstance().killAllActivity();
            openActivity(HomeActivity.class);
        }
    }

    private void checkMoneyAffodable() {
        if (mPatientInfo == null) return;
        if (!mFromIntentIsRegist) return;
        if (Constants.PAY_MODE_CARD_MEDICAL.equalsIgnoreCase(mPayType)) {
            Double balance = Double.valueOf(mPatientInfo.getMoney());
            mBtnSubmit.setEnabled(mFromIntentDoctor.getTotalRegFee() <= balance);
        }
    }
}
