package com.gwi.selfplatform.ui.activity.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.service.ValidateCodeService;
import com.gwi.selfplatform.ui.view.ShakableEditText;
import com.gwi.selfplatform.ui.view.ValidateButton;

/**
 * 修改手机
 *
 * @author 彭毅
 */
public class MobileModifyActivity extends HospBaseActivity implements
        OnClickListener {

    private static final String TAG = MobileModifyActivity.class
            .getSimpleName();

    private ViewFlipper mVfModifyContainer = null;
    private Button mBtnNext = null;

    private ShakableEditText mSetOldValidationCode = null;
    private ShakableEditText mSetNewPhone = null;
    private ShakableEditText mSetNewValidationCode = null;

    private ValidateButton mBtnOldSendCode = null;
    private ValidateButton mBtnNewSendCode = null;

    private TextView mTvPhoneNewHint = null;

    private static final int STEP_1 = 0;
    private static final int STEP_2 = 1;
    private static final int STEP_3 = 2;
    private int mCurrentStep = STEP_1;

    private ValidateCodeService mCodeService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_modify);
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

        mVfModifyContainer = (ViewFlipper) findViewById(R.id.mobile_vf_container);
        mSetOldValidationCode = (ShakableEditText) findViewById(R.id.mobile_modify_phone_old);
        mBtnOldSendCode = (ValidateButton) findViewById(R.id.mobile_modify_old_resend);
        mSetNewPhone = (ShakableEditText) findViewById(R.id.mobile_modify_phone);
        mSetNewValidationCode = (ShakableEditText) findViewById(R.id.mobile_validate_code);
        mBtnNewSendCode = (ValidateButton) findViewById(R.id.mobile_validate_resend);
        mBtnNext = (Button) findViewById(R.id.mobile_modify_next);
        // 加载当前的手机号码
        final TextView oldPhoneHint = (TextView) findViewById(R.id.mobile_modify_tv_phone_old);
        mTvPhoneNewHint = (TextView) findViewById(R.id.mobile_validate_phone_new);
        oldPhoneHint.setText(String.format(getString(R.string.mobile_old_hint),
                GlobalSettings.INSTANCE.getCurrentUser().getMobilePhone()));
    }

    @Override
    protected void initEvents() {
        mBtnOldSendCode.setOnClickListener(this);
        mBtnNewSendCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mobile_modify_old_resend) {
            getValidateCode(v, GlobalSettings.INSTANCE.getCurrentUser()
                    .getMobilePhone());
        } else if (id == R.id.mobile_validate_resend) {
            getValidateCode(v, mSetNewPhone.getText().toString());
        } else if (id == R.id.mobile_modify_next) {
            doNext();
        }
    }

    private void getValidateCode(final View button, final String phone) {
        if (mCodeService == null) {
            mCodeService = new ValidateCodeService();
        }
        ApiCodeTemplate.getValidationCodeAsync(this, TAG, phone, new RequestCallback<T_Phone_AuthCode>() {
            @Override
            public void onRequestSuccess(T_Phone_AuthCode result) {
                if (result != null) {
                    mCodeService.start(phone,result.getAuthCode());
                    ((ValidateButton) button).sendBlockMessage();
                } else {
                    showToast(R.string.msg_error_cannot_get_code_1);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MobileModifyActivity.this, (Exception) error.getException());
            }
        });
//        doForcableAsyncTask(this, getString(R.string.msg_mobile_send),
//                new AsyncCallback<T_Phone_AuthCode>() {
//
//                    @Override
//                    public T_Phone_AuthCode callAsync() throws Exception {
//                        return WebServiceController.getValidationCode(phone);
//                    }
//
//                    @Override
//                    public void onPostCall(T_Phone_AuthCode result) {
//                        if (result != null) {
//                            mCodeService.start(result.getAuthCode());
//                            ((ValidateButton) button).sendBlockMessage();
//                        } else {
//                            showToast(R.string.msg_error_cannot_get_code_1);
//                        }
//                    }
//
//                    @Override
//                    public void onCallFailed(Exception exception) {
//                        Logger.e(TAG, "getValidateCode#onCallFailed", exception);
//                        showToast(R.string.msg_error_cannot_get_code_2);
//                    }
//                });
    }

    private void doNext() {
        String oldMobilePhone = GlobalSettings.INSTANCE.getCurrentUser().getMobilePhone();
        switch (mCurrentStep) {
            case STEP_1:
                if (TextUtils.isEmpty(mSetOldValidationCode.getText().toString())) {
                    mSetOldValidationCode
                            .setError(getText(R.string.msg_empty_validation_code));
                    return;
                }
                if (mCodeService == null) {
                    showToast(R.string.msg_get_validate_code_first);
                    return;
                } else if (!mCodeService.isValid(
                        oldMobilePhone,
                        mSetOldValidationCode.getText().toString())) {
                    mSetOldValidationCode
                            .setError(getText(R.string.msg_error_validation_code));
                    return;
                } else if (mCodeService.isExpired()) {
                    mSetOldValidationCode
                            .setError(getText(R.string.msg_error_validation_expired));
                    return;
                }
                break;
            case STEP_2:
                if (TextUtils.isEmpty(mSetNewPhone.getText().toString())) {
                    mSetNewPhone.setError(getString(R.string.msg_empty_phone));
                    return;
                } else if (!TextUtil.matchPhone(mSetNewPhone.getText().toString())) {
                    mSetNewPhone.setError("手机号码格式不正确!");
                    return;
                } else if (mSetNewPhone.getText().toString().equals(
                        GlobalSettings.INSTANCE.getCurrentUser().getMobilePhone())) {

                } else {
                    mTvPhoneNewHint.setText(String.format(
                            getString(R.string.mobile_new), mSetNewPhone.getText()
                                    .toString()));
                }
                isMobileRegistered();
                return;
            case STEP_3:
                if (TextUtils.isEmpty(mSetNewValidationCode.getText().toString())) {
                    mSetNewValidationCode
                            .setError(getText(R.string.msg_empty_validation_code));
                    return;
                } else if (!mCodeService.isValid(mSetNewPhone.getText().toString(),
                        mSetNewValidationCode.getText().toString())) {
                    mSetNewValidationCode
                            .setError(getText(R.string.msg_error_validation_code));
                    return;
                } else if (mCodeService.isExpired()) {
                    mSetNewValidationCode
                            .setError(getText(R.string.msg_error_validation_expired));
                    return;
                } else {
                    modifyPhoneAsync();
                }
                return;
        }
        mCurrentStep++;
        mVfModifyContainer.setInAnimation(this, R.anim.push_left_in);
        mVfModifyContainer.setOutAnimation(this, R.anim.push_left_out);
        mVfModifyContainer.showNext();
    }

    private void isMobileRegistered() {
        ApiCodeTemplate.isMobileRegisteredAsync(this, TAG, mSetNewPhone.getText().toString(), new RequestCallback<G5117>() {
            @Override
            public void onRequestSuccess(G5117 result) {
                 if (result.getMobilePhoneExist() == G5117.REGISTERED) {
//                if (result.isMobilePhoneExist()) {
                    showToast(R.string.msg_error_phone_registered);
                } else {
                    mBtnNext.setText("完成");
                    mCurrentStep++;
                    mVfModifyContainer.setInAnimation(
                            MobileModifyActivity.this,
                            R.anim.push_left_in);
                    mVfModifyContainer.setOutAnimation(
                            MobileModifyActivity.this,
                            R.anim.push_left_out);
                    mVfModifyContainer.showNext();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MobileModifyActivity.this, (Exception) error.getException());
            }
        });
//
//        doCancellableAsyncTask(this, getText(R.string.msg_loading_validate),
//                new AsyncCallback<Boolean>() {
//
//                    @Override
//                    public Boolean callAsync() throws Exception {
//                        T_UserInfo phoneInfo = new T_UserInfo();
//                        phoneInfo.setMobilePhone(mSetNewPhone.getText()
//                                .toString());
//                        return WebServiceController
//                                .isMobileRegistered(phoneInfo);
//                    }
//
//                    @Override
//                    public void onPostCall(Boolean result) {
//                        if (result != null) {
//                            if (result.booleanValue()) {
//                                showToast(R.string.msg_error_phone_registered);
//                            } else {
//                                mBtnNext.setText("完成");
//                                mCurrentStep++;
//                                mVfModifyContainer.setInAnimation(
//                                        MobileModifyActivity.this,
//                                        R.anim.push_left_in);
//                                mVfModifyContainer.setOutAnimation(
//                                        MobileModifyActivity.this,
//                                        R.anim.push_left_out);
//                                mVfModifyContainer.showNext();
//                            }
//                        } else {
//                            showToast(R.string.msg_service_disconnected);
//                        }
//                    }
//
//                    @Override
//                    public void onCallFailed(Exception exception) {
//                        Logger.e(TAG, exception.getLocalizedMessage() + "");
//                        if (exception.getLocalizedMessage() != null) {
//                            showToast(exception.getLocalizedMessage());
//                        } else {
//                            showToast(R.string.msg_service_disconnected);
//                        }
//                    }
//                });
    }

    private void doPrevious() {
        if (mCurrentStep == STEP_3) {
            mBtnNext.setText("下一步");
        }
        mCurrentStep--;
        mVfModifyContainer.setInAnimation(this, R.anim.push_right_in);
        mVfModifyContainer.setOutAnimation(this, R.anim.push_right_out);
        mVfModifyContainer.showPrevious();
    }

    private void goBack() {
        BaseDialog dialog = new BaseDialog(this);
        dialog.showHeader(true);
        dialog.showFooter(true);
        dialog.setTitle("提示");
        dialog.setContent("放弃更改手机号码？");
        dialog.setTwoButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }, "确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            }
        });
        dialog.show();
    }

    private void modifyPhoneAsync() {
        ApiCodeTemplate.modifyPhoneAsync(this, TAG, mSetNewPhone.getText().toString(), new RequestCallback<T_UserInfo>() {
            @Override
            public void onRequestSuccess(T_UserInfo result) {
                if (result != null) {
                    GlobalSettings.INSTANCE.setCurrentUser(result);
                    T_Phr_BaseInfo family = GlobalSettings.INSTANCE
                            .getCurrentFamilyAccount();
                    family.setSelfPhone(result.getMobilePhone());
                    GlobalSettings.INSTANCE.setCurrentFamilyAccount(family);
                    // 保存到DB
                    DBController.INSTANCE.saveUser(result);
                    DBController.INSTANCE.saveFamilyAccount(family);
                    showToast("修改成功");
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,
                            R.anim.push_right_out);
                } else {
                    showToast("修改手机号码失败，请稍后再试！");
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MobileModifyActivity.this, (Exception) error.getException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mCurrentStep < 1) {
                    goBack();
                } else {
                    doPrevious();
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mCurrentStep < 1) {
            goBack();
        } else {
            doPrevious();
        }
//        super.onBackPressed();
    }

}
