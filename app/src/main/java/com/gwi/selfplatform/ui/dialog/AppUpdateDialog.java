package com.gwi.selfplatform.ui.dialog;

import java.util.Date;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

public class AppUpdateDialog extends BaseDialog {

    private TextView mTvFileName = null;
    private TextView mTvappVersion = null;
    private TextView mTvUpdateTime = null;
    private TextView mTvVersionInfo = null;

    public AppUpdateDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        setDialogContentView(R.layout.layout__dialog_app_update);
        showHeader(true);
        showFooter(true);
        setTitle(R.string.app_update_title);
        mTvFileName = (TextView) findViewById(R.id.app_update_file_name);
        mTvappVersion = (TextView) findViewById(R.id.app_update_version_code);
        mTvUpdateTime = (TextView) findViewById(R.id.app_update_time);
        mTvVersionInfo = (TextView) findViewById(R.id.app_update_info);
    }

    public AppUpdateDialog setFileName(String name) {
        mTvFileName.setText(String.format(
                getContext().getString(R.string.app_update_file_name), name));
        return this;
    }

    public AppUpdateDialog setAppVersion(String version) {
        mTvappVersion
                .setText(String.format(
                        getContext().getString(R.string.app_update_version),
                        version));
        return this;
    }

    public AppUpdateDialog setUpdateTime(Date time) {
        mTvUpdateTime.setText(String.format(
                getContext().getString(R.string.app_update_time),
                CommonUtils.phareDateFormat(Constants.DATE_FORMAT, time)));
        return this;
    }

    public AppUpdateDialog setVersionInfo(String info) {
        mTvVersionInfo.setText(String.format(
                getContext().getString(R.string.app_update_info),
                info));
        return this;
    }

    public void show(String leftText,
            DialogInterface.OnClickListener leftListener, String rightText,
            DialogInterface.OnClickListener rightListener) {
        setTwoButton(leftText, leftListener, rightText, rightListener);
        show();
    }

}
