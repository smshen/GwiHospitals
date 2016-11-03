package com.gwi.selfplatform.common.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gwi.selfplatform.ui.activity.first.SplashActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 处理未捕获异常，提高application的友好度
 * 
 * @author Peng Yi
 *
 */
public class AppException implements UncaughtExceptionHandler {

    public static final String TAG = "AppCrashHandler";
    private static AppException sInstance;
    private Context mContext;
    private UncaughtExceptionHandler mHanExceptionHandler;

    private AppException(Context context) {
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mHanExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static AppException getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppException(context);
        }
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mHanExceptionHandler != null) {
            mHanExceptionHandler.uncaughtException(thread, ex);
        } else {
            killProcess();
        }

    }

    /**
     * 收集和处理异常
     * 
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        MobclickAgent.reportError(mContext, ex);
        ex.printStackTrace();
//        StackTraceElement[] arr = ex.getStackTrace();
//        final StringBuffer report = new StringBuffer(ex.toString());
//        final String lineSeperator = "\n\n-------------------------------\n\n";
//        report.append("\n\n--------- Stack trace ---------\n\n");
//        for (int i = 0; i < arr.length; i++) {
//            report.append("    ");
//            report.append(arr[i].toString());
//            report.append("\n|");
//        }
//        report.append(lineSeperator);
//        // If the exception was thrown in a background thread inside
//        // AsyncTask, then the actual exception can be found with getCause
//        report.append("\n\n--------- Cause ---------\n\n");
//        Throwable cause = ex.getCause();
//        if (cause != null) {
//            report.append(cause.toString());
//            report.append("||");
//            arr = cause.getStackTrace();
//            for (int i = 0; i < arr.length; i++) {
//                report.append("    ");
//                report.append(arr[i].toString());
//                report.append("\n|");
//            }
//        }
//        Logger.e(TAG, report.toString());
        int requestCode = 0;
        PendingIntent pIntent = PendingIntent.getActivity(mContext,
                requestCode, new Intent(mContext, SplashActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        //设置系统报错后，2s后重启
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pIntent);
        System.exit(0);

        // new Thread() {
        //
        // @Override
        // public void run() {
        // Looper.prepare();
        // new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false)
        // .setMessage("程序崩溃了...").setNeutralButton("我知道了", new
        // OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // System.exit(0);
        // }
        // })
        // .create().show();
        // Looper.loop();
        // }
        //
        // }.start();
        return true;
    }

    private void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
