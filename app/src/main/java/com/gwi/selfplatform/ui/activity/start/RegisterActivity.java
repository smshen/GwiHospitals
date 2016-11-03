package com.gwi.selfplatform.ui.activity.start;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.IHyperlinkText;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.IValidate;
import com.gwi.selfplatform.common.utils.validator.Validator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.Response;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.dialog.WebDialog;
import com.gwi.selfplatform.ui.dialog.WebDialog.OnWebDialogErrorListener;
import com.gwi.selfplatform.ui.service.ValidateCodeService;
import com.gwi.selfplatform.ui.view.ValidateButton;

import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * 注册页面，处理用户的注册流程
 *
 * @author 彭毅
 */
public class RegisterActivity extends HospBaseActivity implements IValidate {
    public static final String TAG = "RegisterActivity";

    private EditText mPasswordInput = null;
    private EditText mUserCode = null;
    private EditText mIdCard = null;
    private EditText mValidationCode = null;
    private EditText mPhoneNumber = null;
    private EditText mName = null;
    private ValidateButton mBtnGetValidateCode = null;
    private Button mBtnCommit = null;
    private CheckedTextView mCtvProtocols = null;

    private View mLyoutValidate = null;

    private ValidateCodeService mCodeService;

    private T_UserInfo mUser;

    private boolean mNeedMobileValidate = true;

    /**
     * Senario:如果已经获取了验证码，在修改手机号，就需要重新获取验证码
     */
    private boolean mIsNeedResetValidateCode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
        handleHospitalParams();
    }

    private void handleHospitalParams() {
        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
        mNeedMobileValidate = HospitalParams.getValue(params, HospitalParams.CODE_IS_CARDBIND_NEED_MOBILE_VALIDATE_ENABLED).equals(HospitalParams.VALUE_ONE);
        if (!mNeedMobileValidate) mLyoutValidate.setVisibility(View.GONE);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_register_validate) {
            getValidateCodeAsync(v);
        } else if (id == R.id.user_register_switch) {
            siwtchClick(v);
        } else if (id == R.id.user_register_protocols) {
            mCtvProtocols.setChecked(!mCtvProtocols.isChecked());
        } else if (id == R.id.user_register_commit) {
            if (validate()) {
                if (mUser == null) {
                    mUser = new T_UserInfo();
                }
                mUser.setMobilePhone(mPhoneNumber.getText().toString());
                isMobileRegisteredNew();
            }
        }
    }

    private void commitNewAsync() {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        Request.commonHeader(request, Request.FUN_CODE_USER_REGISTER, true);

        request.setBody(new TBase());
        mUser.setUserCode(mPhoneNumber.getText().toString());
        mUser.setUserName(mName.getText().toString());
        //mUser.setUserCode(mFromIntentUserName.getText().toString());
        mUser.setMobilePhone(mPhoneNumber.getText().toString());
        mUser.setUserPwd(MD5Util.string2MD5(mPasswordInput.getText().toString()));
        T_Phr_BaseInfo baseinfo = new T_Phr_BaseInfo();
        baseinfo.setEhrID(UUID.randomUUID().toString());
        baseinfo.setSelfPhone(mPhoneNumber.getText().toString());
        baseinfo.setName(mName.getText().toString());
        baseinfo.setIDCard(mIdCard.getText().toString());
        try {
            baseinfo.setSex(CommonUtils.getSexFromIdCard(mIdCard.getText().toString()));
            baseinfo.setBirthDay(CommonUtils.getDateFromIDCard(mIdCard.getText().toString()));
            request.getBody().setUserInfo(mUser);
            request.getBody().setPhr_BaseInfo(baseinfo);
        } catch (Exception e) {
            Logger.e(TAG, "commitNewAsync", e);
            CommonUtils.showError(this, e);
            return;
        }

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(getString(R.string.msg_loading_register))
                .mappingInto(new TypeToken<GResponse<T_Phr_BaseInfo>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        showToast(R.string.msg_success_register);
                        finish(R.anim.push_right_in, R.anim.push_right_out);
                        AutoLoginEvent event = new AutoLoginEvent();
                        event.phone = mPhoneNumber.getText().toString();
                        event.password = mPasswordInput.getText().toString();
                        EventBus.getDefault().post(event);
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegisterActivity.this, (Exception) error.getException());
                    }
                });

    }

    private void commitAsync() {
        doCancellableAsyncTask(this, getString(R.string.msg_loading_register), new AsyncCallback<Response>() {

            @Override
            public Response callAsync() throws Exception {
                //TODO: 手机作为用户名
                mUser.setUserCode(mPhoneNumber.getText().toString());
                //mUser.setUserCode(mFromIntentUserName.getText().toString());
                mUser.setMobilePhone(mPhoneNumber.getText().toString());
                mUser.setUserPwd(MD5Util.string2MD5(mPasswordInput.getText().toString()));
                T_Phr_BaseInfo baseinfo = new T_Phr_BaseInfo();
                baseinfo.setEhrID(UUID.randomUUID().toString());
                baseinfo.setSelfPhone(mPhoneNumber.getText().toString());
                baseinfo.setIDCard(mIdCard.getText().toString());
                baseinfo.setSex(CommonUtils.getSexFromIdCard(mIdCard.getText().toString()));
                return WebServiceController.register(mUser, baseinfo);
            }

            @Override
            public void onPostCall(Response result) {
                if (result != null && result.getStatus() == WebServiceController.SUCCESS) {
                    showToast(R.string.msg_success_register);
                    finish(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    showToast(result.getResultMsg());
                }

            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e(TAG, "commitAsync#onCallFailed", exception);
                showToast(R.string.msg_service_disconnected);
            }
        });
    }

    private void siwtchClick(View v) {
        CheckedTextView tv = (CheckedTextView) v;
        tv.setChecked(!tv.isChecked());
        if (tv.isChecked()) {
            mPasswordInput.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        } else {
            mPasswordInput
                    .setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
        }
    }

    private void getValidateCodeAsync(final View v) {
        ApiCodeTemplate.getValidationCodeAsync(this, TAG, mPhoneNumber.getText().toString(),
                new RequestCallback<T_Phone_AuthCode>() {
                    @Override
                    public void onRequestSuccess(T_Phone_AuthCode result) {
                        mIsNeedResetValidateCode = false;
                        if (result != null) {
                            if (mCodeService == null) {
                                mCodeService = new ValidateCodeService();
                            }
                            mCodeService.start(mPhoneNumber.getText().toString(), result.getAuthCode());
                            // 发送成功后，阻塞验证码按钮
                            ((ValidateButton) v).sendBlockMessage();
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

    private void getValidationCode(final View v) {
        doForcableAsyncTask(this, getString(R.string.msg_mobile_send),
                new AsyncCallback<T_Phone_AuthCode>() {

                    @Override
                    public T_Phone_AuthCode callAsync() throws Exception {
                        if (mCodeService == null) {
                            mCodeService = new ValidateCodeService();
                        }
                        return WebServiceController
                                .getValidationCode(mPhoneNumber.getText().toString());
                    }

                    @Override
                    public void onPostCall(T_Phone_AuthCode result) {
                        if (result != null) {
                            mCodeService.start(mPhoneNumber.getText().toString(), result.getAuthCode());
                            // 发送成功后，阻塞验证码按钮
                            ((ValidateButton) v).sendBlockMessage();
                        } else {
                            showToast(R.string.msg_error_cannot_get_code_1);
                        }
                    }

                    @Override
                    public void onCallFailed(Exception exception) {
                        Logger.e(TAG, exception.getLocalizedMessage() + "");
                        showToast(R.string.msg_error_cannot_get_code_2);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mIdCard = (EditText) findViewById(R.id.user_register_id_card);
        mUserCode = (EditText) findViewById(R.id.user_register_username);
        mName = (EditText) findViewById(R.id.user_register_name);
        mPhoneNumber = (EditText) findViewById(R.id.user_register_phone);
        mValidationCode = (EditText) findViewById(R.id.user_register_validate_code);
        mPasswordInput = (EditText) findViewById(R.id.user_register_password_input);

        mLyoutValidate = findViewById(R.id.register_layout_validate);

        mBtnGetValidateCode = (ValidateButton) findViewById(R.id.user_register_validate);
        mCtvProtocols = (CheckedTextView) findViewById(R.id.user_register_protocols);

        mCtvProtocols.setChecked(true);

        mBtnGetValidateCode.setEnabled(false);
        TextUtil.addUnderlineText(this, mCtvProtocols, 7, 13, new IHyperlinkText() {

            @Override
            public void hyperlinkClick() {
                WebDialog dialog = new WebDialog(RegisterActivity.this);
                dialog.setCheckNetwork(false);
                dialog.setOnWebDialogErrorListener(new OnWebDialogErrorListener() {

                    @Override
                    public void urlError() {
                        showToast("url 错误，请检查url地址");
                    }

                    @Override
                    public void networkError() {
                        //do nothing.
                    }
                });
                dialog.loadUrl(Constants.URL_AGGREMENT);
                dialog.setTitle(getString(R.string.title_aggrement));
                dialog.setLeftButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCtvProtocols.setChecked(true);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void initEvents() {
        mPhoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIsNeedResetValidateCode = true;
                if (s.length() > 0 && TextUtil.matchPhone(s.toString()) && !mBtnGetValidateCode.isBlocked()) {
                    mBtnGetValidateCode.setEnabled(true);
                } else {
                    mBtnGetValidateCode.setEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean validate() {
        CardValidator validator = new CardValidator();
        if (TextUtils.isEmpty(mPhoneNumber.getText())) {
            mPhoneNumber.setError("手机号码不能为空！");
            mPhoneNumber.requestFocus();
            return false;
        }
        if (!TextUtil.matchPhone(mPhoneNumber.getText().toString())) {
            mPhoneNumber.setError("手机号码格式不正确！");
            mPhoneNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mName.getText())) {
            mName.setError("姓名不能为空！");
            mName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mIdCard.getText())) {
            mIdCard.setError("身份证号不能为空！");
            mIdCard.requestFocus();
            return false;
        }
        if (validator.validate(mIdCard.getText().toString(), CardValidator.CARD_ID) != Validator.SUCCESS) {
            mIdCard.setError("身份证号格式不正确!");
            mIdCard.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mPasswordInput.getText())) {
            mPasswordInput.setError("密码不能为空");
            mPasswordInput.requestFocus();
            return false;
        }
        if (mPasswordInput.getText().length() < 6) {
            mPasswordInput.setError("密码不能小于6位");
            mPasswordInput.requestFocus();
            return false;
        }
        //TODO:屏蔽
        if (mNeedMobileValidate) {
            if (mCodeService == null || mIsNeedResetValidateCode) {
                showToast(R.string.msg_get_validate_code_first);
                return false;
            }
            if (TextUtils.isEmpty(mValidationCode.getText())) {
                mValidationCode
                        .setError(getText(R.string.msg_empty_validation_code));
                mValidationCode.requestFocus();
                return false;
            }
            if (!mCodeService.isValid(mPhoneNumber.getText().toString(), mValidationCode.getText().toString())) {
                mValidationCode
                        .setError(getText(R.string.msg_error_validation_code));
                mValidationCode.requestFocus();
                return false;
            }
            if (mCodeService.isExpired()) {
                mValidationCode
                        .setError(getText(R.string.msg_error_validation_expired));
                mValidationCode.requestFocus();
                return false;
            }
        }

        if (!mCtvProtocols.isChecked()) {
            showToast("请阅读并同意《服务条款》");
            return false;
        }
        return true;
    }

    private void isMobileRegisteredNew() {
        ApiCodeTemplate.isMobileRegisteredAsync(this, TAG, mPhoneNumber.getText().toString(), new RequestCallback<G5117>() {
            @Override
            public void onRequestSuccess(G5117 g5117) {
                if (g5117.getMobilePhoneExist() == G5117.REGISTERED) {
                    showToast(R.string.msg_error_phone_registered);
                } else {
                    commitNewAsync();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegisterActivity.this, (Exception) error.getException());
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
                        phoneInfo.setMobilePhone(mPhoneNumber.getText().toString());
                        return WebServiceController.isMobileRegistered(phoneInfo);
                    }

                    @Override
                    public void onPostCall(Boolean result) {
                        if (result != null) {
                            if (result.booleanValue()) {
                                showToast(R.string.msg_error_phone_registered);
                            } else {
                                commitNewAsync();
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
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AutoLoginEvent {
        String phone;
        String password;
    }
}
