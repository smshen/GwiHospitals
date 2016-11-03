package com.gwi.selfplatform.config;

import java.util.Properties;

import com.gwi.phr.hospital.R;
import android.content.Context;

/**
 * 单点医院配置
 * @author 彭毅
 *
 */
@Deprecated
public class SettingConfig {
    
    public static final String KEY_HOSPITAL = "HospCode";
    public static final String KEY_TERMINALNO = "TerminalNo";
    public static final String KEY_FAKE_LOGIN = "fake_login";
    
    private Properties mProperties;
    
    public SettingConfig(Context context) {
        mProperties = new Properties();
        try {
            mProperties.load(context.getResources().openRawResource(R.raw.setting));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getString(String key) {
        return getString(key,null);
    }
    
    public String getString(String key, String defValue) {
        return mProperties.getProperty(key, defValue);
    }
    
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key,boolean defValue){
        Boolean value = null;
        try {
        value = Boolean.valueOf(getString(key,"false"));
        }catch(Exception e) {
            value = Boolean.FALSE;
        }
        return value.booleanValue();
    }
    
    
}
