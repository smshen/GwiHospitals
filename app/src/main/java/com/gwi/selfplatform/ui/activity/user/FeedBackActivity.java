package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.validator.IValidate;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

public class FeedBackActivity extends HospBaseActivity implements OnCheckedChangeListener, IValidate {

    private static final String TAG = FeedBackActivity.class.getSimpleName();

    private TextView mTvCommit = null;

    private EditText mEtContent = null;
    private EditText mEtPhone = null;
    private RadioButton mRbSuggest = null;
    private RadioButton mRbcomplaint = null;

    private T_FeedBack_Rec mFeedBackRecord = null;

    public static final int TYPE_SUGESSTION = 1;
    public static final int TYPE_COMPLIANT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initViews();
        initEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commit,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        // initActionbar();
        setTitle("意见反馈");
        mEtContent = (EditText) findViewById(R.id.feed_back_et_content);
        mEtPhone = (EditText) findViewById(R.id.feed_back_phone);
        mRbSuggest = (RadioButton) findViewById(R.id.feed_back_rb_suggest);
        mRbcomplaint = (RadioButton) findViewById(R.id.feed_back_rb_complaint);

        mFeedBackRecord = new T_FeedBack_Rec();
        if (mRbcomplaint.isChecked()) {
            mFeedBackRecord.setType(TYPE_COMPLIANT);
        } else {
            mFeedBackRecord.setType(TYPE_SUGESSTION);
        }
        if (GlobalSettings.INSTANCE.isIsLogined()) {
            T_Phr_BaseInfo family = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
            if (!TextUtils.isEmpty(family.getName())) {
                mFeedBackRecord.setUserCode(family.getName());
            }
            if (!TextUtils.isEmpty(family.getSelfPhone())) {
                mEtPhone.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getSelfPhone());
            }
        } else {
        }
    }

    private void initActionbar() {
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setCustomView(R.layout.layout_action_bar_right);
        
        View v = getSupportActionBar().getCustomView();
        TextView title = (TextView) v.findViewById(R.id.action_bar_left);
        title.setText(R.string.menu_main_feedback);
        mTvCommit = (TextView) v.findViewById(R.id.action_bar_right);
        mTvCommit.setText(getText(R.string.btn_pwd_find_commit));*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void initEvents() {
        mRbSuggest.setOnCheckedChangeListener(this);
        mRbcomplaint.setOnCheckedChangeListener(this);
    }

    private void commitFeedBackNewAsync() {
        final int SOURCE_MOBILE = 1;
        mFeedBackRecord.setSource(SOURCE_MOBILE);//来源：手机
        mFeedBackRecord.setAdvice(mEtContent.getText().toString());
        mFeedBackRecord.setPhoneNumber(mEtPhone.getText().toString());

        ApiCodeTemplate.commitFeedBackNewAsync(this, TAG, mFeedBackRecord, new RequestCallback<Void>() {
            @Override
            public void onRequestSuccess(Void result) {

            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null && error.getException() instanceof GWIVolleyError) {
                    GWIVolleyError gwiVolleyError = (GWIVolleyError) error.getException();
                    if (gwiVolleyError.getStatus() == GResponse.SUCCESS) {
                        showToast(R.string.feed_back_success_committing);
                        finish(R.anim.push_right_in, R.anim.push_right_out);
                    } else {
                        if (gwiVolleyError.getCause() != null) {
                            showToast(gwiVolleyError.getCause().getLocalizedMessage());
                        }
                    }
                }
            }
        });
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
            case R.id.menu_commit:
                if (validate()) {
                    commitFeedBackNewAsync();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int checkedId = buttonView.getId();
            if (checkedId == R.id.feed_back_rb_suggest) {
                mFeedBackRecord.setType(TYPE_SUGESSTION);//1：建议
            } else {
                mFeedBackRecord.setType(TYPE_COMPLIANT);//2:投诉
            }
        }
    }

    @Override
    public boolean validate() {
        if (TextUtils.isEmpty(mEtContent.getText())) {
            showToast("请填写反馈内容~");
            return false;
        }else if (TextUtils.isEmpty(mEtPhone.getText())) {
            showToast("手机号不能为空~");
          return false;
        }else if (!TextUtil.matchPhone(mEtPhone.getText().toString())) {
            showToast(getString(R.string.error_mobile_format));
            return false;
        }
        return true;
    }

}
