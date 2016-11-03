package com.gwi.selfplatform.ui.activity.start;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/23 0023.
 */
@Deprecated
public class SetNewPasswordV2Activity extends HospBaseActivity {
    private static final String TAG = SetNewPasswordV2Activity.class.getSimpleName();

    private Long mUserId = null;

    @Bind(R.id.et_password_new)
    ShakableEditText mEtPasswordNew;
    @Bind(R.id.et_password_new_confirm)
    ShakableEditText mEtPasswordNewConfirm;
    @Bind(R.id.btn_password_commit)
    Button mBtnPasswordCommit;

    @OnClick({R.id.btn_password_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_password_commit:
                if (validate()) {
                    setNewPasswordNewAsync();
                }
                break;
            default:
                break;
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(mEtPasswordNew.getText().toString())
                    && !TextUtils.isEmpty(mEtPasswordNewConfirm.getText().toString())) {
                mBtnPasswordCommit.setEnabled(true);
            } else {
                mBtnPasswordCommit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    @Override
    protected void initEvents() {
        mEtPasswordNew.addTextChangedListener(mTextWatcher);
        mEtPasswordNewConfirm.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initViews() {
        mBtnPasswordCommit.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_new);
        ButterKnife.bind(this);
        initViews();
        initEvents();

//        Bundle b = getIntent().getExtras();
//        if (b != null && b.containsKey(PwdFindPhoneActivity.KEY_USER_ID)) {
//            mUserId = b.getLong(PwdFindPhoneActivity.KEY_USER_ID);
//        } else {
//            // TODO:
//            finish();
//        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mEtPasswordNew.getText())) {
            mEtPasswordNew.setError(getText(R.string.msg_empty_password));
            mEtPasswordNew.requestFocus();
            return false;
        }
        if (mEtPasswordNew.getText().length() < 6) {
            mEtPasswordNew.setError(getText(R.string.password_too_short));
            mEtPasswordNew.requestFocus();
            return false;
        }
        if (!mEtPasswordNew.getText().toString()
                .equals(mEtPasswordNewConfirm.getText().toString())) {
            mEtPasswordNewConfirm
                    .setError(getText(R.string.msg_error_pwd_not_same));
            mEtPasswordNewConfirm.requestFocus();
            return false;
        }
        return true;
    }

    private void setNewPasswordNewAsync() {
        T_UserInfo userinfo = new T_UserInfo();
        userinfo.setUserId(mUserId);
        userinfo.setUserPwd(MD5Util.string2MD5(mEtPasswordNew.getText().toString()));

        ApiCodeTemplate.setNewPasswordAsync(this, TAG, userinfo, new RequestCallback<GBase>() {
            @Override
            public void onRequestSuccess(GBase result) {
                showToast(R.string.msg_success_modifying);
                // backToLogin();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(SetNewPasswordV2Activity.this, (Exception) error.getException());
            }
        });
    }
}
