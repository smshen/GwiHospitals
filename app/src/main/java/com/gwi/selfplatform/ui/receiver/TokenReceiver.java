package com.gwi.selfplatform.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GAuth;

/**
 * 获取token的Receiver
 * @author 彭毅
 * @date 2015/6/5.
 */
public class TokenReceiver extends BroadcastReceiver {

    public static final String ACTION_REQUEST = "com.gwi.phr.TOKEN_REQUEST";
    public static final String ACTION_SIGNED = "com.gwi.phr.TOKEN_SIGNED";
    public static final String KEY_FLAG = "key_flag";
    public static final int VALUE_FLAG_SUCESS = 1;
    public static final int VALUE_FLAG_FAILED = 2;
    public static final String KEY_ERROR_MSG="key_error_msg";


    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (ACTION_REQUEST.equalsIgnoreCase(intent.getAction())) {
            ApiCodeTemplate.authenticateAsync(null, ACTION_REQUEST, null, new RequestCallback<GAuth>() {
                @Override
                public void onRequestSuccess(GAuth auth) {
                    GlobalSettings.INSTANCE.setToken(auth.getToken());

                    Intent signedIntent = new Intent(ACTION_SIGNED);
                    signedIntent.putExtra(KEY_FLAG,VALUE_FLAG_SUCESS);
                    context.sendBroadcast(signedIntent);

                }

                @Override
                public void onRequestError(RequestError error) {
                    Intent signedIntent = new Intent(ACTION_SIGNED);
                    signedIntent.putExtra(KEY_FLAG, VALUE_FLAG_FAILED);
                    if (error != null && error.getException() != null) {
                        Exception e = (Exception) error.getException();
                        if (e.getCause() != null) {
                            signedIntent.putExtra(KEY_ERROR_MSG, e.getCause().toString());
                        } else {
                            signedIntent.putExtra(KEY_ERROR_MSG, "获取token失败，请稍后再试...");
                        }
                    } else {
                        signedIntent.putExtra(KEY_ERROR_MSG, "获取token失败，请稍后再试...");
                    }

                    context.sendBroadcast(signedIntent);
                }
            });
        }
    }
}
