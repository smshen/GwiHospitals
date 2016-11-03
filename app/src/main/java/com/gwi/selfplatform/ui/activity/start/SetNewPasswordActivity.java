package com.gwi.selfplatform.ui.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.validator.IValidate;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ShakableEditText;

/**
 * 重置新密码页面
 *
 * @author 彭毅
 */
public class SetNewPasswordActivity extends HospBaseActivity implements IValidate {

    private static final String TAG = SetNewPasswordActivity.class
            .getSimpleName();

    private Long mUserId = null;

    private ShakableEditText mSetPasswordNew = null;
    private ShakableEditText mSetPasswordConfirm = null;
    private Button mBtnSet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setTitle(R.string.label_forgot_password);
        mSetPasswordNew = (ShakableEditText) findViewById(R.id.new_pwd_set_password);
        mSetPasswordConfirm = (ShakableEditText) findViewById(R.id.new_pwd_set_password_confirm);
        mBtnSet = (Button) findViewById(R.id.new_pwd_set_password_commit);

        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey(PwdFindPhoneActivity.KEY_USER_ID)) {
            mUserId = b.getLong(PwdFindPhoneActivity.KEY_USER_ID);
        } else {
            // TODO:
            finish();
        }
    }

    private void setNewPasswordNewAsync() {
        T_UserInfo userinfo = new T_UserInfo();
        userinfo.setUserId(mUserId);
        userinfo.setUserPwd(MD5Util.string2MD5(mSetPasswordNew.getText()
                .toString()));

        ApiCodeTemplate.setNewPasswordAsync(this, TAG, userinfo, new RequestCallback<GBase>() {
            @Override
            public void onRequestSuccess(GBase result) {
                showToast(R.string.msg_success_modifying);
                backToLogin();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(SetNewPasswordActivity.this, (Exception) error.getException());
            }
        });
    }

//    private void setNewPasswordAsync() {
//        doForcableAsyncTask(this, getString(R.string.msg_hint_modifying),
//                new AsyncCallback<T_UserInfo>() {
//
//                    @Override
//                    public T_UserInfo callAsync() throws Exception {
//                        // 设置只需要id,password.
//                        T_UserInfo userinfo = new T_UserInfo();
//                        userinfo.setUserId(mUserId);
//                        userinfo.setUserPwd(MD5Util.string2MD5(mSetPasswordNew.getText()
//                                .toString()));
//                        return WebServiceController.resetNewPassword(userinfo);
//                    }
//
//                    @Override
//                    public void onPostCall(T_UserInfo result) {
//                        if (result != null) {
//                            showToast(R.string.msg_success_modifying);
//                            backToLogin();
//                        } else {
//                            showToast(R.string.msg_error_modifying);
//                        }
//                    }
//
//                    @Override
//                    public void onCallFailed(Exception exception) {
//                        Logger.e(TAG, "setNewPasswordAsync#onCallFailed",
//                                exception);
//                        showToast(R.string.msg_service_disconnected);
//                    }
//                });
//    }

    /**
     * 修改成功，回到login页面。
     */
    private void backToLogin() {
        Intent intent = new Intent(this, LoginV2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void initEvents() {
        mBtnSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    setNewPasswordNewAsync();
                }
            }
        });
    }

    @Override
    public boolean validate() {
        if (TextUtils.isEmpty(mSetPasswordNew.getText())) {
            mSetPasswordNew.setError(getText(R.string.msg_empty_password));
            mSetPasswordNew.requestFocus();
            return false;
        }
        if (mSetPasswordNew.getText().length() < 6) {
            mSetPasswordNew.setError(getText(R.string.password_too_short));
            mSetPasswordNew.requestFocus();
            return false;
        }
        if (!mSetPasswordNew.getText().toString()
                .equals(mSetPasswordConfirm.getText().toString())) {
            mSetPasswordConfirm
                    .setError(getText(R.string.msg_error_pwd_not_same));
            mSetPasswordConfirm.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
