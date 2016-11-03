package com.gwi.ccly.android.commonlibrary.common;

import android.util.Log;

/**
 * Created by Kuofei Liu on 2015-12-1.
 */
public class Constant {
    //日志输出级别
    public  static final int LOGUTILS_OUT_NONE=0;      //不输出日志
    public  static final int LOGUTILS_OUT_LOGCAT=1;    //输出日志到系统，同普通的logcat
    public  static final int LOGUTILS_OUT_FILE=2;     //输出到文件
    public  static final int LOGUTILS_OUT_ALL=3;     //同时输出到系统和文件

    public static final int RESPONSE_STATUS_JAVA_EXCEPTION = -1;  //分析返回数据时，JAVA异常
    public static final int RESPONSE_STATUS_SUCCESS = 1;
    public static final int RESPONSE_STATUS_FAILURE = 2;

    public static final int VOLLEY_REQUEST_RET_TYPE_BOOLEAN=0; //请求返还没有值，只有成功或失败，TRUE:成功   FALSE:失败
    public static final int VOLLEY_REQUEST_RET_TYPE_ITEM=1; //请求返还单个ITEM值
    public static final int VOLLEY_REQUEST_RET_TYPE_ARRAY=2; //请求返还一个数组
    public static final int VOLLEY_REQUEST_RET_TYPE_STRING=3; //请求返还字符串

}
