package com.gwi.selfplatform.common.utils;

import android.os.Build;

public class Version {
    
    /**
     * >=11
     * @return
     */
    public static boolean isGeHONEYCOMB() {
        return android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB;
    }
    
    /**
     * >=16
     * @return
     */
    public static boolean isGeJELLY_BEAN() {
        return android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isGeICE_CREAM_SANDWICH_MR1() {
        return android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

}
