package com.gwi.selfplatform.ui.activity.start;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5115;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.service.ValidateCodeService;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class PasswordForgotV2Activity extends HospBaseActivity {
    public static final String TAG = PasswordForgotV2Activity.class.getSimpleName();

    private ValidateCodeService mCodeService = null;
    // private String mAuthCode;
    private long mUserId;

    @Bind(R.id.et_telephone_num)
    ShakableEditText mEtTelephoneNum;
    @Bind(R.id.et_register_code)
    ShakableEditText mEtRegisterCode;
    @Bind(R.id.btn_retry)
    Button mBtnRetry;
    @Bind(R.id.btn_verification)
    Button mBtnVerification;


    @OnClick({R.id.btn_verification, R.id.btn_retry})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                validateResetInfoNewAsync();
                break;
            case R.id.btn_verification:
                doNext();
                break;
            default:
                break;
        }
    }

    private TextWatcher mTelephoneWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0 && TextUtil.matchPhone(s.toString())) {
                mBtnRetry.setEnabled(true);
            } else {
                mBtnRetry.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!(TextUtils.isEmpty(mEtRegisterCode.getText().toString())
                    && !TextUtils.isEmpty(mEtTelephoneNum.getText().toString()))) {
                mBtnVerification.setEnabled(true);
            } else {
                mBtnVerification.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_BOROADCAST_VALIDATION_CODE.equals(intent.getAction())) {
                Bundle b = intent.getExtras();
                if (b != null && b.containsKey(Constants.EXTRA_VALIDATION_CODE)) {
                    mEtRegisterCode.setText(b.getString(Constants.EXTRA_VALIDATION_CODE));
                }
            }
        }
    };

    @Override
    protected void initViews() {
        mCodeService = new ValidateCodeService();

        mBtnRetry.setEnabled(false);
        mBtnVerification.setEnabled(false);
    }

    @Override
    protected void initEvents() {
        mEtTelephoneNum.addTextChangedListener(mTelephoneWatcher);
        // mEtTelephoneNum.addTextChangedListener(mTextWatcher);
        // mEtRegisterCode.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot_v2);
        ButterKnife.bind(this);
        registerReceiver(mSmsReceiver, new IntentFilter(Constants.ACTION_BOROADCAST_VALIDATION_CODE));
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
    }

    private void getValidateCodeAsync(final View v) {
        ApiCodeTemplate.getValidationCodeAsync(this, TAG, mEtTelephoneNum.getText().toString().trim(),
                new RequestCallback<T_Phone_AuthCode>() {
                    @Override
                    public void onRequestSuccess(T_Phone_AuthCode result) {
                        // mIsNeedResetValidateCode = false;
                        if (result != null) {

                        } else {
                            showToast(R.string.msg_error_cannot_get_code_1);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        Logger.e(TAG, "onRequestError", (Exception) error.getException());
                        showToast(R.string.msg_error_cannot_get_code_2);
                    }
                });
    }

    private void validateResetInfoNewAsync() {
        T_UserInfo validateUser = new T_UserInfo();
        validateUser.setMobilePhone(mEtTelephoneNum.getText().toString());

        final Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_PASSWORD_FIND_VALIDATION, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(validateUser);

        GWINet.connect().createRequest().postGWI(null, request).fromGWI()
                .setLoadingMessage(getString(R.string.msg_mobile_validation))
                .showLoadingDlg(this, false)
                .mappingInto(new TypeToken<GResponse<G5115>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        G5115 result = (G5115) o;
                        T_Phone_AuthCode code = result.getPhoneAuth();
                        if (code != null) {
                            mCodeService.start(mEtTelephoneNum.getText().toString(),code.getAuthCode().trim());
                            // mUserId = result.getUserInfo().getUserId();
                            mBtnVerification.setEnabled(true);
                        } else {
                            showToast(R.string.msg_error_pwd_find_phone_validation);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(PasswordForgotV2Activity.this, (Exception) error.getException());
                    }
                });
    }

    private void doNext() {
        //TODO:屏蔽验证码
        if (TextUtils.isEmpty(mEtRegisterCode.getText().toString())) {
            mEtRegisterCode.setError(getText(R.string.msg_empty_validation_code));
            mEtRegisterCode.shakeText();
            return;
        } else if (!mCodeService.isValid(mEtTelephoneNum.getText().toString(),mEtRegisterCode.getText().toString())) {
            mEtRegisterCode.setError(getText(R.string.msg_error_validation_code));
            mEtRegisterCode.shakeText();
            return;
        } else {
            if (mCodeService.isExpired()) {
                mEtRegisterCode.setError(getText(R.string.msg_error_validation_expired));
                mEtRegisterCode.shakeText();
                return;
            }
        }

//        Bundle bundle = new Bundle();
//        bundle.putString("PhoneNumber", mEtTelephoneNum.getText().toString());
//        bundle.putString("Password", mEtPassword.getText().toString());
//        openActivity(SetNewPasswordV2Activity.class, bundle);
    }
}
