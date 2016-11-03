package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.common.utils.validator.IValidate;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.service.ValidateCodeService;
import com.gwi.selfplatform.ui.view.ValidateButton;

/**
 * 处理自助机身份证注册，手机为空的case.
 *
 * @author 彭毅
 */
public class MobileBindFirstActiviy extends HospBaseActivity implements
        OnClickListener, IValidate {

    private static final String TAG = MobileBindFirstActiviy.class
            .getSimpleName();

    private EditText mEtPhone;
    private EditText mEtValidCode;
    private Button mBtnSendCode;
    private Button mBtnCommit;
    private ValidateCodeService mCodeService = null;

    private T_UserInfo mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_bind_first);
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
        setTitle(R.string.personal_info);

        mEtPhone = (EditText) findViewById(R.id.mobile_bind_phone);
        mEtValidCode = (EditText) findViewById(R.id.validate_code);
        mBtnSendCode = (Button) findViewById(R.id.validate_send);
        mBtnCommit = (Button) findViewById(R.id.mobile_bind_first_commt);
    }

    @Override
    protected void initEvents() {
        mBtnCommit.setOnClickListener(this);
        mBtnSendCode.setOnClickListener(this);
        mEtPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mBtnSendCode.setEnabled(!TextUtils.isEmpty(s)
                        && TextUtil.matchPhone(s.toString()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void isMobileRegisteredNew(final T_UserInfo info) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_IS_MOBILE_REGISTERED, true);
        request.setBody(new TBase());
        request.getBody().setUserInfo(info);

        GWINet.connect().createRequest().postGWI(null, request).fromGWI()
                .setLoadingMessage(getString(R.string.msg_loading_validate))
                .showLoadingDlg(this, true)
                .mappingInto(new TypeToken<GResponse<G5117>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        G5117 result = (G5117) o;
                        if (result.getMobilePhoneExist() == G5117.REGISTERED) {
//                        if (result.isMobilePhoneExist()) {
                            showToast(R.string.msg_error_phone_registered);
                        } else {
                            modifyPhoneNewAsync();
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(MobileBindFirstActiviy.this, (Exception) error.getException());
                    }
                });
    }

    private void isMobileRegistered(final T_UserInfo info) {
        doCancellableAsyncTask(this,
                getText(R.string.msg_loading_validate),
                new AsyncCallback<Boolean>() {

                    @Override
                    public Boolean callAsync() throws Exception {
                        T_UserInfo phoneInfo = new T_UserInfo();
                        phoneInfo.setMobilePhone(mEtPhone.getText().toString());
                        return WebServiceController.isMobileRegistered(phoneInfo);
                    }

                    @Override
                    public void onPostCall(Boolean result) {
                        if (result != null) {
                            if (result.booleanValue()) {
                                showToast(R.string.msg_error_phone_registered);
                            } else {
                                modifyPhoneNewAsync();
                            }
                        } else {
                            showToast(R.string.msg_service_disconnected);
                        }
                    }

                    @Override
                    public void onCallFailed(Exception exception) {
                        Logger.e(TAG, exception.getLocalizedMessage() + "");
                        if (exception.getLocalizedMessage() != null) {
                            showToast(exception.getLocalizedMessage());
                        } else {
                            showToast(R.string.msg_service_disconnected);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.validate_send) {
            getValidateCodeNew(v, mEtPhone.getText().toString());
        } else if (id == R.id.mobile_bind_first_commt) {
            if (validate()) {
                if (mUser == null) {
                    mUser = new T_UserInfo();
                }
                mUser.setMobilePhone(mEtPhone.getText().toString());
                isMobileRegisteredNew(mUser);
            }
        }

    }

    @Override
    public boolean validate() {
        if (TextUtils.isEmpty(mEtPhone.getText())) {
            mEtPhone.setError(getText(R.string.msg_empty_phone));
            mEtPhone.requestFocus();
            return false;
        } else if (!TextUtil.matchPhone(mEtPhone.getText().toString())) {
            mEtPhone.setError(getText(R.string.msg_error_phone));
            mEtPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mEtValidCode.getText())) {
            mEtValidCode.setError(getText(R.string.msg_empty_validation_code));
            mEtValidCode.requestFocus();
            return false;
        } else if (!mCodeService.isValid(mEtPhone.getText().toString(), mEtValidCode.getText().toString())) {
            mEtValidCode.setError(getText(R.string.msg_error_validation_code));
            mEtValidCode.requestFocus();
            return false;
        } else if (mCodeService.isExpired()) {
            mEtValidCode
                    .setError(getText(R.string.msg_error_validation_expired));
            mEtValidCode.requestFocus();
            return false;
        }
        return true;
    }

    private void getValidateCodeNew(final View button, final String phone) {
        if (mCodeService == null) {
            mCodeService = new ValidateCodeService();
        }
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_VALIDATION_CODE_GET, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
        GWINet.connect().createRequest().postGWI(null, request).fromGWI()
                .setLoadingMessage(getString(R.string.msg_mobile_send))
                .showLoadingDlg(this, true)
                .mappingInto(new TypeToken<GResponse<T_Phone_AuthCode>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        T_Phone_AuthCode result = (T_Phone_AuthCode) o;
                        if (result != null) {
                            mCodeService.start(mEtPhone.getText().toString(), result.getAuthCode());
                            ((ValidateButton) button).sendBlockMessage();
                        } else {
                            showToast(R.string.msg_error_cannot_get_code_1);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        Logger.e(TAG, "getValidateCode#onCallFailed", (Exception) error.getException());
                        showToast(R.string.msg_error_cannot_get_code_2);
                    }
                });
    }

    private void getValidateCode(final View button, final String phone) {
        if (mCodeService == null) {
            mCodeService = new ValidateCodeService();
        }
        // TODO:mValidationCode
        doForcableAsyncTask(this, getString(R.string.msg_mobile_send),
                new AsyncCallback<T_Phone_AuthCode>() {

                    @Override
                    public T_Phone_AuthCode callAsync() throws Exception {
                        return WebServiceController.getValidationCode(phone);
                    }

                    @Override
                    public void onPostCall(T_Phone_AuthCode result) {
                        if (result != null) {
                            mCodeService.start(mEtPhone.getText().toString(), result.getAuthCode());
                            ((ValidateButton) button).sendBlockMessage();
                        } else {
                            showToast(R.string.msg_error_cannot_get_code_1);
                        }
                    }

                    @Override
                    public void onCallFailed(Exception exception) {
                        Logger.e(TAG, "getValidateCode#onCallFailed", exception);
                        showToast(R.string.msg_error_cannot_get_code_2);
                    }
                });
    }

    private void modifyPhoneNewAsync() {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_USER_INFO_MODIFY, true);

        request.setBody(new TBase());
        T_UserInfo curUser = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, curUser);

        T_UserInfo user = new T_UserInfo();
        user.setUserName(curUser.getUserName());
        user.setUserId(curUser.getUserId());
        user.setUserCode(curUser.getUserCode());
        user.setEhrId(curUser.getEhrId());
        user.setEMail(curUser.getEMail());
        user.setNickName(curUser.getNickName());
        user.setMobilePhone(mEtPhone.getText().toString());
        request.getBody().setUserInfo(user);

        GWINet.connect().createRequest().postGWI(null, request).fromGWI()
                .setLoadingMessage("正在提交...")
                .showLoadingDlg(this, false)
                .mappingInto(new TypeToken<GResponse<T_UserInfo>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        T_UserInfo result = (T_UserInfo) o;
                        GlobalSettings.INSTANCE.setCurrentUser(result);
                        T_Phr_BaseInfo family = GlobalSettings.INSTANCE
                                .getCurrentFamilyAccount();
                        family.setSelfPhone(result.getMobilePhone());
                        GlobalSettings.INSTANCE.setCurrentFamilyAccount(family);
                        showToast("修改成功");
                        setResult(RESULT_OK);
                        goBack();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        Exception e = (Exception) error.getException();
                        Logger.e(TAG, "modifyPhoneAsync#onCallFailed", e);
                        if (e.getLocalizedMessage() == null) {
                            showToast("修改手机号码失败，请稍后再试！");
                        } else if (e.getCause() != null) {
                            showToast(e.getCause().getLocalizedMessage());
                        }
                    }
                });
    }

    private void modifyPhoneAsync() {
        doForcableAsyncTask(this, "正在提交...", new AsyncCallback<T_UserInfo>() {

            @Override
            public T_UserInfo callAsync() throws Exception {
                return WebServiceController.modifyPhone(mEtPhone.getText()
                        .toString());
            }

            @Override
            public void onPostCall(T_UserInfo result) {
                if (result != null) {
                    GlobalSettings.INSTANCE.setCurrentUser(result);
                    T_Phr_BaseInfo family = GlobalSettings.INSTANCE
                            .getCurrentFamilyAccount();
                    family.setSelfPhone(result.getMobilePhone());
                    GlobalSettings.INSTANCE.setCurrentFamilyAccount(family);
                    showToast("修改成功");
                    setResult(RESULT_OK);
                    goBack();
                } else {
                    showToast("修改手机号码失败，请稍后再试！");
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e(TAG, "modifyPhoneAsync#onCallFailed", exception);
                if (exception.getLocalizedMessage() == null) {
                    showToast("修改手机号码失败，请稍后再试！");
                } else {
                    showToast(exception.getLocalizedMessage());
                }
            }
        });
    }

    private void goBack() {
        finish();
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
