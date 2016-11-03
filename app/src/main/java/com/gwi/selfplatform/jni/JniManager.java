package com.gwi.selfplatform.jni;

/**
 * JNI管理类
 * @author Administrator
 *
 */
public enum JniManager {
    INSTANCE;
    
    static {
        System.loadLibrary("Phr");
    }
    
//    /**
//     * 获取DEK SEKEY
//     * @return
//     */
//    public native int getDesKey();

    public native String getSignKey();
}
