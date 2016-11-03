package com.gwi.selfplatform.ui.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.NetworkUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author 彭毅
 * 
 */
public class DataService extends IntentService {
    private static final String TAG = DataService.class.getSimpleName();
    private static LocalBroadcastManager m_sManager;
    
    public static final Uri DATA_SYNC_URI = Uri.parse("datacontent://com.gwi.phr/data-sync");

    private static boolean mIsSyncData = true;

    public static void setLocalBroadcastmanager(LocalBroadcastManager m) {
        m_sManager = m;
    }

    public DataService() {
        super(TAG);
        if (getBaseContext() != null) {
            m_sManager = LocalBroadcastManager.getInstance(getBaseContext());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Constants.ACTION_SERVICE_DATA)) {
            computeOffilineDataAsync();
        } 
    }


    /**
     * 网络已连接或者登录后调用
     * 
     * @return
     */
    public static boolean isSyncData(Context context) {
        boolean bSyncData = false;
        if (NetworkUtil.isNetworkConnected(context)
                && GlobalSettings.INSTANCE.isIsLogined()) {
            bSyncData = true;
        }
        return bSyncData;
    }

    private void computeOffilineDataAsync() {
        // TODO:只同步当前正在使用的家庭用户的离线数据
        if (NetworkUtil.isNetworkConnected((getBaseContext()))) {
            notifyExistOffData(DBController.INSTANCE
                    .getNotUpdatedSignDataList(GlobalSettings.INSTANCE
                            .getCurrentFamilyAccount().getEhrID()));
        } else {
            return;
        }
    }

    private void notifyExistOffData(List<T_Phr_SignRec> list) {
        Logger.d(TAG, "notifyExistOffData");
        if (list == null || list.isEmpty()) {
            return;
        }
        Logger.d(TAG, "notifyExistOffData,list size: "+list.size());
        // 通知DataReceiver数据已更新
        Intent intent = new Intent();
        intent.setAction(Constants.ACTION_BROADCAST_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Constants.EXTRA_OFFLINE_DATA, (Serializable) list);
        m_sManager.sendBroadcast(intent);
    }

}
