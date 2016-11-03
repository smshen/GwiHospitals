package com.gwi.selfplatform.ui.activity.user;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.beans.MobileVerParam;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.base.WebActivity;
import com.gwi.selfplatform.ui.dialog.AppUpdateDialog;
import com.gwi.selfplatform.ui.service.AppUpdateService;

/**
 * 设置页面
 *
 * @author 彭毅
 */
@Deprecated
public class SettingActivity extends HospBaseActivity implements OnClickListener {

    public static final String TAG = SettingActivity.class.getSimpleName();
    private View mRlUpgrade = null;
    private View mRlAppName = null;
    private TextView mTvVersionCode = null;
    private TextView mTvServiceItem = null;
    private TextView mTvFeedBack = null;
    private TextView mTvAbout = null;
    private TextView mTvNewestversionName = null;

    BroadcastReceiver mAppDownloadStatusReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        if (mAppDownloadStatusReceiver != null) {
            unregisterReceiver(mAppDownloadStatusReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setTitle("系统设置");
        mRlUpgrade = findViewById(R.id.setting_Rl_upgrade);
        mRlAppName = findViewById(R.id.setting_Rl_app_name_change);
        mTvVersionCode = (TextView) findViewById(R.id.setting_version_code);
        mTvServiceItem = (TextView) findViewById(R.id.setting_tvservice_item);
        mTvFeedBack = (TextView) findViewById(R.id.setting_tvfeed_back);
        mTvAbout = (TextView) findViewById(R.id.setting_tvabout);
        mTvNewestversionName = (TextView) findViewById(R.id.setting_tv_version_code);
        mTvVersionCode.setText(CommonUtils.getAppVersion(this));
        if (!TextUtil.isEmpty(GlobalSettings.INSTANCE.getNewestVersionName())) {
            mTvNewestversionName.setText(GlobalSettings.INSTANCE.getNewestVersionName());
        } else {
            mTvNewestversionName.setText(mTvVersionCode.getText().toString());
        }
    }

    @Override
    protected void initEvents() {
        mRlUpgrade.setOnClickListener(this);
        mTvServiceItem.setOnClickListener(this);
        mTvFeedBack.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
        mRlAppName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.setting_Rl_upgrade) {
            checkAppVersion();
        } else if (id == R.id.setting_tvservice_item) {
            openServiceItemPage();
        } else if (id == R.id.setting_tvfeed_back) {
            openActivity(FeedBackActivity.class);
        } else if (id == R.id.setting_tvabout) {
            openActivity(AboutActivity.class);
        }
    }

    private void openServiceItemPage() {
        Bundle b = new Bundle();
        b.putString(WebActivity.KEY_URL, Constants.URL_AGGREMENT);
        b.putString(WebActivity.KEY_TITLE, getString(R.string.title_aggrement));
        openActivity(WebActivity.class, b);
    }

    /**
     * 提供信息如下： 下载地址(hide) 版本号： 文件大小： 更新日志： 1.XXX 2.XXX
     */
    private void checkAppVersion() {
        ApiCodeTemplate.checkAppNewVersionAsync(this, TAG, new RequestCallback<MobileVerParam>() {
            @Override
            public void onRequestSuccess(final MobileVerParam result) {
                if (result != null) {
                    boolean isNewVerChecked = false;
                    try {
                        // 新版本检测
                        int serVer = Integer.parseInt(result.getVerCode());
                        int curVer = CommonUtils.getAppVerCode(SettingActivity.this);
                        isNewVerChecked = serVer > curVer;
                    } catch (Exception e) {
                        // 兼容老版本检测
                        isNewVerChecked = !result.getVerCode().equals(
                                CommonUtils.getAppVersion(SettingActivity.this));
                    }
                    if (isNewVerChecked) {
                        new AppUpdateDialog(SettingActivity.this)
                                .setFileName(getString(R.string.app_name))
                                .setAppVersion(result.getVerCode())
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
                                                        SettingActivity.this);
                                                mAppDownloadStatusReceiver = service.updateApp(
                                                        result.getFilePath(),
                                                        result.getVerCode());
                                                dialog.dismiss();
                                            }
                                        });
                    } else {
                        showToast(R.string.setting_version_newest);
                    }
                } else {

                }
            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null) {
                    Logger.e(TAG, "checkAppNewVersionNew#onRequestError", (Exception) error.getException());
                }
            }
        });
//        doCancellableAsyncTask(this, null, new AsyncCallback<MobileVerParam>() {
//
//            @Override
//            public MobileVerParam callAsync() throws Exception {
//                return WebServiceController.checkAppVersion();
//            }
//
//            @Override
//            public void onPostCall(final MobileVerParam result) {
//                if (result != null) {
//                    boolean isNewVerChecked = false;
//                    try {
//                        // 新版本检测
//                        int serVer = Integer.parseInt(result.getVerCode());
//                        int curVer = Integer.parseInt(CommonUtils
//                                .getAppVerCode(SettingActivity.this));
//                        isNewVerChecked = serVer > curVer;
//                    } catch (Exception e) {
//                        // 兼容老版本检测
//                        isNewVerChecked = !result.getVerCode().equals(
//                                CommonUtils.getAppVersion(SettingActivity.this));
//                    }
//                    if (isNewVerChecked) {
//                        new AppUpdateDialog(SettingActivity.this)
//                                .setFileName(getString(R.string.app_name))
//                                .setAppVersion(result.getVerCode())
//                                .setUpdateTime(result.getUpdateTime())
//                                .setVersionInfo(result.getUpdateInfo())
//                                .show(getString(R.string.dialog_cancel),
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                dialog.dismiss();
//                                            }
//                                        }, getString(R.string.dialog_cofirm),
//                                        new DialogInterface.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                AppUpdateService service = new AppUpdateService(
//                                                        SettingActivity.this);
//                                                mAppDownloadStatusReceiver = service.updateApp(
//                                                        result.getFilePath(),
//                                                        result.getVerCode());
//                                                dialog.dismiss();
//                                            }
//                                        });
//                    } else {
//                        showToast(R.string.setting_version_newest);
//                    }
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onCallFailed(Exception exception) {
//
//            }
//        });
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

}
