package com.gwi.selfplatform.ui.activity.start;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.interfaces.IHyperlinkText;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.dialog.WebDialog;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

@Deprecated
public class RegisterV2Activity extends HospBaseActivity {
    public static final String TAG = RegisterV2Activity.class.getSimpleName();

    /**
     * Senario:如果已经获取了验证码，在修改手机号，就需要重新获取验证码
     */
    // private boolean mIsNeedResetValidateCode = false;

    // private ValidateCodeService mCodeService;

    @Bind(R.id.et_register_num)
    ShakableEditText mEtRegisterNum;

    @Bind(R.id.btn_get_code)
    Button mBtnGetCode;

    @Bind(R.id.txt_register_protocols)
    CheckedTextView mTxtRegisterProtocols;

    @OnClick({R.id.btn_get_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                isMobileRegisteredNew();
                break;
            case R.id.txt_register_protocols:
                break;
            default:
                break;
        }
    }

    @OnTextChanged(value = R.id.et_register_num, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        // mIsNeedResetValidateCode = true;
        if (s.length() > 0 && TextUtil.matchPhone(s.toString())/* && !mBtnGetValidateCode.isBlocked()*/) {
            mBtnGetCode.setEnabled(true);
        } else {
            mBtnGetCode.setEnabled(false);
        }
    }

    @Override
    protected void initViews() {
        setTitle("用户注册");
        mBtnGetCode.setEnabled(false);
    }

    @Override
    protected void initEvents() {
        TextUtil.addUnderlineText(this, mTxtRegisterProtocols, 7, 15, new IHyperlinkText() {

            @Override
            public void hyperlinkClick() {
                WebDialog dialog = new WebDialog(RegisterV2Activity.this);
                dialog.setCheckNetwork(false);
                dialog.setOnWebDialogErrorListener(new WebDialog.OnWebDialogErrorListener() {

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
                dialog.setLeftButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_v2);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    private void isMobileRegisteredNew() {
        ApiCodeTemplate.isMobileRegisteredAsync(this, TAG, mEtRegisterNum.getText().toString().trim(), new RequestCallback<G5117>() {
            @Override
            public void onRequestSuccess(G5117 g5117) {
                if (g5117.getMobilePhoneExist() == G5117.REGISTERED) {
                    showToast(R.string.msg_error_phone_registered);
                } else {
                    // commitNewAsync();
                    finish(R.anim.push_right_in, R.anim.push_right_out);

                    Bundle bundle = new Bundle();
                    bundle.putString("PhoneNumber", mEtRegisterNum.getText().toString().trim());
                    // bundle.putString("AuthCode", result.getAuthCode());
                    openActivity(RegisterInputActivity.class, bundle);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegisterV2Activity.this, (Exception) error.getException());
            }
        });
    }

//    private void getValidateCodeAsync(final View v) {
//        ApiCodeTemplate.getValidationCodeAsync(this, TAG, mEtRegisterNum.getText().toString().trim(),
//                new RequestCallback<T_Phone_AuthCode>() {
//                    @Override
//                    public void onRequestSuccess(T_Phone_AuthCode result) {
//                        // mIsNeedResetValidateCode = false;
//                        if (result != null) {
////                            if (mCodeService == null) {
////                                mCodeService = new ValidateCodeService();
////                            }
////                            mCodeService.start(result.getAuthCode());
////                            // 发送成功后，阻塞验证码按钮
////                            ((ValidateButton) v).sendBlockMessage();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("PhoneNumber", result.getPhoneNumber());
//                            bundle.putString("AuthCode", result.getAuthCode());
//                            openActivity(RegisterInputActivity.class, bundle);
//                        } else {
//                            showToast(R.string.msg_error_cannot_get_code_1);
//                        }
//                    }
//
//                    @Override
//                    public void onRequestError(RequestError error) {
//                        Logger.e(TAG, "onRequestError", (Exception) error.getException());
//                        showToast(R.string.msg_error_cannot_get_code_2);
//                    }
//                });
//    }
}
