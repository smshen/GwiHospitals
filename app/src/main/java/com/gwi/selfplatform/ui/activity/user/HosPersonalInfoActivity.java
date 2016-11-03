package com.gwi.selfplatform.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.IValidate;
import com.gwi.selfplatform.common.utils.validator.Validator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.dialog.DateTimePickerDialog;
import com.gwi.selfplatform.ui.view.DateTimePicker;

import java.util.Date;
import java.util.List;

/**
 * 个人信息设置页面
 *
 * @author 彭毅
 */
public class HosPersonalInfoActivity extends HospBaseActivity implements IValidate {

    protected static final String TAG = HosPersonalInfoActivity.class
            .getSimpleName();
    private static final int CODE_REQUEST_PHONE_MODIFY = 0x001;
    private TextView mtvPhone = null;
    private EditText mEtRealName = null;
    private RadioGroup mRgSex = null;
    private RadioButton mRbMale = null;
    private RadioButton mRbFemale = null;
    private TextView mTvIdCard = null;
    private EditText mEtAddress = null;
    private EditText mEtNickName = null;
    private TextView mTvBirth = null;
    private TextView mTvSex = null;

    private T_Phr_BaseInfo mFamilyAccount = null;
    private T_UserInfo mUserinfo = null;

    private boolean mIsChanged = false;

    private Integer mSelectedSex = null;

    private Date mBirthDate = null;

    private T_Phr_BaseInfo mNewFA = null;
    private T_UserInfo mNewUserInfo = null;
    CardValidator validator = null;

    private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            mIsChanged = true;
            if (isChecked) {
                if (buttonView.getId() == R.id.hos_personal_info_male) {
                    mSelectedSex = 1;
                } else {
                    mSelectedSex = 2;
                }
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            mIsChanged = true;

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_personal_info);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        initViews();
        initData();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setTitle(R.string.personal_info);
        mtvPhone = (TextView) findViewById(R.id.hos_personal_info_phone);
        mEtRealName = (EditText) findViewById(R.id.hos_personal_info_real_name);
        mTvIdCard = (TextView) findViewById(R.id.hos_personal_info_id_card);
        mEtAddress = (EditText) findViewById(R.id.hos_personal_info_address);
        mEtNickName = (EditText) findViewById(R.id.hos_personal_info_nick_name);
        mTvBirth = (TextView) findViewById(R.id.hos_personal_info_birth);
        mTvSex = (TextView) findViewById(R.id.hos_personal_info_sex_input);

        mRgSex = (RadioGroup) findViewById(R.id.hos_personal_info_sex);
        mRbMale = (RadioButton) findViewById(R.id.hos_personal_info_male);
        mRbFemale = (RadioButton) findViewById(R.id.hos_personal_info_female);
        validator = new CardValidator();

        mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.MSG_SHOULD_FINISH:
                        finish(R.anim.push_right_in, R.anim.push_right_out);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initEvents() {
        mRbMale.setOnCheckedChangeListener(mChangeListener);
        mRbFemale.setOnCheckedChangeListener(mChangeListener);

        mEtRealName.addTextChangedListener(mTextWatcher);
        mTvIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIDCardInputDialog();
            }
        });
        mEtAddress.addTextChangedListener(mTextWatcher);

        mTvBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(validator.validate((String) mTvIdCard.getTag(), CardValidator.CARD_ID) == Validator.SUCCESS)) {
                    showBirthDialog();
                }
            }
        });
    }

    private void showBirthDialog() {
        final DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog(this, DateTimePicker.Mode.date);
        dateTimePickerDialog.setTitle("请选择出生日期");
        dateTimePickerDialog.setLeftButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dateTimePickerDialog.setRightButton(getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBirthDate = dateTimePickerDialog.getCurrentDate().getTime();
                mTvBirth.setText(dateTimePickerDialog.getCurrentDateTimeString());
                dialog.dismiss();
            }
        });
        dateTimePickerDialog.show();
    }

    private void showIDCardInputDialog() {
        BaseDialog dlg = new BaseDialog(this);
        dlg.setTitle("请输入身份证号码");
        View content = dlg.setDialogContentView(R.layout.dialog_item_input);
        final EditText idCardText = (EditText) content.findViewById(android.R.id.text1);
        dlg.showHeader(true);
        dlg.showFooter(true);
        dlg.setLeftButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dlg.setRightButton(getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIsChanged = true;

                mTvIdCard.setText(idCardText.getText().toString());
                mTvIdCard.setTag(idCardText.getText().toString());
                if (!TextUtils.isEmpty(idCardText.getText())) {
                    if (validator.validate(idCardText.getText().toString(), CardValidator.CARD_ID) == Validator.SUCCESS) {
                        try {
                            mSelectedSex = CommonUtils.getSexFromIdCard(idCardText.getText().toString());
                            CharSequence sexType = getResources().getTextArray(
                                    R.array.sex_type)[mSelectedSex - 1];
                            mTvSex.setText(sexType);
                            mRgSex.setVisibility(View.GONE);
                            mTvSex.setVisibility(View.VISIBLE);
                            mTvBirth.setText(CommonUtils.getBirthFromIDCard(idCardText.getText().toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mTvSex.setVisibility(View.GONE);
                        mRgSex.setVisibility(View.VISIBLE);
                    }
                } else {
                    mTvSex.setVisibility(View.GONE);
                    mRgSex.setVisibility(View.VISIBLE);
                    mTvBirth.setText("");
                }
                dialog.dismiss();
            }
        });
        dlg.show();
    }

    private void getMainFamilyMemberNewAsync() {
        ApiCodeTemplate.getFamilyMembersAsync(this, TAG, new RequestCallback<List<T_Phr_BaseInfo>>() {
            @Override
            public void onRequestSuccess(List<T_Phr_BaseInfo> result) {
                T_UserInfo user = GlobalSettings.INSTANCE.getCurrentUser();
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        T_Phr_BaseInfo member = result.get(i);
                        if (member.getEhrID().equals(user.getEhrId())) {
                            mFamilyAccount = member;
                            setFamilyValue();
                            return;
                        }
                    }
                } else {
                    showToast(R.string.msg_error_common);
                    mHandler.sendEmptyMessageDelayed(Constants.MSG_SHOULD_FINISH, Constants.MILLIMS_DELAY_BACK);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(HosPersonalInfoActivity.this, (Exception) error.getException());
                mHandler.sendEmptyMessageDelayed(Constants.MSG_SHOULD_FINISH, Constants.MILLIMS_DELAY_BACK);
            }
        });
    }

    private void getMainFamilyMemberAsync() {
        doCancellableAsyncTask(this, getText(R.string.dialog_content_loading), new AsyncCallback<List<T_Phr_BaseInfo>>() {

            @Override
            public List<T_Phr_BaseInfo> callAsync() throws Exception {
                return WebServiceController.getFamilyMembers();
            }

            @Override
            public void onPostCall(List<T_Phr_BaseInfo> result) {
                T_UserInfo user = GlobalSettings.INSTANCE.getCurrentUser();
                if (result != null && !result.isEmpty()) {
                    for (int i = 0; i < result.size(); i++) {
                        T_Phr_BaseInfo member = result.get(i);
                        if (member.getEhrID().equals(user.getEhrId())) {
                            mFamilyAccount = member;
                            setFamilyValue();
                            return;
                        }
                    }
                } else {
                    showToast(R.string.msg_error_common);
                    mHandler.sendEmptyMessageDelayed(Constants.MSG_SHOULD_FINISH, Constants.MILLIMS_DELAY_BACK);
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                if (exception.getLocalizedMessage() != null) {

                } else {
                    showToast(R.string.msg_service_disconnected);
                    mHandler.sendEmptyMessageDelayed(Constants.MSG_SHOULD_FINISH, Constants.MILLIMS_DELAY_BACK);
                }
            }
        });
    }

    private void initData() {
        mNewFA = new T_Phr_BaseInfo();
        mFamilyAccount = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        //获取主用户的家庭档案
        if (null != mFamilyAccount) {
            if (!mFamilyAccount.getEhrID().equals(GlobalSettings.INSTANCE.getCurrentUser().getEhrId())) {
                mFamilyAccount = null;
                getMainFamilyMemberNewAsync();
            } else {
                setFamilyValue();
            }
        }
        mUserinfo = GlobalSettings.INSTANCE.getCurrentUser();
        mtvPhone.setText(mUserinfo.getMobilePhone());
        mEtNickName.setText(mUserinfo.getNickName());
        mEtRealName.setText(mUserinfo.getUserName());

        mNewUserInfo = new T_UserInfo();
        mNewUserInfo.setNickName(mUserinfo.getNickName());
        mNewUserInfo.setUserName(mUserinfo.getUserName());
        mNewUserInfo.setMobilePhone(mUserinfo.getMobilePhone());

    }


    private void setFamilyValue() {
        mTvIdCard.setText(mFamilyAccount.getIDCard());
        mTvIdCard.setTag(mFamilyAccount.getIDCard());
        mTvBirth.setText(CommonUtils.getBirthFromIDCard(mFamilyAccount.getIDCard()));
        mEtAddress.setText(mFamilyAccount.getNowAddress());

        mTvSex.setText(getResources().getTextArray(R.array.sex_type)[mFamilyAccount.getSex() - 1 < 0 ? 0 : mFamilyAccount.getSex() - 1]);
        if (mFamilyAccount.getSex() == 1) {
            mRbMale.setChecked(true);
            mSelectedSex = 1;
        } else {
            mRbFemale.setChecked(true);
            mSelectedSex = 2;
        }


        mBirthDate = mFamilyAccount.getBirthDay();

        mNewFA.setBirthDay(mFamilyAccount.getBirthDay());
        mNewFA.setIDCard(mFamilyAccount.getIDCard());
        mNewFA.setNowAddress(mFamilyAccount.getNowAddress());
        mNewFA.setName(mFamilyAccount.getName());
        mNewFA.setSelfPhone(mFamilyAccount.getSelfPhone());
        mNewFA.setSex(mFamilyAccount.getSex());
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.hos_personal_info_modify) {
            if (mIsChanged && isChanged()) {
                if (validate()) {
                    doModifyNewAsync();
                }
            } else {
                showToast("数据没有改变!");
            }
        } else if (id == R.id.hos_personal_mobile_change) {
            if (TextUtils.isEmpty(mtvPhone.getText())) {
                openActivityForResult(MobileBindFirstActiviy.class, CODE_REQUEST_PHONE_MODIFY);
            } else {
                openActivityForResult(MobileModifyActivity.class, CODE_REQUEST_PHONE_MODIFY);
            }
        }
    }

    private void doModifyNewAsync() {
//        mNewUserInfo.setNickName(mEtNickName.getText()
//                .toString());
        mNewUserInfo.setUserName(mEtRealName.getText()
                .toString());

        String idCard = (String) mTvIdCard.getTag();
        if (!TextUtils.isEmpty(idCard)) {
            try {
                mBirthDate = CommonUtils.getDateFromIDCard(idCard);
            } catch (Exception e) {
                Logger.e(TAG, "doModifyNewAsync", e);
                showToast(e.getLocalizedMessage());
                return;
            }
        }
        mNewFA.setBirthDay(mBirthDate);
        mNewFA.setSex(mSelectedSex);
        mNewFA.setIDCard(idCard);
        mNewFA.setNowAddress(mEtAddress.getText().toString());
        mNewFA.setName(mEtRealName.getText().toString());

        ApiCodeTemplate.modifyUserInfoAsync(this, TAG, mUserinfo, mNewUserInfo, mNewFA, new RequestCallback<T_UserInfo>() {
            @Override
            public void onRequestSuccess(T_UserInfo result) {
                mUserinfo.setUserName(mNewUserInfo.getUserName());
                mUserinfo.setNickName(mNewUserInfo.getNickName());

                mFamilyAccount.setBirthDay(mNewFA.getBirthDay());
                mFamilyAccount.setSex(mNewFA.getSex());
                mFamilyAccount.setIDCard(mNewFA.getIDCard());
                mFamilyAccount.setNowAddress(mNewFA.getNowAddress());
                mFamilyAccount.setName(mNewFA.getName());
                GlobalSettings.INSTANCE.setCurrentUser(mUserinfo);
                GlobalSettings.INSTANCE
                        .setCurrentFamilyAccount(mFamilyAccount);
                DBController.INSTANCE.saveUser(mUserinfo);
                DBController.INSTANCE
                        .saveFamilyAccount(mFamilyAccount);
                showToast(R.string.msg_success_modifying);
                setResult(RESULT_OK);
                if (AppManager.getInstance().getTopActivity() instanceof HosPersonalInfoActivity) {
                    openActivity(HomeActivity.class);
                }
                finish(R.anim.push_right_in, R.anim.push_right_out);
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(HosPersonalInfoActivity.this, (Exception) error.getException());
            }
        });
    }

    private boolean isChanged() {
        if (mEtRealName.getText().toString()
                .equals(mFamilyAccount.getName())
                && mTvIdCard.getTag()
                .equals(mFamilyAccount.getIDCard())
                && mSelectedSex.intValue() == mFamilyAccount.getSex()
                .intValue()
                && mEtAddress.getText().toString()
                .equals(mFamilyAccount.getNowAddress())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validate() {
       /* if (TextUtils.isEmpty(mEtNickName.getText())) {
            mEtNickName.setError(getText(R.string.msg_empty_nick_name));
        }*/
        if (TextUtils.isEmpty(mEtRealName.getText())) {
            mEtRealName.setError(getText(R.string.msg_empty_name));
            return false;
        }
        String idcard = (String) mTvIdCard.getTag();
        if (TextUtils.isEmpty(idcard)) {
            mTvIdCard.setError(getText(R.string.msg_empty_id_card));
            return false;
        } else if (validator.validate(idcard, CardValidator.CARD_ID) != Validator.SUCCESS) {
            showToast("身份证号不正确！");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_REQUEST_PHONE_MODIFY) {
            if (resultCode == RESULT_OK) {
                mUserinfo = GlobalSettings.INSTANCE.getCurrentUser();
                mtvPhone.setText(mUserinfo.getMobilePhone());
                mNewUserInfo.setMobilePhone(mUserinfo.getMobilePhone());
            }
        }
        super.onActivityResult(requestCode, requestCode, data);
    }
}
