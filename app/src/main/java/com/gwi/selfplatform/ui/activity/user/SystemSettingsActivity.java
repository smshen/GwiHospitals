package com.gwi.selfplatform.ui.activity.user;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.beans.MobileVerParam;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.base.WebActivity;
import com.gwi.selfplatform.ui.dialog.AppUpdateDialog;
import com.gwi.selfplatform.ui.service.AppUpdateService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class SystemSettingsActivity extends HospBaseActivity {
    public static final String TAG = SystemSettingsActivity.class.getSimpleName();

    private BroadcastReceiver mAppDownloadStatusReceiver = null;

    @Bind(R.id.txt_online_upgrade)
    TextView mTxtOnlineUpgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        ButterKnife.bind(this);
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

    @OnClick({R.id.ly_online_upgrade, R.id.ly_service_items, R.id.ly_feedback, R.id.ly_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_online_upgrade:
                checkAppVersion();
                break;
            case R.id.ly_service_items:
                openServiceItemPage();
                break;
            case R.id.ly_feedback:
                openActivity(FeedBackActivity.class);
                break;
            case R.id.ly_about:
                openActivity(AboutActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        setTitle("系统设置");
        mTxtOnlineUpgrade.setText(String.format("在线升级（V%s）", CommonUtils.getAppVersion(this)));
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
                        int curVer = CommonUtils.getAppVerCode(SystemSettingsActivity.this);
                        isNewVerChecked = serVer > curVer;
                    } catch (Exception e) {
                        // 兼容老版本检测
                        isNewVerChecked = !result.getVerCode().equals(
                                CommonUtils.getAppVersion(SystemSettingsActivity.this));
                    }
                    if (isNewVerChecked) {
                        new AppUpdateDialog(SystemSettingsActivity.this)
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
                                                        SystemSettingsActivity.this);
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
//                                .getAppVerCode(SystemSettingsActivity.this));
//                        isNewVerChecked = serVer > curVer;
//                    } catch (Exception e) {
//                        // 兼容老版本检测
//                        isNewVerChecked = !result.getVerCode().equals(
//                                CommonUtils.getAppVersion(SystemSettingsActivity.this));
//                    }
//                    if (isNewVerChecked) {
//                        new AppUpdateDialog(SystemSettingsActivity.this)
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
//                                                        SystemSettingsActivity.this);
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
