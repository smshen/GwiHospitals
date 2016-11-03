package com.gwi.selfplatform.ui.activity.registration;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1214;
import com.gwi.selfplatform.module.net.response.G1414;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;

/**
 * 挂号完成页面：挂号成功，挂号失败
 *
 * @version v2.0
 * @date 2015-12-14
 */
public class RegistCompleteActivity extends HospBaseActivity {

    public static final String KEY_IS_SUCESS = "key_is_sucess";
    private static final String TAG = RegistConfirmV2Activity.class.getSimpleName();

    @Bind(R.id.loading_indicator)
    View mLoadingView;


    @Bind(R.id.loading_message)
    TextView mTvLoadingMsg;

    @Bind(R.id.regist_complete_layout)
    View mLayoutRegistComplete;

    @Bind(R.id.regist_complete_info_layout)
    TableLayout mLayoutInfo;

    @Bind(R.id.regist_complete_success_msg)
    TextView mTvSuccessMsg;

    @Bind(R.id.regist_complete_patient_name)
    TextView mTvPatientName;

    @Bind(R.id.regist_complete_dct_name)
    TextView mTvDoctName;

    @Bind(R.id.regist_complete_card_no)
    TextView mTvCardNo;

    @Bind(R.id.regist_complete_card_balance)
    TextView mTvCardBalance;

    @Bind(R.id.regist_complete_reg_password_layout)
    View mRegPwdLayout;
    @Bind(R.id.regist_complete_reg_password)
    TextView mTvRegPassword;
    @Bind(R.id.regist_complete_depart_name)
    TextView mTvDepartName;
    @Bind(R.id.regist_complete_depart_position)
    TextView mTvDepartPostion;

    @Bind(R.id.regist_complete_order_time)
    TextView mTvOrderDate;

    //---From intent [start]---
    private G1211 mFromIntentDept;
    private G1417 mFromIntentDoctor;
    private boolean mFromIntentIsRegist;
    private String mfromIntentOrderDate;
    private boolean mFromIntentHasDetailTime;
    private G1017 mfromIntentSubHos;
    private String mfromIntentTypeID;
    private G1011 mFromIntentPatientInfo;
    T_Phr_BaseInfo mFromIntentCurMember;
    T_Phr_CardBindRec mFromIntentCardInfo;
    private String mFromIntentPayMode;
    //---From intent [end]---

    private boolean mIsOperationSuccess = false;


    @Override
    protected void initViews() {
        Drawable successDraweable = getResources().getDrawable(R.drawable.registered_successfully);
        Rect sucessRect = new Rect(0, 0, mLayoutRegistComplete.getLayoutParams().width, mLayoutRegistComplete.getLayoutParams().height / 2);
        successDraweable.setBounds(sucessRect);
        mLayoutRegistComplete.setBackgroundDrawable(successDraweable);
        mTvLoadingMsg.setText("正在执行操作，请稍后...");
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_complete);
        addBackListener();
        addHomeButton();
        handleIntent();
        initViews();
        initEvents();

        if (mFromIntentIsRegist) {
            doRegistAsync();
        } else {
            doAppointAsync();
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsOperationSuccess) {
            openActivity(HomeActivity.class);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void handleIntent() {
        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            mFromIntentIsRegist = i.getBooleanExtra(Constants.KEY_IS_TYPE_REGIST,true);
            mfromIntentOrderDate = i.getStringExtra(Constants.KEY_ORDER_DATE);
            mfromIntentSubHos = (G1017) i.getSerializableExtra(Constants.KEY_SUB_HOSPITAL);
            mfromIntentTypeID = i.getStringExtra(Constants.KEY_TYPE_ID);
            mFromIntentDept = (G1211) i.getSerializableExtra(Constants.KEY_DEPT);
            mFromIntentDoctor = (G1417) i.getSerializableExtra(Constants.KEY_DCT);
            mFromIntentCardInfo = (T_Phr_CardBindRec) i.getSerializableExtra(RegistConfirmV2Activity.KEY_CARD_INFO);
            mFromIntentPatientInfo = (G1011) i.getSerializableExtra(Constants.KEY_PATIENT_INFO);
            mFromIntentHasDetailTime = i.getBooleanExtra(Constants.KEY_HAS_DETAIL_TIME, true);
            mFromIntentCurMember = (T_Phr_BaseInfo) i.getSerializableExtra(RegistConfirmV2Activity.KEY_PERSONAL_INFO);
            mFromIntentPayMode = i.getStringExtra(RegistConfirmV2Activity.KEY_PAY_MODE);
        }
    }

    private void doRegistAsync() {
        ApiCodeTemplate.doRegistAsync(this, TAG, mLoadingView, mFromIntentCardInfo, mfromIntentTypeID,
                mFromIntentPatientInfo, mFromIntentDoctor.getRegSourceID(), mFromIntentDoctor, mFromIntentDept, mfromIntentSubHos,
                new RequestCallback<G1214>() {
                    @Override
                    public void onRequestSuccess(G1214 result) {
                        setSuccessValue();
                        mTvDepartPostion.setText(result.getTreatLocation());
                        mRegPwdLayout.setVisibility(View.GONE);
                        loadNewPatientInfoAsync();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegistCompleteActivity.this, (Exception) error.getException());
                        finish();
                    }
                });
    }

    private void doAppointAsync() {
        ApiCodeTemplate.doOrderRegistAsync(this, TAG, mLoadingView,
                mFromIntentCardInfo, mfromIntentTypeID, mFromIntentPayMode, mFromIntentPatientInfo,
                mFromIntentDoctor.getRegSourceID(), mFromIntentDoctor, mFromIntentDept, new RequestCallback<G1414>() {
                    @Override
                    public void onRequestSuccess(G1414 result) {
                        setSuccessValue();
                        mTvDepartPostion.setText(result.getTreatLocation());
                        if (TextUtil.isEmpty(result.getOrderIdentity())) {
                            mRegPwdLayout.setVisibility(View.GONE);
                        } else {
                            mTvRegPassword.setText(result.getOrderIdentity());
                        }
                        //new money.
                        loadNewPatientInfoAsync();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegistCompleteActivity.this, (Exception) error.getException());
                        finish();
                    }
                }
        );
    }

    private void loadNewPatientInfoAsync() {
        T_Phr_CardBindRec cardBindRec = new T_Phr_CardBindRec();
        cardBindRec.setCardNo(mFromIntentCardInfo.getCardNo());
        cardBindRec.setCardType(mFromIntentCardInfo.getCardType());
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mTvCardBalance.setText(CommonUtils.formatCash(Double.valueOf(result.getMoney()), "元"));
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegistCompleteActivity.this, (Exception) error.getException());
            }
        });
    }

    private void setSuccessValue() {
        mIsOperationSuccess = true;
        try {
            Date orderDate = CommonUtils.stringPhaseDate(mfromIntentOrderDate, Constants.FORMAT_ISO_DATE);
            mTvSuccessMsg.setText(getString(R.string.register_success_msg, mFromIntentCurMember.getName(),
                    CommonUtils.phareDateFormat(Constants.DATE_FORMAT, orderDate)
                    , mFromIntentDept.getDeptName()
            ));
        } catch (ParseException e) {
            mTvSuccessMsg.setVisibility(View.INVISIBLE);
            Logger.e(TAG, "doAppointAsync", e);
        }

        mTvPatientName.setText(mFromIntentCurMember.getName());
        mTvCardNo.setText(mFromIntentCardInfo.getCardNo());
        mTvDoctName.setText(mFromIntentDoctor.getDoctName());
        mTvDepartName.setText(mFromIntentDept.getDeptName());
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
        mTvOrderDate.setText(String.format("%s %s", mfromIntentOrderDate, timepoint));
    }
}
