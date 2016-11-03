package com.gwi.selfplatform.ui.service;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.WindowManager;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;

import java.io.File;

public class AppUpdateService {
    
    private static final String TAG = AppUpdateService.class.getSimpleName();
    
    private BaseDialog mAppDownloadCancelDialog = null;
    
    Context mContext;
    
    public AppUpdateService(Context context) {
        mContext = context;
    }
    
//    @SuppressLint("NewApi")
//    public BroadcastReceiver downloadApp(String url,String name,boolean isUpdate) {
//        final DownloadManager manager = (DownloadManager) mContext
//                .getSystemService(Context.DOWNLOAD_SERVICE);
//        Logger.d(TAG, "downloadApp#" + url);
//        final Uri uri = Uri.parse(url);
//        final long refrenceId = manager.enqueue(new Request(uri)
//        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
//        .setAllowedOverMetered(true)
////                .setAllowedOverRoaming(false)
//                .setTitle(isUpdate?mContext.getText(R.string.app_name):name+"下载程序")
//                .setMimeType("application/vnd.android.package-archive")
//                .setDescription("下载中")
//                .setDestinationInExternalPublicDir(
//                        Environment.DIRECTORY_DOWNLOADS,
//                        uri.getLastPathSegment())
//                .setNotificationVisibility(
//                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
//        
//        BroadcastReceiver receiver;
//
//        receiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Logger.d(TAG, "downloadReceiver#" + intent.getAction());
//                if (intent.getAction().equalsIgnoreCase(
//                        DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//                        || DownloadManager.ACTION_NOTIFICATION_CLICKED
//                                .equals(intent.getAction())) {
//                    queryDownloadStatus(context);
//                }
//            }
//
//            void queryDownloadStatus(Context context) {
//                Query downloadQuery = new Query();
//                // 设置之前入队的id
//                downloadQuery.setFilterById(refrenceId);
//                Cursor cursor = manager.query(downloadQuery);
//                if (cursor.moveToFirst()) {
//                    int columnIndex = cursor
//                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
//                    int status = cursor.getInt(columnIndex);
//                    String statusText = "";
//
//                    switch (status) {
//                    case DownloadManager.STATUS_FAILED:
//                        statusText = "STATUS_FAILED";
//                        manager.remove(refrenceId);
//                        break;
//                    case DownloadManager.STATUS_SUCCESSFUL:
//                        statusText = "STATUS_SUCCESSFUL";
//                        installApk(Environment
//                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                                + "/" + uri.getLastPathSegment());
//                        break;
//                    default:
//                        statusText = "STATUS_OTHER";
//                        showCancelDialog();
//                    }
////                    Toast.makeText(mContext, "status" + statusText,
////                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            void showCancelDialog() {
//                if(mAppDownloadCancelDialog!=null) {
//                    mAppDownloadCancelDialog.dismiss();
//                    mAppDownloadCancelDialog = null;
//                }
//                
//                if(mAppDownloadCancelDialog==null) {
//                    mAppDownloadCancelDialog = new BaseDialog(mContext.getApplicationContext());
//                }
//                mAppDownloadCancelDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                mAppDownloadCancelDialog.showFooter(true);
//                mAppDownloadCancelDialog.showHeader(true);
//                mAppDownloadCancelDialog.setContent("取消程序更新？");
//                mAppDownloadCancelDialog.setTwoButton(mContext.getString(R.string.dialog_cancel),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                    int which) {
//                                dialog.dismiss();
//                            }
//                        }, mContext.getString(R.string.dialog_cofirm),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                    int which) {
//                                manager.remove(refrenceId);
//                                dialog.dismiss();
//                            }
//                        });
//                mAppDownloadCancelDialog.show();
//            }
//
//            void installApk(String filePath) {
//                try {
//                    Logger.d(TAG, "installApk#" + filePath);
//                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
//                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    installIntent.setDataAndType(
//                            Uri.fromFile(new File(filePath)),
//                            "application/vnd.android.package-archive");
//                    mContext.startActivity(installIntent);
//                } catch (Exception e) {
//                    Logger.e(TAG, e.getLocalizedMessage() + "");
//                }
//            }
//
//        };
//
//        IntentFilter completeFilter = new IntentFilter(
//                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        mContext.registerReceiver(receiver, completeFilter);
//
//        IntentFilter clickedfilter = new IntentFilter();
//        clickedfilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
//        mContext.registerReceiver(receiver, clickedfilter);
//        
//        return receiver;
//    }
    
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BroadcastReceiver updateApp(String url,String versionCode) {
        final DownloadManager manager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Logger.d(TAG, "updateApp#" + url);
        final Uri uri = Uri.parse(url);
        final long refrenceId = manager.enqueue(new Request(uri)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI)
//                .setAllowedOverRoaming(false)
                .setTitle(mContext.getText(R.string.app_name))
                .setMimeType("application/vnd.android.package-archive")
                .setDescription(versionCode)
                .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                                uri.getLastPathSegment())
                .setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
        BroadcastReceiver receiver;

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Logger.d(TAG, "downloadReceiver#" + intent.getAction());
                if (intent.getAction().equalsIgnoreCase(
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        || DownloadManager.ACTION_NOTIFICATION_CLICKED
                                .equals(intent.getAction())) {
                    queryDownloadStatus(context);
                }
            }

            void queryDownloadStatus(Context context) {
                Query downloadQuery = new Query();
                // 设置之前入队的id
                downloadQuery.setFilterById(refrenceId);
                Cursor cursor = manager.query(downloadQuery);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);
                    String statusText = "";

                    switch (status) {
                    case DownloadManager.STATUS_FAILED:
                        statusText = "STATUS_FAILED";
                        manager.remove(refrenceId);
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        statusText = "STATUS_SUCCESSFUL";
                        installApk(Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                + "/" + uri.getLastPathSegment());
                        break;
                    default:
                        statusText = "STATUS_OTHER";
                        showCancelDialog();
                    }
                }
            }

            void showCancelDialog() {
                if(mAppDownloadCancelDialog!=null) {
                    mAppDownloadCancelDialog.dismiss();
                    mAppDownloadCancelDialog = null;
                }
                
                if(mAppDownloadCancelDialog==null) {
                    mAppDownloadCancelDialog = new BaseDialog(mContext.getApplicationContext());
                }
                mAppDownloadCancelDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mAppDownloadCancelDialog.showFooter(true);
                mAppDownloadCancelDialog.showHeader(true);
                mAppDownloadCancelDialog.setContent("取消程序更新？");
                mAppDownloadCancelDialog.setTwoButton(mContext.getString(R.string.dialog_cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();
                            }
                        }, mContext.getString(R.string.dialog_cofirm),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                manager.remove(refrenceId);
                                dialog.dismiss();
                            }
                        });
                mAppDownloadCancelDialog.show();
            }

            void installApk(String filePath) {
                try {
                    Logger.d(TAG, "installApk#" + filePath);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setDataAndType(
                            Uri.fromFile(new File(filePath)),
                            "application/vnd.android.package-archive");
                    mContext.startActivity(installIntent);
                } catch (Exception e) {
                    Logger.e(TAG, e.getLocalizedMessage() + "");
                }
            }

        };

        IntentFilter completeFilter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(receiver, completeFilter);

        IntentFilter clickedfilter = new IntentFilter();
        clickedfilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mContext.registerReceiver(receiver, clickedfilter);
        
        return receiver;
    }

    
}
