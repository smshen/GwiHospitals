package com.gwi.selfplatform.ui.receiver;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.NetworkUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver {
    
    public static final String TAG = "NetworkStateReceiver";
    private  static final  String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static BroadcastReceiver receiver = null;


    /**
     * 注册时，初始化receiver.
     * @return
     */
    private static BroadcastReceiver getReceiver() {
        if (receiver == null) {
            receiver = new NetworkStateReceiver();
        }
        return receiver;
    }

    /**
     * 
     * @param context
     */
    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }
    
    public static void unRegisterNetworkStateReceiver(Context context) {
        if(receiver!=null) {
            context.getApplicationContext().unregisterReceiver(receiver);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "onReceive");
        if(intent.getAction().equals(ANDROID_NET_CHANGE_ACTION)) {
            if(NetworkUtil.isNetworkAvailable(context)) {
                String msg = context.getResources().getString(R.string.msg_network_connected);
                Logger.d(TAG, msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }else {
                String msg = context.getResources().getString(R.string.msg_network_disconnected);
                Logger.d(TAG,msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    
}
