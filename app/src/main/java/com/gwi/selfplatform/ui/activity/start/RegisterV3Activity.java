package com.gwi.selfplatform.ui.activity.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.start.RegisterCodeFragment;
import com.gwi.selfplatform.ui.fragment.start.RegisterTelphoneFragment;

public class RegisterV3Activity extends HospBaseActivity {
    public static final String TAG = RegisterV3Activity.class.getSimpleName();
    public static final String TELPHONE_FRAGMENT = "RegisterTelphoneFragment";
    public static final String CODE_FRAGMENT = "RegisterCodeFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_v3);
        initViews();
        initEvents();
        getThirdpartData();

        if (null == savedInstanceState) {
            gotoTelphoneFragment();
        }
    }

    @Override
    protected void initViews() {
        setTitle("用户注册");
    }

    @Override
    protected void initEvents() {

    }

    private String mTpSource;
    private String mTpName;
    private String mTpMobile;
    private String mTpIDCardNo;

    /**
     * 获取第三方登陆数据
     */
    private void getThirdpartData() {
        Intent intent = getIntent();
        if (null != intent) {
            mTpSource = intent.getStringExtra(Constants.ThirdPart.SOURCE);
            mTpName = intent.getStringExtra(Constants.ThirdPart.USER_NAME);
            mTpMobile = intent.getStringExtra(Constants.ThirdPart.MOBILE_NO);
            mTpIDCardNo = intent.getStringExtra(Constants.ThirdPart.ID_CARD_NO);
            String toast = "SOURCE=" + mTpSource + "\nUSER_NAME=" + mTpName + "\nMOBILE_NO=" + mTpMobile + "\nID_CARD_NO=" + mTpIDCardNo;
            Log.i(TAG, toast);
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            }
        }
    }

//    /**
//     * 是否第三方登陆
//     *
//     * @return
//     */
//    private boolean isFromThirdpart() {
//        return (null != mTpMobile) ? true : false;
//    }

    private void gotoTelphoneFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TELPHONE_FRAGMENT);
        if (null == fragment) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.id_content, RegisterTelphoneFragment.newInstance(mTpSource, mTpMobile), TELPHONE_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoCodeFragment(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("PhoneNumber", phoneNumber);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TELPHONE_FRAGMENT);
        if (null != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out,
                    R.anim.push_right_in, R.anim.push_right_out);
            transaction.hide(fragment);
            transaction.add(R.id.id_content, Fragment.instantiate(this, RegisterCodeFragment.class.getName(), bundle), CODE_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (null != mTpSource && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AppManager.getInstance().AppExit(this);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else super.onBackPressed();
    }
}
