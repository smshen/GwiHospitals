package com.gwi.selfplatform.module.pay.boc;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Created by hying on 14-1-21.
 */
public class SSLCustomSocketFactory extends SSLSocketFactory {
    private static final String TAG = "SSLCustomSocketFactory";

    private static final String KEY_PASS = "111111";
    private static final String KEY_KEYSTORE = "pfx";
    
    public SSLCustomSocketFactory(KeyStore trustStore) throws Throwable {
        super(trustStore);
    }

    public static SSLSocketFactory getSocketFactory(Context context) {
    	InputStream ins = null ;
        try {
            ins = context.getAssets().open("uat3.pfx");
            KeyStore trustStore;
            if(KEY_KEYSTORE != null){
            	 trustStore = KeyStore.getInstance("PKCS12");
            }else{
            	 trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            }
            trustStore.load(ins, KEY_PASS.toCharArray());
            SSLSocketFactory factory = new SSLCustomSocketFactory(trustStore);
            return factory;
        } catch (Throwable e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }finally {
            try {
            	if(ins != null){
            		ins.close();
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
    }
}
