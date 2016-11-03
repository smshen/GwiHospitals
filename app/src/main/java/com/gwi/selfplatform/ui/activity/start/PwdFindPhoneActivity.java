package com.gwi.selfplatform.ui.activity.start;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.validator.IValidate;
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
import com.gwi.selfplatform.ui.view.ValidateButton;

public class PwdFindPhoneActivity extends HospBaseActivity implements IValidate,
        OnClickListener {

    private static final String TAG = PwdFindPhoneActivity.class
            .getSimpleName();
    public static final String KEY_USER_ID = "key_user_id";
    //用户名
//    private ShakableEditText mSetUserCode = null;
    private ShakableEditText mSetPhone = null;
    private ShakableEditText mSetCode = null;
    private ValidateButton mBtnValidte = null;
    private Button mBtnNext = null;

    private ValidateCodeService mCodeService = null;

    private T_UserInfo mValidateUser = null;

    private long mUserId;

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (!(TextUtils.isEmpty(mSetCode.getText())
                    /*|| TextUtils.isEmpty(mSetUserCode.getText())*/ || TextUtils
                    .isEmpty(mSetPhone.getText()))) {
                mBtnNext.setEnabled(true);
            } else {
                mBtnNext.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_BOROADCAST_VALIDATION_CODE.equals(intent
                    .getAction())) {
                Bundle b = intent.getExtras();
                if (b != null && b.containsKey(Constants.EXTRA_VALIDATION_CODE)) {
                    mSetCode.setText(b
                            .getString(Constants.EXTRA_VALIDATION_CODE));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_find_phone);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mSmsReceiver);
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setTitle(R.string.label_forgot_password);
//        mSetUserCode = (ShakableEditText) findViewById(R.id.pwd_find_phone_user_name);
        mSetPhone = (ShakableEditText) findViewById(R.id.pwd_find_phone_number);
        mSetCode = (ShakableEditText) findViewById(R.id.validate_code);
        mBtnValidte = (ValidateButton) findViewById(R.id.validate_send);
        mBtnNext = (Button) findViewById(R.id.pwd_find_phone_btn_next);
        mValidateUser = new T_UserInfo();

        registerReceiver(mSmsReceiver, new IntentFilter(
                Constants.ACTION_BOROADCAST_VALIDATION_CODE));
        mBtnNext.setEnabled(false);

        mCodeService = new ValidateCodeService();
    }

    @Override
    protected void initEvents() {
        mBtnNext.setOnClickListener(this);
        mBtnValidte.setOnClickListener(this);

        mSetCode.addTextChangedListener(mTextWatcher);
//        mSetUserCode.addTextChangedListener(mTextWatcher);
        mSetPhone.addTextChangedListener(mTextWatcher);
    }

    private void validateResetInfoNewAsync() {
        final Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_PASSWORD_FIND_VALIDATION, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(mValidateUser);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
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
                            mCodeService.start(mSetPhone.getText().toString(),code.getAuthCode().trim());
                            mUserId = result.getUserInfo().getUserId();
                            mBtnValidte.sendBlockMessage();
                        } else {
                            showToast(R.string.msg_error_pwd_find_phone_validation);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(PwdFindPhoneActivity.this, (Exception) error.getException());
                    }
                });
    }

//    private void validateResetInfoAsync() {
//        doForcableAsyncTask(this, getString(R.string.msg_mobile_validation),
//                new AsyncCallback<Response>() {
//
//                    @Override
//                    public Response callAsync() throws Exception {
//                        Response response = WebServiceController
//                                .validateBeforeResetPassword(mValidateUser);
//                        if (response.getStatus() != WebServiceController.SUCCESS) {
//                            throw new Exception(response.getResultMsg());
//                        }
//                        return response;
//                    }
//
//                    @Override
//                    public void onPostCall(Response result) {
//                        T_Phone_AuthCode code = result.getPhone_AuthCode();
//                        if (code != null) {
//                            mCodeService.start(code.getAuthCode().trim());
////                            mCodeFromService = code.getAuthCode().trim();
//                            mUserId = result.getUserInfo().getUserId();
//                            mBtnValidte.sendBlockMessage();
//                        } else {
//                            showToast(R.string.msg_error_pwd_find_phone_validation);
//                        }
//                    }
//
//                    @Override
//                    public void onCallFailed(Exception exception) {
//                        Logger.e(TAG, "validateResetInfoAsync#onCallFailed",
//                                exception);
//                        if (TextUtils.isEmpty(exception.getLocalizedMessage())) {
//                            showToast(R.string.msg_service_disconnected);
//                        } else {
//                            showToast(exception.getLocalizedMessage());
//                        }
//                    }
//                });
//    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean validate() {
//        if (TextUtils.isEmpty(mSetUserCode.getText())) {
//            mSetUserCode.setError(getText(R.string.msg_empty_name));
//            mSetUserCode.shakeText();
//            return false;
//        }
//        if (TextUtils.isEmpty(mSetPhone.getText())) {
//            mSetPhone.setError(getText(R.string.msg_empty_phone));
//            mSetUserCode.shakeText();
//            return false;
//        }
        //name->code
//        mValidateUser.setUserCode(mSetUserCode.getText().toString());
        mValidateUser.setMobilePhone(mSetPhone.getText().toString());
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.validate_send) {
            if (validate()) {
                validateResetInfoNewAsync();
            }
        } else if (id == R.id.pwd_find_phone_btn_next) {
            doNext();
        }

    }

    private void doNext() {
        //TODO:屏蔽验证码
        if (TextUtils.isEmpty(mSetCode.getText())) {
            mSetCode.setError(getText(R.string.msg_empty_validation_code));
            mSetCode.shakeText();
            return;
        } else if (!mCodeService.isValid(mSetPhone.getText().toString(),mSetCode.getText().toString())) {
            mSetCode.setError(getText(R.string.msg_error_validation_code));
            mSetCode.shakeText();
            return;
        } else {
            if (mCodeService.isExpired()) {
                mSetCode.setError(getText(R.string.msg_error_validation_expired));
                mSetCode.shakeText();
                return;
            }
        }
        Bundle b = new Bundle();
        b.putLong(KEY_USER_ID, mUserId);
        openActivity(SetNewPasswordActivity.class, b);
        //Bug resolved. 
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
