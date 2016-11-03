package com.gwi.selfplatform.ui.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.ccly.android.commonlibrary.common.security.MD5Util;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5110;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.ui.activity.user.HosPersonalInfoActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.service.ValidateCodeService;
import com.gwi.selfplatform.ui.view.ShakableEditText;
import com.gwi.selfplatform.ui.view.ValidateButton;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class RegisterInputActivity extends HospBaseActivity {
    public static final String TAG = RegisterInputActivity.class.getSimpleName();

    private String mPhoneNumber;
    private String mAuthCode;

    private ValidateCodeService mCodeService;

    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.txt_num)
    TextView mTxtNum;
    @Bind(R.id.et_register_code)
    ShakableEditText mEtRegisterCode;
    @Bind(R.id.btn_retry)
    ValidateButton mBtnRetry;
    @Bind(R.id.et_password)
    ShakableEditText mEtPassword;
    @Bind(R.id.ck_hide)
    CheckedTextView mCkHide;

    @OnClick({R.id.btn_register, R.id.btn_retry, R.id.ck_hide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_retry:
                getValidateCodeAsync();
                break;
            case R.id.btn_register:
                if (validate()) {
                    doRegisterAsync();
                }
                break;
            case R.id.ck_hide:
                Logger.i(TAG, "isChecked = " + mCkHide.isChecked());
                mCkHide.setChecked(!mCkHide.isChecked());
                CommonUtils.setInputType(mEtPassword, !mCkHide.isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    protected void initViews() {
        setTitle("用户注册");
        mCkHide.setChecked(true);
        mTxtNum.setText(String.format("%s%s", getString(R.string.register_num_has_sent), mPhoneNumber));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input);
        ButterKnife.bind(this);

        initDatas();
        initViews();
        initEvents();
    }

    private void initDatas() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            mPhoneNumber = bundle.getString("PhoneNumber");
            // mAuthCode = bundle.getString("AuthCode");
            Logger.i(TAG, "mPhoneNumber = " + mPhoneNumber + " ; mAuthCode = " + mAuthCode);
        }
    }

    private void getValidateCodeAsync() {
        ApiCodeTemplate.getValidationCodeAsync(this, TAG, mPhoneNumber,
                new RequestCallback<T_Phone_AuthCode>() {
                    @Override
                    public void onRequestSuccess(T_Phone_AuthCode result) {
                        // mIsNeedResetValidateCode = false;
                        if (result != null) {
                            if (mCodeService == null) {
                                mCodeService = new ValidateCodeService();
                            }
                            mCodeService.start(mPhoneNumber,result.getAuthCode());
                            // 发送成功后，阻塞验证码按钮
                            mBtnRetry.sendBlockMessage();

                            mPhoneNumber = result.getPhoneNumber();
                            mAuthCode = result.getAuthCode();
                            Logger.i(TAG, "mPhoneNumber = " + mPhoneNumber + " ; mAuthCode = " + mAuthCode);
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

    private boolean validate() {
        if (TextUtils.isEmpty(mEtRegisterCode.getText())) {
            mEtRegisterCode.setError(getText(R.string.msg_empty_validation_code));
            mEtRegisterCode.requestFocus();
            return false;
        }
//        if (!mEtRegisterCode.getText().toString().equals(mAuthCode)) {
        if (!mCodeService.isValid(mPhoneNumber, mAuthCode)) {
            mEtRegisterCode.setError(getText(R.string.msg_error_validation_code));
            mEtRegisterCode.requestFocus();
            return false;
        }

        // 验证码已经过期
        if (mCodeService.isExpired()) {
            mEtRegisterCode.setError(getText(R.string.msg_error_validation_expired));
            mEtRegisterCode.requestFocus();
            return false;
        }

//        if (mEtRegisterCode.isExpired()) {
//            mValidationCode
//                    .setError(getText(R.string.msg_error_validation_expired));
//            mValidationCode.requestFocus();
//            return false;
//        }


        if (TextUtils.isEmpty(mEtPassword.getText())) {
            mEtPassword.setError("密码不能为空");
            mEtPassword.requestFocus();
            return false;
        }
        if (mEtPassword.getText().length() < 6) {
            mEtPassword.setError("密码不能小于6位");
            mEtPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void doRegisterAsync() {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        Request.commonHeader(request, Request.FUN_CODE_USER_REGISTER, true);

        request.setBody(new TBase());
        T_UserInfo mUser = new T_UserInfo();

        mUser.setUserCode(mPhoneNumber);
        mUser.setUserName(mEtRegisterCode.getText().toString());
        //mUser.setUserCode(mFromIntentUserName.getText().toString());
        mUser.setMobilePhone(mPhoneNumber);
        mUser.setUserPwd(MD5Util.string2MD5(mEtPassword.getText().toString()));
        T_Phr_BaseInfo baseinfo = new T_Phr_BaseInfo();
        baseinfo.setEhrID(UUID.randomUUID().toString());
        baseinfo.setSelfPhone(mPhoneNumber);

        request.getBody().setUserInfo(mUser);
        request.getBody().setPhr_BaseInfo(baseinfo);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(getString(R.string.msg_loading_register))
                .mappingInto(new TypeToken<GResponse<T_Phr_BaseInfo>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        showToast(R.string.msg_success_register);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("PhoneNumber", mPhoneNumber);
//                        bundle.putString("Password", mEtPassword.getText().toString());
//                        openActivity(RegisterInfoActivity.class, bundle);

//                        finish(R.anim.push_right_in, R.anim.push_right_out);
//                        openActivity(HosPersonalInfoActivity.class);
                        loginAsync(mPhoneNumber, mEtPassword.getText().toString());
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegisterInputActivity.this, (Exception) error.getException());
                    }
                });
    }

    /**
     * 模拟登陆动作
     */
    private void loginAsync(final String username, final String password) {
//        final String username = mPhoneNumber;
//        final String password = mEtPassword.getText().toString();
        ApiCodeTemplate.loginAsync(this, TAG, username, password, new RequestCallback<G5110>() {
            @Override
            public void onRequestSuccess(G5110 response) {
                if (response != null) {
                    T_UserInfo user = response.getUserInfo();
                    T_Phr_BaseInfo baseinfo = response.getBaseInfo();
                    if (user != null && baseinfo != null) {
                        user.setUserPwd(com.gwi.selfplatform.common.security.MD5Util.string2MD5(password));
                        boolean a = DBController.INSTANCE.saveUser(user);
                        boolean b = DBController.INSTANCE
                                .saveFamilyAccount(baseinfo);
                        if (a & b) {
                            Logger.d(TAG, "登录成功");
                            setResult(RESULT_OK);
                            GlobalSettings.INSTANCE.setIsLogined(true);
                            GlobalSettings.INSTANCE.setCurrentUser(user);
                            GlobalSettings.INSTANCE
                                    .setCurrentFamilyAccount(baseinfo);
                        } else {
                            setResult(RESULT_CANCELED);
                            showToast("登录失败了,请稍后再试~");
                        }
                    } else {
                        setResult(RESULT_CANCELED);
                        showToast("登录失败了,请稍后再试~");
                    }

//                    if (getCallingActivity() != null && getCallingActivity().getClassName().equals(SplashActivity.class.getName())) {
//                        openActivity(HomeActivity.class);
//                    }
                    if (baseinfo != null && !TextUtil.isEmpty(baseinfo.getIDCard())) {
                        openActivity(HomeActivity.class);
                    } else {
                        openActivity(HosPersonalInfoActivity.class);
                    }
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    showToast(R.string.login_login_failed);
                    setResult(RESULT_CANCELED);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegisterInputActivity.this, (Exception) error.getException());
            }
        });
    }
}
