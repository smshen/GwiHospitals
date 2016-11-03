package com.gwi.selfplatform.ui.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.ccly.android.commonlibrary.common.security.MD5Util;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.Validator;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

@Deprecated
public class RegisterInfoActivity extends HospBaseActivity {
    public static final String TAG = RegisterInputActivity.class.getSimpleName();

    private String mPhoneNumber;
    private String mPassword;

    private T_UserInfo mUser;

    @Bind(R.id.et_register_name)
    ShakableEditText mEtRegisterName;
    @Bind(R.id.et_idcard)
    ShakableEditText mEtIdcard;
    @Bind(R.id.btn_register_complete)
    Button mBtnRegisterComplete;

    @OnClick({R.id.btn_register_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_complete:
                if (validate()) {
                    if (mUser == null) {
                        mUser = new T_UserInfo();
                    }
                    mUser.setMobilePhone(mPhoneNumber);
                    isMobileRegisteredNew();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initViews() {
        setTitle("个人资料");
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
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
            mPassword = bundle.getString("Password");
            Logger.i(TAG, "mPhoneNumber = " + mPhoneNumber + " ; mPassword = " + mPassword);
        }
    }

    private boolean validate() {
        CardValidator validator = new CardValidator();

        if (TextUtils.isEmpty(mEtRegisterName.getText().toString())) {
            mEtRegisterName.setError("姓名不能为空！");
            mEtRegisterName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mEtIdcard.getText().toString())) {
            mEtIdcard.setError("身份证号不能为空！");
            mEtIdcard.requestFocus();
            return false;
        }
        if (validator.validate(mEtIdcard.getText().toString(), CardValidator.CARD_ID) != Validator.SUCCESS) {
            mEtIdcard.setError("身份证号格式不正确!");
            mEtIdcard.requestFocus();
            return false;
        }
        return true;
    }

    private void isMobileRegisteredNew() {
        ApiCodeTemplate.isMobileRegisteredAsync(this, TAG, mPhoneNumber, new RequestCallback<G5117>() {
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
                CommonUtils.showError(RegisterInfoActivity.this, (Exception) error.getException());
            }
        });
    }

    private void commitNewAsync() {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        Request.commonHeader(request, Request.FUN_CODE_USER_REGISTER, true);

        request.setBody(new TBase());
        mUser.setUserCode(mPhoneNumber);
        mUser.setUserName(mEtRegisterName.getText().toString());
        //mUser.setUserCode(mFromIntentUserName.getText().toString());
        mUser.setMobilePhone(mPhoneNumber);
        mUser.setUserPwd(MD5Util.string2MD5(mPassword));
        T_Phr_BaseInfo baseinfo = new T_Phr_BaseInfo();
        baseinfo.setEhrID(UUID.randomUUID().toString());
        baseinfo.setSelfPhone(mPhoneNumber);
        baseinfo.setName(mEtRegisterName.getText().toString());
        baseinfo.setIDCard(mEtIdcard.getText().toString());
        try {
            baseinfo.setSex(CommonUtils.getSexFromIdCard(mEtIdcard.getText().toString()));
            baseinfo.setBirthDay(CommonUtils.getDateFromIDCard(mEtIdcard.getText().toString()));
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
                        RegisterActivity.AutoLoginEvent event = new RegisterActivity.AutoLoginEvent();
                        event.phone = mPhoneNumber;
                        event.password = mPassword;
                        EventBus.getDefault().post(event);
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(RegisterInfoActivity.this, (Exception) error.getException());
                    }
                });
    }
}
