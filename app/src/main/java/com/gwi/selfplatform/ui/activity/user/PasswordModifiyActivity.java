package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ShakableEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/19 0019.
 */
public class PasswordModifiyActivity extends HospBaseActivity {
    private static final String TAG = PasswordModifiyActivity.class.getSimpleName();

    @Bind(R.id.et_password_old)
    ShakableEditText mEtPasswordOld;
    @Bind(R.id.et_password_new)
    ShakableEditText mEtPasswordNew;
    @Bind(R.id.et_password_new_confirm)
    ShakableEditText mEtPasswordNewConfirm;
    @Bind(R.id.btn_password_commit)
    Button mBtnPasswordCommit;
    @Bind(R.id.ck_hide)
    CheckedTextView mCkHide;

    @OnClick({R.id.btn_password_commit, R.id.ck_hide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_password_commit:
                if (validate()) {
                    modifyPasswordAsync();
                }
                break;
            case R.id.ck_hide:
                Logger.i(TAG, "isChecked = " + mCkHide.isChecked());
                mCkHide.setChecked(!mCkHide.isChecked());
                CommonUtils.setInputType(mEtPasswordOld, !mCkHide.isChecked());
                CommonUtils.setInputType(mEtPasswordNew, !mCkHide.isChecked());
                CommonUtils.setInputType(mEtPasswordNewConfirm, !mCkHide.isChecked());
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
            if (!TextUtils.isEmpty(mEtPasswordOld.getText().toString())
                    && !TextUtils.isEmpty(mEtPasswordNew.getText().toString())
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
        mEtPasswordOld.addTextChangedListener(mTextWatcher);
        mEtPasswordNew.addTextChangedListener(mTextWatcher);
        mEtPasswordNewConfirm.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void initViews() {
        setTitle("修改密码");
        mCkHide.setChecked(true);
        mBtnPasswordCommit.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modify);
        ButterKnife.bind(this);
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean validate() {
        String old = MD5Util.string2MD5(mEtPasswordOld.getText().toString());
        String newPass = mEtPasswordNew.getText().toString();
        String passConfirm = mEtPasswordNewConfirm.getText().toString();
        if (TextUtils.isEmpty(old)) {
            mEtPasswordOld.shakeText();
            showToast("请输入旧密码！");
            return false;
        } else {
            if (!old.equals(GlobalSettings.INSTANCE.getCurrentUser().getUserPwd())) {
                mEtPasswordOld.shakeText();
                showToast("旧密码不正确！");
                return false;
            }
        }
        if (TextUtils.isEmpty(newPass)) {
            mEtPasswordNew.shakeText();
            showToast("请输入新密码！");
            return false;
        } else if (newPass.length() < 6) {
            mEtPasswordNew.shakeText();
            showToast("密码长度不得低于6位!");
            return false;
        } else if (newPass.equals(old)) {
            mEtPasswordNew.shakeText();
            showToast("新旧密码不能一致!");
            return false;
        } else {
            if (!passConfirm.equals(newPass)) {
                mEtPasswordNewConfirm.shakeText();
                showToast(R.string.msg_error_pwd_not_same);
                return false;
            }
        }
        return true;
    }

    private void modifyPasswordAsync() {
        T_UserInfo user = new T_UserInfo();
        user.setUserPwd(MD5Util.string2MD5(mEtPasswordNew.getText().toString()));

        ApiCodeTemplate.modifyPasswordAsync(this, TAG, user, new RequestCallback<GBase>() {

            @Override
            public void onRequestSuccess(GBase result) {
                if (result != null) {
                    GlobalSettings.INSTANCE.setCurrentUser(result.getUserInfo());
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    showToast(R.string.msg_success_modifying);
                } else {
                    showToast("更新数据失败");
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(PasswordModifiyActivity.this, (Exception) error.getException());
            }
        });
    }
}
