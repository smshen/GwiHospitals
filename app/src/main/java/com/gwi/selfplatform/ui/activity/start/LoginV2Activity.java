package com.gwi.selfplatform.ui.activity.start;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.utils.CommonUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5110;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 登录页面
 *
 * @version v2.0
 * @Date 2016/1/11
 */
public class LoginV2Activity extends HospBaseActivity {

    private static final String TAG = LoginV2Activity.class.getSimpleName();
    @Bind(R.id.login_username)
    EditText mEtUserName;

    @Bind(R.id.login_password)
    EditText mEtPwd;

    @Bind(R.id.login_v2_title)
    TextView mTvTitle;

    @Bind(R.id.login_v2_version)
    TextView mTvVersion;

    @OnClick(R.id.btn_login)
    void loginSubmit() {
        if (TextUtils.isEmpty(mEtUserName.getText())) {
            mEtUserName.setError("请输入用户名");
            mEtUserName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mEtPwd.getText())) {
            mEtPwd.setError("请输入密码");
            mEtPwd.requestFocus();
            return;
        }
        loginAsync();
    }

    @OnClick(R.id.login_v2_register)
    void register() {
        openActivityForResult(RegisterV3Activity.class, 1);
        // openActivityForResult(RegisterActivity.class, 1);
    }

    @OnClick(R.id.login_v2_pwd_forgot)
    void passwordForgot() {
        // openActivity(PasswordForgotV2Activity.class);
        openActivityForResult(PwdFindPhoneActivity.class, 1);
    }

    @OnTextChanged(R.id.login_username)
    void onTextChanged(CharSequence s) {
        if (TextUtils.isEmpty(mEtUserName.getText())) {
            mEtPwd.setText("");
        }
    }

    @Override
    protected void initViews() {
        mTvTitle.setText(R.string.app_name);
        mTvVersion.setText(String.format("版本v%s", CommonUtil.getAppVersion(this)));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initViews();
        initEvents();
    }

    private void loginAsync() {
        final String username = mEtUserName.getText().toString();
        final String password = mEtPwd.getText().toString();
        ApiCodeTemplate.loginAsync(this, TAG, username, password, new RequestCallback<G5110>() {
            @Override
            public void onRequestSuccess(G5110 response) {
                if (response != null) {
                    T_UserInfo user = response.getUserInfo();
                    T_Phr_BaseInfo baseinfo = response.getBaseInfo();
                    if (user != null && baseinfo != null) {
                        user.setUserPwd(MD5Util.string2MD5(password));
                        boolean a = DBController.INSTANCE.saveUser(user);
                        boolean b = DBController.INSTANCE
                                .saveFamilyAccount(baseinfo);
                        if (a & b) {
                            Logger.d(TAG, "登录成功");
                            setResult(RESULT_OK);
                            GlobalSettings.INSTANCE.setIsLogined(true);
                            GlobalSettings.INSTANCE.setCurrentUser(user);
                            GlobalSettings.INSTANCE.setCurrentFamilyAccount(baseinfo);
                        } else {
                            setResult(RESULT_CANCELED);
                            showToast("登录失败了,请稍后再试~");
                        }
                    } else {
                        setResult(RESULT_CANCELED);
                        showToast("登录失败了,请稍后再试~");
                    }

//                    if (getCallingActivity() != null && getCallingActivity().getClassName().equals(SplashActivity.class.getName())) {
//                        if (baseinfo != null && !TextUtil.isEmpty(baseinfo.getIDCard())) {
//                            openActivity(HomeActivity.class);
//                        } else {
//                            openActivity(HosPersonalInfoActivity.class);
//                        }
//                    }
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    showToast(R.string.login_login_failed);
                    setResult(RESULT_CANCELED);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(LoginV2Activity.this, (Exception) error.getException());
            }
        });
    }
}
