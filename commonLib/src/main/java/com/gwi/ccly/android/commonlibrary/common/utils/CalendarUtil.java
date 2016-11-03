package com.gwi.ccly.android.commonlibrary.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * CalendarUtil
 *
 */
public class CalendarUtil {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_HH = new SimpleDateFormat("yyyy-MM-dd HH");
    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat DATE_FORMAT_HH_MM_SS = new SimpleDateFormat("HH:mm:ss");

    private CalendarUtil() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * string time to string
     *
     * @param dateString
     * @param dateFormat
     * @return
     */
    public static String getTime(String dateString, SimpleDateFormat dateFormat) {
        // String dateString = "2012-12-06 ";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);
            return getTime(date.getTime(), dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";

        }
        return null;
    }

    /**
     * 获取给定两个日期的所有日期字符串(包括这两个日期)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDateListFromRange(String startDate,
                                                    String endDate,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(
                formatStr, Locale.getDefault());
        Date start = null;
        long diff = 0;
        try {
            start = format.parse(startDate);
            diff = getDistanceDays(startDate, endDate,formatStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        List<String> datekList = new ArrayList<String>();
        long DayUnit = 24 * 3600000;
        String dateStr;
        for (long current = start.getTime(), i = 0; i <= diff; current += DayUnit, i++) {
            calendar.setTime(new Date(current));
            dateStr = format.format(calendar.getTime());
            datekList.add(dateStr);
        }
        return datekList;
    }

    /**
     * 获取日期相差天数
     *
     * @param str1
     * @param str2
     * @return
     * @throws Exception
     */
    public static long getDistanceDays(String str1, String str2,String format)
            throws Exception {
        DateFormat df = new SimpleDateFormat(format,
                Locale.getDefault());
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 是否超过一个月
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean isMoreThanAMonth(String start, String end,String format) {
        return CommonUtil.differDays(start, end, format) > 31;
    }

    /**
     * dates为null时，返回当前的时间；当dates设置了5个参数，可以返回：1970-12-24 13:12;设置3个，返回1970-12-24
     *
     * @param format 日期格式，比如（yyyy-MM-dd HH:mm）
     * @param dates
     * @return 参数错误返回null
     */
    public static String phareDateFormat(String format, int[] dates) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        if (dates == null || dates.length == 0) {
            return sdf.format(new Date());
        } else if (dates.length == 5) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, dates[0]);
            c.set(Calendar.MONTH, dates[1]);
            c.set(Calendar.DAY_OF_MONTH, dates[2]);
            c.set(Calendar.HOUR_OF_DAY, dates[3]);
            c.set(Calendar.MINUTE, dates[4]);
            return sdf.format(c.getTime());
        } else if (dates.length == 3) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, dates[0]);
            c.set(Calendar.MONTH, dates[1]);
            c.set(Calendar.DAY_OF_MONTH, dates[2]);
            return sdf.format(c.getTime());
        } else if (dates.length == 2) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, dates[0]);
            c.set(Calendar.MONTH, dates[1]);
            return sdf.format(c.getTime());
        }
        return null;
    }

    public static String phareDateFormat(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(date);

    }
}
