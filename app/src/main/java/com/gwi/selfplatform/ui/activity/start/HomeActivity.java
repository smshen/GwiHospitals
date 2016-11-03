package com.gwi.selfplatform.ui.activity.start;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.BaseApplication;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.beans.MobileVerParam;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.activity.user.PersonalCenterV2Activity;
import com.gwi.selfplatform.ui.adapter.MainModuleV2Adapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.dialog.AppUpdateDialog;
import com.gwi.selfplatform.ui.service.AppUpdateService;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

//import com.gwi.selfplatform.ui.activity.expand.OnlinePharmacyActivity;
//import com.gwi.selfplatform.ui.activity.pay.MedicalCardChargeActivity;
//import com.gwi.selfplatform.ui.activity.query.HospitalIntroductionV2Activity;
//import com.gwi.selfplatform.ui.activity.query.PriceQueryV2Activity;
//import com.gwi.selfplatform.ui.baike.HealthBaikeActivity;

public class HomeActivity extends HospBaseActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private static final int MSG_CHANGE_PHOTO = 1;
    /**
     * 图片自动切换时间
     */
    private static final int PHOTO_CHANGE_TIME = 3000;
    private static final int REQUEST_CODE_LOGIN = 2;

    /**
     * 计数，连续两次按退出键
     */
    private int mOnBackPressedCnt = 0;
    /**
     * 退出APP的消息
     */
    public static final int MSG_EXIT_APP = 0x10001;
    /**
     * 重置两次按backpress的间隔
     */
    public static final int EXIT_INTERNAL = 1500;

    BroadcastReceiver mAppDownloadStatusReceiver = null;

    @Bind(R.id.grid_view)
    GridView gridView;

    @Bind(R.id.txt_welcome)
    TextView txtWelcome;

    @Bind(R.id.iv_welcome)
    ImageView mIvWelcome;

    @Bind(R.id.home_welcome_header)
    View mWeclcomeLayout;

    @OnClick(R.id.txt_welcome)
    void onClick() {
        if (GlobalSettings.INSTANCE.isIsLogined()) {
            openActivityForResult(PersonalCenterV2Activity.class, REQUEST_CODE_LOGIN);
        } else {
            openActivityForResult(LoginV2Activity.class, REQUEST_CODE_LOGIN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        addChangeFamilyButton();
        initData();
        initViews();
        initEvents();
        checkAppNewVersionNew();

        //Baidu Push 服务
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                HospitalParams.getValue(GlobalSettings.INSTANCE.getHospitalParams(), HospitalParams.CODE_BAIDU_AK));
        // 开启基于地理位置推送
        PushManager.enableLbs(getApplicationContext());

//        showUpgradingImportantNotificationDialog();
        EventBus.getDefault().register(this);
    }

    private void checkAppNewVersionNew() {
        ApiCodeTemplate.checkAppNewVersionAsync(null, TAG, new RequestCallback<MobileVerParam>() {
            @Override
            public void onRequestSuccess(final MobileVerParam result) {
                if (result != null) {
                    boolean isNewVerChecked;
                    try {
                        // 新版本检测
                        int serVer = Integer.parseInt(result.getVerCode());
                        int curVer = CommonUtils.getAppVerCode(HomeActivity.this);
                        isNewVerChecked = serVer > curVer;
                    } catch (Exception e) {
                        // 兼容老版本检测
                        isNewVerChecked = !result.getVerCode().equals(
                                CommonUtils.getAppVersion(HomeActivity.this));
                    }
                    if (isNewVerChecked) {
                        GlobalSettings.INSTANCE.setNewestVersionName(result.getVerName());
                        new AppUpdateDialog(HomeActivity.this)
                                .setFileName(getString(R.string.app_name))
                                .setAppVersion(result.getVerName())
                                .setUpdateTime(result.getUpdateTime())
                                .setVersionInfo(result.getUpdateInfo())
                                .show(getString(R.string.dialog_cancel),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                            }
                                        }, getString(R.string.dialog_cofirm),
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                AppUpdateService service = new AppUpdateService(
                                                        HomeActivity.this);
                                                mAppDownloadStatusReceiver = service.updateApp(
                                                        result.getFilePath(),
                                                        result.getVerName() == null ? result
                                                                .getVerCode()
                                                                : result.getVerName());
                                                dialog.dismiss();
                                            }
                                        });
                    }
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null) {
                    Logger.e(TAG, "checkAppNewVersionNew#onRequestError", (Exception) error.getException());
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        setWelcomeTip();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        ActivityCompat.invalidateOptioonsMenu(this);
//        setTitle(GlobalSettings.INSTANCE.getHospitalName());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onDestroy() {
        if (mAppDownloadStatusReceiver != null) {
            unregisterReceiver(mAppDownloadStatusReceiver);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        mHandler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_CHANGE_PHOTO:
                   /* int index = mAdViewPager.getCurrentItem();
                    if (index == mImageUrls.length - 1) {
                        index = -1;
                    }
                    mAdViewPager.setCurrentItem(index + 1);*/
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
                                PHOTO_CHANGE_TIME);
                        break;
                    case MSG_EXIT_APP:
                        if (mOnBackPressedCnt < 2) {
                            mOnBackPressedCnt = 0;
                        }
                        break;
                }
            }

        };

    }

    @Deprecated
    private void showUpgradingImportantNotificationDialog() {
        if (!GlobalSettings.INSTANCE.isFirstEnterApp()) {
            return;
        }
        int curVer = CommonUtils
                .getAppVerCode(HomeActivity.this);
        if (curVer == 1) {
            return;
        }
        BaseDialog dlg = new BaseDialog(this);
        dlg.setTitle(getText(R.string.dialog_title_prompt));
        dlg.setContent("因为系统升级的缘故，老用户需要重新进行注册登录，给您们带来的不便，敬请谅解！");
        dlg.showHeader(true);
        dlg.showFooter(true);
        dlg.setCanceledOnTouchOutside(false);
        dlg.setLeftButton(getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                GlobalSettings.INSTANCE.firstEnterApp();
            }
        });
        dlg.show();
    }

    @Override
    protected void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
//            getSupportActionBar().setDisplayUseLogoEnabled(false);
//            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        initWelcome();
        initGridView();
    }

    private void initWelcome() {

        int welcomeHeight = AbViewUtil.scaleValue(this, 340);
        mWeclcomeLayout.getLayoutParams().height = welcomeHeight;

//        Drawable drawable = getResources().getDrawable(R.drawable.main_name_bj);
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int height = AbViewUtil.scaleValue(this, 65);
//        int width = AbViewUtil.scaleValue(this, 160);
//        Drawable sacled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
//        txtWelcome.setBackgroundDrawable(sacled);
        mIvWelcome.getLayoutParams().height = height;
        mIvWelcome.getLayoutParams().width = AbViewUtil.scaleValue(this, 76);
        txtWelcome.getLayoutParams().height = height;
    }

    private void setWelcomeTip() {
        if (GlobalSettings.INSTANCE.isIsLogined()) {
            txtWelcome.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getName() + getString(R.string.label_welcome));
        } else {
            txtWelcome.setText(R.string.label_login);
        }
    }

    private void initGridView() {
        gridView.setAdapter(new MainModuleV2Adapter(this));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (GlobalSettings.INSTANCE.isIsLogined()) {
//            MenuItem userMenu = menu.findItem(R.id.menu_main_user);
//            userMenu.setTitle(GlobalSettings.INSTANCE.getCurrentFamilyAccount()
//                    .getName());
//        } else {
//            MenuItem userMenu = menu.findItem(R.id.menu_main_user);
//            userMenu.setTitle(R.string.login);
//        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //调整menu显示顺序
        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_self_home, menu);
//        MenuItem item = menu.findItem(R.id.menu_main_setting);
//        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.family) {
            if (GlobalSettings.INSTANCE.isIsLogined()) {
                // 切換就診人
                openActivity(MyMedicalCardActivity.class);
            } else {
                openActivityForResult(LoginV2Activity.class, REQUEST_CODE_LOGIN);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == Constants.REQUEST_CODE_LOGIN) {
//                txtWelcome.setText(String.format("%s%s", GlobalSettings.INSTANCE.getCurrentUser().getUserName(), getString(R.string.label_welcome)));
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 监听手机按键事件
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "dispatchKeyEvent");
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (isTaskRoot()) {
                    mOnBackPressedCnt++;
                    Logger.d(TAG, "count: " + mOnBackPressedCnt);
                    if (mOnBackPressedCnt < 2) {
                        showToast("再按一次返回键退出程序");
                        mHandler.sendEmptyMessageDelayed(MSG_EXIT_APP,
                                EXIT_INTERNAL);
                        return true;
                    } else {
                        // 退出所有的activity
                        ((BaseApplication) getApplication()).exitApp();
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    public void onEvent(T_Phr_BaseInfo baseInfo) {
        txtWelcome.setText(String.format("%s%s", baseInfo.getName(), getString(R.string.label_welcome)));
    }

    public void toLogin() {
        openActivityForResult(LoginV2Activity.class, REQUEST_CODE_LOGIN);
//        overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
    }
}
