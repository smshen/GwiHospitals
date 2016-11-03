package com.gwi.ccly.android.commonlibrary.common.utils;

import android.os.Environment;
import android.util.Log;

import com.gwi.ccly.android.commonlibrary.common.Constant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kuofei Liu on 2015-11-30.
 */
public class LogUtil {
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 3;// sd卡中日志文件的最多保存天数
    private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), Log.WARN);
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), Log.ERROR);
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), Log.DEBUG);
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), Log.INFO);
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), Log.VERBOSE);
    }

    public static void w(String tag, String text) {
        log(tag, text, Log.WARN);
    }

    public static void e(String tag, String text) {
        log(tag, text, Log.ERROR);
    }

    public static void d(String tag, String text) {
        log(tag, text, Log.DEBUG);
    }

    public static void i(String tag, String text) {
        log(tag, text, Log.INFO);
    }

    public static void v(String tag, String text) {
        log(tag, text, Log.VERBOSE);
    }
    
    private static int logutils_out_type = Constant.LOGUTILS_OUT_LOGCAT;
    private static int logutils_out_level = Log.VERBOSE;

    /**
     *
     * @param type {@code LOGUTILS_OUT_LOGCAT,LOGUTILS_OUT_NONE,LOGUTILS_OUT_FILE,LOGUTILS_OUT_ALL}
     * @param level
     */
    public static void init(int type,int level) {
        LogUtil.logutils_out_type = type;
        logutils_out_level = level;
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, int level) {
        if (tag==null||msg==null)
            return;

        if (logutils_out_type!= Constant.LOGUTILS_OUT_NONE) {
            if ((Log.ERROR == level) &&
                    (Log.ERROR >= logutils_out_level)) { // 输出错误信息
                Log.e(tag, msg);
            } else if ((Log.WARN == level) &&
                    (Log.WARN >=logutils_out_level)) {
                Log.w(tag, msg);
            } else if ((Log.DEBUG == level) &&
                    (Log.DEBUG >= logutils_out_level)) {
                Log.d(tag, msg);
            } else if ((Log.INFO == level) &&
                    (Log.INFO >= logutils_out_level)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }

            //是否打印到SDCard
            if ((logutils_out_type== Constant.LOGUTILS_OUT_FILE)
               ||(logutils_out_type== Constant.LOGUTILS_OUT_ALL))
                writeLogtoFile(String.valueOf(level), tag, msg);
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     * **/
    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String strDatetime = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "    " + tag + "    " + text;

     //   File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel+ MYLOGFILEName);

        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory()
                    .getCanonicalFile() + "/" +strDatetime+ MYLOGFILEName);
        } catch (IOException e) {
        }

        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     * */
    public static void delFile() {// 删除日志文件
        String strDatetime = logfile.format(getDateBefore());
      //  File file = new File(MYLOG_PATH_SDCARD_DIR, needDelFiel + MYLOGFILEName);
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory()
                    .getCanonicalFile() + "/" + MYLOGFILEName+strDatetime);
        } catch (IOException e) {
        }

        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     * */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
}
