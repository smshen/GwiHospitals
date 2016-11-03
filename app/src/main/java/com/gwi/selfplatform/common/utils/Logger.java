package com.gwi.selfplatform.common.utils;

import android.util.Log;

import com.gwi.selfplatform.GlobalSettings;

/**
 * A wrapper class handles the Log.
 * 
 * @author pengyi
 * 
 */
public class Logger {

    public static void v(String tag, String message) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.v(tag, message);
        }

    }

    public static void e(String tag, String message) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.e(tag, message);
        }

    }
    
    public static void e(String tag, String message,Exception e) {
        if (GlobalSettings.INSTANCE.DEBUG) {
            Log.e(tag, message,e);
        }

    }
 
    
}
