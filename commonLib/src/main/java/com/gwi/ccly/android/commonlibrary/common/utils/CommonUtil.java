package com.gwi.ccly.android.commonlibrary.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import com.gwi.ccly.android.commonlibrary.common.utils.validator.CardValidator;
import com.gwi.ccly.android.commonlibrary.common.utils.validator.Validator;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class CommonUtil {

    public static String phareDateFormat( Date date,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(date);

    }

    public static String parsebeforeMonthDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        long DayUnit = 24 * 3600000;

        long time = date.getTime() - 30 * DayUnit;
        date = new Date(time);
        cd.setTime(date);
        System.out.println(cd.getTime());
        return format.format(cd.getTime());
    }

    public static String parseAfterMonthDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        long DayUnit = 24 * 3600000;

        long time = date.getTime() + 30 * DayUnit;
        date = new Date(time);
        cd.setTime(date);
        System.out.println(cd.getTime());
        return format.format(cd.getTime());
    }

    /**
     * 获取含小数点后两位的数据
     */

    public static String getDoubleData(String data) {
        return String.format(Locale.getDefault(), "%.2f", Double.valueOf(data));
    }

    /**
     * 金额保留小数点后两位
     *
     * @param cashStr
     * @return
     */
    public static String formatCash(double cash) {
        return String.format(Locale.getDefault(), "%.2f", cash);
    }

    /**
     * 金额保留小数点后两位
     *
     * @param cashStr
     * @return
     */
    public static String formatCash(double cash, String unit) {
        return String.format(Locale.getDefault(), "%.2f" + unit, cash);
    }

    /**
     * 例如，format = "%.2f".
     *
     * @param cash
     * @param format
     * @return
     */
    public static String formatNumber(Object cash, String format) {
        return String.format(Locale.getDefault(), format, cash);
    }

    /**
     * <b>55.23</b>元
     *
     * @param cashString
     * @return
     */
    public static SpannableStringBuilder cashText(String cashString) {
        SpannableStringBuilder builder = new SpannableStringBuilder(cashString);
        builder.setSpan(new ForegroundColorSpan(Color.RED), 0,
                cashString.length() - 1,
                SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder cashGreenText(String cashString,String splitStr) {
        SpannableStringBuilder builder = new SpannableStringBuilder(cashString);
        int index = cashString.indexOf(splitStr);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#467f1e")), index+1,
                cashString.length() - 2,
                SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }


    public static void removeNull(List<?> list) {
        while (list != null && list.contains(null)) {
            list.remove(null);
        }
    }

    public static int scaletByResolution(Context context, int standard) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        return (int) (standard * dm.density * 4 / 3);

    }

    /**
     * 比较两个日期是否是同年同月，如果是，返回true；否则返回false.
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean dateCompareTo(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }
        return false;
    }

    public static Date stringPhaseDate(String dateString, String format)
            throws ParseException {
        if (TextUtils.isEmpty(dateString)) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.parse(dateString);
    }


    /**
     * @param date
     * @param calendarType {@link Calendar.HOUR}, {@link Calendar.HOUR_OF_DAY},
     *                     {@link Calendar.DAY_OF_MONTH}, {@link Calendar.MONTH},
     *                     {@link Calendar.YEAR}
     * @return
     */
    public static void setActualMaximumDate(Calendar c, int calendarType) {
        switch (calendarType) {
            case Calendar.HOUR:
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                break;
            case Calendar.HOUR_OF_DAY:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                break;
            case Calendar.DAY_OF_MONTH:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));

                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case Calendar.MONTH:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));

                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMaximum(Calendar.DAY_OF_MONTH));
                c.set(Calendar.MONTH, c.getActualMaximum(Calendar.MONTH));
                break;
            case Calendar.YEAR:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                break;
        }
    }


    public static String toDotNetDateTimeString(long milliseconds) {
        String prefix = "/Date(";
        String suffix = ")/";
        String timeZone = "+0800";
        return prefix + milliseconds + timeZone + suffix;
    }

    public static String fromDotNetDateTimeString(String dotNetDateTime) {
        int start = 6;
        int end = dotNetDateTime.length() - 7;
        return dotNetDateTime.substring(start, end);
    }

    public static String getAppVersion(Context context) {
        String code;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            code = info.versionName;
        } catch (NameNotFoundException e) {
            code = "1.0.0";
        }
        return code;
    }

    public static int getAppVerCode(Context context) {
        int code;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            code = info.versionCode;
        } catch (NameNotFoundException e) {
            code = 1;
        }
        return code;
    }

    public static Properties getProperties(String path) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(path));
        } catch (Exception e) {
            return null;
        }
        return p;
    }

    public static Long differDays(String start, String end, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date startD = sdf.parse(start);
            Date endD = sdf.parse(end);
            long time1 = startD.getTime();
            long time2 = endD.getTime();
            return Math.abs(((time2 - time1) / (1000 * 3600 * 24)));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * end 是否大于start
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean isBigger(Date start, Date end) {
        return start.after(end);
    }

    /**
     * 从身份证中获取生日
     *
     * @param idCard
     * @return
     * @throws Exception
     */
    public static Date getDateFromIDCard(String idCard,String format) throws Exception {
        StringBuffer tempStr = null;
        if (idCard.trim().length() == 15) {
            tempStr = new StringBuffer();
            tempStr = new StringBuffer(idCard.substring(6, 12));
            tempStr.insert(4, '-');
            tempStr.insert(2, '-');
            tempStr.insert(0, "19");
        } else if (idCard.trim().length() == 18) {
            tempStr = new StringBuffer(idCard.substring(6, 14));
            tempStr.insert(6, '-');
            tempStr.insert(4, '-');
        }
        return stringPhaseDate(tempStr.toString(), format);
    }

    /**
     * 从身份证中获取性别信息
     *
     * @param idCard
     * @return 2:female 1:male
     */
    public static int getSexFromIdCard(String idCard) throws Exception {
        char c;
        if (idCard.length() == 15) {
            c = idCard.charAt(14);
        } else {
            c = idCard.charAt(16);
        }
        return (((int) c % 2) == 0) ? 2 : 1;
    }

    /**
     * 从身份证中获取生日(只支持18位)
     *
     * @param idCard
     * @return
     */
    public static String getBirthFromIDCard(String idCard) {
        if (idCard.length() == 18) {
            StringBuffer sb = new StringBuffer(idCard.substring(6, 14));
            sb.insert(6, "月");
            sb.insert(4, "年");
            sb.append("日");
            return sb.toString();
        } else return null;
    }

    public static boolean validateIDCard(String idCard) {
        CardValidator validator = new CardValidator();
        return validator.validate(idCard, CardValidator.CARD_ID)== Validator.SUCCESS;
    }

    public static String getAgeFromIDCard(String idCard) {
        if (idCard.length() == 18) {
            StringBuffer sb = new StringBuffer(idCard.substring(6, 10));
            Calendar c = Calendar.getInstance();
            int age = c.get(Calendar.YEAR) - Integer.valueOf(sb.toString());
            return String.valueOf(age);
        } else return null;
    }

    /**
     * （8:00,9:00）=>（8:00 - 9:00）
     *
     * @param startTime
     * @param endTime   can be null.
     * @return
     */
    public static String formatTimePoint(String startTime, String endTime) {
        //邵阳市第一人民医院 1970-12-12 12:12
        if (startTime.contains(" ")) {
            String startTimeFix = startTime.split(" ")[1];
            if (endTime != null) {
                String endTimeFix = endTime.split(" ")[1];
                return startTimeFix + " - " + endTimeFix;
            } else {
                return startTimeFix;
            }
        } else {//12:00
            if (endTime != null) {
                return startTime + " - " + endTime;
            } else {
                return startTime;
            }
        }
    }

//    *
//     * 显示错误提示信息
//     *
//     * @param activity 所属的activity
//     * @param e        抛出的异常
//
//    public static void showError(BaseActivity activity, Exception e) {
//        if (activity == null || e == null) {
//            return;
//        }
//        LogUtil.e(activity.getLocalClassName(), e);
//        if (e instanceof ServerError) {
//            ServerError serverError = (ServerError) e;
//            activity.showToast(activity.getString(R.string.msg_service_interval_eror) + "（" + serverError.networkResponse.statusCode + "）");
//        } else if (e instanceof GWIVolleyError) {
//            VolleyError volleyError = (VolleyError) e;
//            if (e.getCause() != null) {
////                activity.showToast(e.getCause().getLocalizedMessage());
//                Toast.makeText(activity, e.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            } else {
//                activity.showToast(R.string.msg_service_disconnected);
//            }
//        } else if (e instanceof VolleyError) {
////            VolleyError volleyError = (VolleyError) e;
////            if (e.getCause() != null) {
////                activity.showToast(e.getCause().getLocalizedMessage());
////            } else {
////                activity.showToast(R.string.msg_service_disconnected);
////            }
//            activity.showToast(R.string.msg_service_disconnected);
//        } else {
//            if (e.getLocalizedMessage() != null) {
//                activity.showToast(e.getLocalizedMessage());
//            } else {
//                activity.showToast(R.string.msg_service_disconnected);
//            }
//        }
//    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static  void restartApp(Context context,Class<?> clz) {
        if (context != null) {
            Intent intent = new Intent(context, clz);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public static String getFormatFee(double f) {
        return String.format("%.2f%s", f, "元");
    }
}
