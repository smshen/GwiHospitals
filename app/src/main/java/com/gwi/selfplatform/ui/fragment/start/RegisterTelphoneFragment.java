package com.gwi.selfplatform.ui.fragment.start;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gwi.selfplatform.ui.activity.start.RegisterV3Activity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;
import com.gwi.selfplatform.ui.dialog.WebDialog;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 输入注册的手机号码
 */
public class RegisterTelphoneFragment extends HospBaseFragment {
    private static final String TAG = RegisterTelphoneFragment.class.getSimpleName();
    // private static final String KEY_TAG = "key_tag";
    private String mTpSource;
    private String mTpMobile;

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

    public RegisterTelphoneFragment() {

    }

    public static RegisterTelphoneFragment newInstance(String source, String mobile) {
        RegisterTelphoneFragment fragment = new RegisterTelphoneFragment();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.ThirdPart.SOURCE, source);
        arguments.putString(Constants.ThirdPart.MOBILE_NO, mobile);
        fragment.setArguments(arguments);
        return fragment;
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


    private void initViews() {
        mBtnGetCode.setEnabled(false);
        mEtRegisterNum.setText(mTpMobile);
    }


    private void initEvents() {
        TextUtil.addUnderlineText(getActivity(), mTxtRegisterProtocols, 7, 15, new IHyperlinkText() {

            @Override
            public void hyperlinkClick() {
                WebDialog dialog = new WebDialog(getActivity());
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

    private void isMobileRegisteredNew() {
        ApiCodeTemplate.isMobileRegisteredAsync(getActivity(), TAG, mEtRegisterNum.getText().toString().trim(), new RequestCallback<G5117>() {
            @Override
            public void onRequestSuccess(G5117 g5117) {
                if (g5117.getMobilePhoneExist() == G5117.REGISTERED) {
                    showToast(R.string.msg_error_phone_registered);
                } else {
                    ((RegisterV3Activity) getActivity()).gotoCodeFragment(mEtRegisterNum.getText().toString().trim());
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTpSource = getArguments().getString(Constants.ThirdPart.SOURCE);
        mTpMobile = getArguments().getString(Constants.ThirdPart.MOBILE_NO);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_telphone, container, false);
        ButterKnife.bind(this, view);
        initViews();
        initEvents();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
