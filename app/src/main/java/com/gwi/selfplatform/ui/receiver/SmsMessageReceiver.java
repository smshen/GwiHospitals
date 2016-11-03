package com.gwi.selfplatform.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.Constants;

/**
 * 短信监听广播
 * @author 彭毅
 *
 */
public class SmsMessageReceiver extends BroadcastReceiver {
    
    private static final String TAG  = SmsMessageReceiver.class.getSimpleName();
    
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    
    private static final String FLAG_PDUS = "pdus";
    
    private static SmsMessageReceiver receiver = null;
    
    /**
     * 注册时，初始化receiver.
     * @return
     */
    private static BroadcastReceiver getReceiver() {
        if (receiver == null) {
            receiver = new SmsMessageReceiver();
        }
        return receiver;
    }
    
    public static void registerSmsMessageReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        context.registerReceiver(getReceiver(), filter);
    }
    
    public static void unregisterSmsMessageReceiver(Context context) {
        context.unregisterReceiver(getReceiver());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, intent.getAction());
        if(ACTION.equalsIgnoreCase(intent.getAction())) {
            StringBuffer smsAddress = new StringBuffer();
            StringBuffer smsContent = new StringBuffer();
            Bundle b = intent.getExtras();
            try {
                if(b!=null) {
                    Object[] pdusObjects = (Object[]) b.get(FLAG_PDUS);
                    SmsMessage[] messages = new SmsMessage[pdusObjects.length];
                    for(int i=0;i<pdusObjects.length;i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObjects[i]);
                    }
                    for(SmsMessage msg:messages) {
                        smsAddress.append(msg.getDisplayOriginatingAddress());
                        smsContent.append(msg.getDisplayMessageBody());
                    }
                    Logger.d(TAG, "Sms Message from: "+smsAddress.toString()+", message is "+smsContent.toString());
                    //删除非数字
                    String[] splited = smsContent.toString().split("[^0-9]");
                    Intent smsIntent = new Intent(Constants.ACTION_BOROADCAST_VALIDATION_CODE);
                    Bundle extra = new Bundle();
                    Logger.d(TAG, splited[splited.length-1]);
                    extra.putString(Constants.EXTRA_VALIDATION_CODE, splited[splited.length-1]);
                    smsIntent.putExtras(extra);
                    context.sendBroadcast(smsIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
