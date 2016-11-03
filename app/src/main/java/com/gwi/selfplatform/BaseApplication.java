package com.gwi.selfplatform;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.Constant;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINetConfig;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.common.exception.AppException;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.DBManager;
import com.gwi.selfplatform.module.image.ImageLoaderConfig;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 初始化app数据，维护app状态，注册app广播和服务等。
 *
 * @author Peng Yi
 */
public class BaseApplication extends Application {

    static final String TAG = BaseApplication.class.getSimpleName();

    private UncaughtExceptionHandler mUncaughtExceptionHandler;
    private AppManager mAppManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "onCreate");
        init();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     *
     */
    public void init() {
        DBManager.INSTANCE.initialize(this);
        GlobalSettings.INSTANCE.init(this);
        SDKInitializer.initialize(this);
        //TODO:发布时解注
        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
        mAppManager = AppManager.getInstance();

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
        }

        if (BuildConfig.DEBUG) {
            LogUtil.init(Constant.LOGUTILS_OUT_LOGCAT, Log.VERBOSE);
        } else {
            LogUtil.init(Constant.LOGUTILS_OUT_NONE, Log.ERROR);
        }

        GWINetConfig.Builder builder = new GWINetConfig.Builder();
        String url;
        if (!"NA".equalsIgnoreCase(BuildConfig.SERVER_URL_FLAVOR)) {
            url = BuildConfig.SERVER_URL_FLAVOR;
        } else {
            url = BuildConfig.SERVICE_URL;
        }
        GWINetConfig config = builder.setBaseUrl(url).setContext(this).build();
        GWINet.init(config);

        loadDataFromMetaData(this);
    }


    private void loadDataFromMetaData(Context context) {
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            if (bundle.get("com.gwi.phr.HOSP_CODE") != null) { //单点医院模式
                String hospCode = String.valueOf(bundle.get("com.gwi.phr.HOSP_CODE"));
                //The bundle.get() will trim the "000001" to "1"
                if (hospCode != null && hospCode.length() < 6) {
                    if (TextUtils.isDigitsOnly(hospCode)) {
                        int nCode = Integer.parseInt(hospCode);
                        if(nCode==0) {
                            hospCode = String.format("%09d", nCode);
                        }else {
                            hospCode = String.format("%06d", nCode);
                        }
                    }
                }
                WebUtil.HOSP_CODE = hospCode;
                GlobalSettings.INSTANCE.setHospCode(hospCode);
                GlobalSettings.INSTANCE.setIsHospitalMode(true);

                //机构下的单点医院,HOSP_CODE != APPCODE
                String appCode = String.valueOf(bundle.get("com.gwi.phr.APP_CODE"));
                if (appCode != null && appCode.length() < 6) {
                    if (TextUtils.isDigitsOnly(appCode)) {
                        int nCode = Integer.parseInt(appCode);
                        if(nCode==0) appCode=String.format("%09d",nCode);
                        else appCode = String.format("%06d", nCode);
                    }
                }
                if (!TextUtil.isEmpty(appCode)) {
                    GlobalSettings.INSTANCE.setAppCode(appCode);
                } else {
                    GlobalSettings.INSTANCE.setAppCode(hospCode);
                }
            } else { //多机构平台模式
                if (GlobalSettings.INSTANCE.getCurrentHospital() != null) {
                    String hospCode = GlobalSettings.INSTANCE.getCurrentHospital().getHospitalCode();
                    WebUtil.HOSP_CODE = hospCode;
                    GlobalSettings.INSTANCE.setHospCode(hospCode);
                }
                String appCode = String.valueOf(bundle.get("com.gwi.phr.APP_CODE"));
                if (!TextUtil.isEmpty(appCode) && appCode.length() < 6) {
                    if (TextUtils.isDigitsOnly(appCode)) {
                        int nCode = Integer.parseInt(appCode);
                        appCode = String.format("%06d", nCode);
                    }
                }
                GlobalSettings.INSTANCE.setAppCode(appCode);
                GlobalSettings.INSTANCE.setIsHospitalMode(false);
            }
            if (bundle.get("com.gwi.phr.API_PARTNER_ID_VALUE") != null) {
                String partnerId = String.valueOf(bundle.get("com.gwi.phr.API_PARTNER_ID_VALUE"));
                String signPart = String.valueOf(bundle.get("com.gwi.phr.API_SIGN_PART_VALUE"));
                ApiCodeTemplate.PATERNER_ID = partnerId;
                ApiCodeTemplate.SIGN_PART = signPart;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * For Phr call.
     *
     * @param context context
     */
    public void init(Context context) {
        DBManager.INSTANCE.initialize(context);
        GlobalSettings.INSTANCE.init(context);
        SDKInitializer.initialize(context);
        //TODO:发布时解注
//        Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
        mAppManager = AppManager.getInstance();

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(context, Constants.BASE_IMAGE_CACHE);
        }
    }

    /**
     * 完全退出app，杀掉application进程。
     */
    public void exitApp() {
        mAppManager.AppExit(this);
    }


    /**
     *  初始化{@link com.gwi.selfplatform.common.exception.AppException},用于处理application未捕获异常。
     */
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        if (mUncaughtExceptionHandler == null) {
            mUncaughtExceptionHandler = AppException.getInstance(this);
        }
        return mUncaughtExceptionHandler;
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        this.mUncaughtExceptionHandler = handler;
    }

}
