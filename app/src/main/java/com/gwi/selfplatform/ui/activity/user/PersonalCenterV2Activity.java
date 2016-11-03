package com.gwi.selfplatform.ui.activity.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户中心
 */
public class PersonalCenterV2Activity extends HospBaseActivity {


    @Bind(R.id.personal_center_user_name)
    TextView mTvUserName;

    @OnClick(R.id.personal_center_fill_detail)
    void fillDetail() {
        openActivity(HosPersonalInfoActivity.class);
    }

    @OnClick(R.id.personal_center_modify_pwd)
    void modifyPwd() {
        openActivity(PasswordModifiyActivity.class);
    }

    @OnClick(R.id.personal_center_my_medical_card)
    void myMedicalCard() {
        openActivity(MyMedicalCardActivity.class);
    }

    @OnClick(R.id.personal_center_setting)
    void systemSetting() {
        openActivity(SystemSettingsActivity.class);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        showLogoutDialog();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvUserName.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center_v2);
        initViews();
    }

    private void showLogoutDialog() {
        final BaseDialog logoutDialog = new BaseDialog(this);
        logoutDialog.showHeader(true);
        logoutDialog.showFooter(true);
        logoutDialog.setTitle("用户注销");
        logoutDialog.setContent("你确定要注销吗？");
        logoutDialog.setTwoButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutDialog.dismiss();
            }
        }, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DemoGenerator.logout();
                GlobalSettings.INSTANCE.setCurrentUser(null);
                GlobalSettings.INSTANCE.setIsLogined(false);
                GlobalSettings.INSTANCE.setCurrentFamilyAccount(null);
                logoutDialog.dismiss();

                // 发送注销广播
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BORADCAST_LOGOUT);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                // openActivity(HomeActivity.class);
                finish();
            }
        });

        logoutDialog.show();
    }

    /**
     * 更改就診人時間觸發
     *
     * @param baseInfo
     */
    public void onEvent(T_Phr_BaseInfo baseInfo) {
        mTvUserName.setText(baseInfo.getName());
    }
}
