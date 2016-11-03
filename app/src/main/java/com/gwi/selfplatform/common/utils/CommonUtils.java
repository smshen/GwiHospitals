package com.gwi.selfplatform.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.IMethodCallback;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.Validator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.DBController.DataRange;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.ui.activity.first.SplashActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.dialog.DateTimePickerDialog;
import com.gwi.selfplatform.ui.view.DateTimePicker.Mode;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class CommonUtils {
    /**
     * 获取当前日期后一周内的日期
     */
    public static List<String> parseDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        List<String> dataWeekList = new ArrayList<String>();

        String[] dateStr = new String[8];
        long DayUnit = 24 * 3600000;
        long[] weekDate = new long[8];
        for (int i = 0; i < 8; i++) {
            weekDate[i] = date.getTime() + i * DayUnit;
            Date dt = new Date(weekDate[i]);
            cd.setTime(dt);
            dateStr[i] = format.format(cd.getTime());
            String dayWeek = parseWeek(cd.get(Calendar.DAY_OF_WEEK));

            StringBuffer buf = new StringBuffer();
            buf.append(dateStr[i]);
            buf.append("（");
            buf.append(dayWeek);
            buf.append("）");
            dataWeekList.add(buf.toString());
        }
        return dataWeekList;
    }

    public static String getDate(String date) {
        int index = date.indexOf("（");
        return date.substring(0, index);
    }

    public static String getData(String data) {
        int index = data.indexOf("-");
        return data.substring(index + 1);
    }

    public static String getStartDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        String startDate = format.format(cd.getTime());
        return startDate;
    }

    public static String getEndDate() {
        Date date = new Date();
        long DayUnit = 24 * 3600000;
        long time = date.getTime() + 8 * DayUnit;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        date = new Date(time);
        cd.setTime(date);
        String endDate = format.format(cd.getTime());
        return endDate;
    }

    public static String parsebeforeWeekDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        long DayUnit = 24 * 3600000;

        long time = date.getTime() - 7 * DayUnit;
        date = new Date(time);
        cd.setTime(date);
        System.out.println(cd.getTime());
        return format.format(cd.getTime());
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

    public static String parseThreeMonDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        long DayUnit = 24 * 3600000;

        long time = date.getTime() - 3 * 30 * DayUnit;
        date = new Date(time);
        cd.setTime(date);
        System.out.println(cd.getTime());
        return format.format(cd.getTime());
    }

    public static String parseSixMonDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        long DayUnit = 24 * 3600000;

        long time = date.getTime() - 6 * 30 * DayUnit;
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
                                                    String endDate) {
        SimpleDateFormat format = new SimpleDateFormat(
                Constants.FORMAT_ISO_DATE, Locale.getDefault());
        Date start = null;
        long diff = 0;
        try {
            start = format.parse(startDate);
            diff = getDistanceDays(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        List<String> datekList = new ArrayList<String>();
        long DayUnit = 24 * 3600000;
        String dateStr = null;
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
    public static long getDistanceDays(String str1, String str2)
            throws Exception {
        DateFormat df = new SimpleDateFormat(Constants.FORMAT_ISO_DATE,
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

    public static void showNoCardDialog(Context context,
                                        final INoCardCallback callback) {
        try {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.title("绑定诊疗卡")
                    .cancelable(true)
                    .content("尚未绑定诊疗卡，现在就去绑定")
                    .positiveText(R.string.dialog_cofirm)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            callback.isBindNow(true);
                            materialDialog.dismiss();
                        }
                    }).show();
        } catch (Exception e) {
            Logger.e("showNoCardDialog",e.getLocalizedMessage());
        }
    }

    /**
     * 是否超过一个月
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean isMoreThanAMonth(String start, String end) {
        return CommonUtils.differDays(start, end, Constants.FORMAT_ISO_DATE) > 31;
    }

    /**
     * 显示日期选择对话框，用于（起始-结束）日期段选择
     *
     * @param context
     * @param btnClicked
     * @param btnStart
     * @param btnEnd
     * @param dateLimit  -1:no limit,0:min,1:max
     * @param callback
     */
    public static void showDateRangeSelectDialog(Context context,
                                                 final Button btnClicked, final Button btnStart,
                                                 final Button btnEnd, int dateLimit,
                                                 final IMethodCallback<Void> callback) {
        final DateTimePickerDialog dtpDialog = new DateTimePickerDialog(
                context, Mode.date);
        final boolean isStartBtn = btnClicked.getId() == R.id.date_select_start;
        // if (isStartBtn) {
        // // 默认是小于结束日期
        // dtpDialog.setMaxDate(((Date) btnEnd.getTag()).getTime());
        // }
        if (dateLimit == 1) {
            dtpDialog.setMaxDate(new Date().getTime());
        } else if (dateLimit == 0) {
            dtpDialog.setMinDate(new Date().getTime());
        }
        dtpDialog.setLeftButton(context.getString(R.string.dialog_cofirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar c = dtpDialog.getCurrentDate();
                        String dateString = CommonUtils.phareDateFormat(
                                Constants.FORMAT_ISO_DATE, c.getTime());
                        // 如果选择的结束日期小于开始日期，重设开始日期与结束日期相等
                        if (!isStartBtn) {
                            if (!CommonUtils.isBigger((Date) btnStart.getTag(),
                                    c.getTime())) {
                                Date d = c.getTime();
                                String beforeMonth = CommonUtils
                                        .parsebeforeMonthDate(d);
                                btnStart.setText(beforeMonth);
                                btnStart.setTag(d);
                            }
                        }
                        btnClicked.setText(dateString);
                        btnClicked.setTag(c.getTime());
                        String start = btnStart.getText().toString();
                        String end = btnEnd.getText().toString();
                        if (isStartBtn && isMoreThanAMonth(start, end)) {
                            Date d = (Date) btnStart.getTag();
                            String afterMonth = CommonUtils
                                    .parseAfterMonthDate(d);
                            btnEnd.setText(afterMonth);
                            btnEnd.setTag(d);
                        } else {
                            if (isMoreThanAMonth(start, end)) {
                                Date d = (Date) btnEnd.getTag();
                                String beforeAMonth = CommonUtils
                                        .parsebeforeMonthDate(d);
                                btnStart.setText(beforeAMonth);
                                btnStart.setTag(d);
                            }
                        }
                        callback.callback(null);
                        dtpDialog.dismiss();
                    }
                });
        dtpDialog.show();
    }

    public static void removeNull(List<?> list) {
        while (list != null && list.contains(null)) {
            list.remove(null);
        }
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
     * 获取需要的日期范围
     *
     * @param c
     * @param range
     * @return
     */
    public static Date[] getDateRange(Calendar date, DataRange range) {
        Calendar c = (Calendar) date.clone();
        Date[] ranges = new Date[2];
        if (c == null) {
            c = Calendar.getInstance();
        }
        switch (range) {
            case day:
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                ranges[0] = c.getTime();
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                ranges[1] = c.getTime();
                break;
            case week:/* 中国人一周的开始从周一算起 */
                // c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                // c.set(Calendar.HOUR_OF_DAY, 0);
                // c.set(Calendar.MINUTE, 0);
                // c.set(Calendar.MILLISECOND, 0);
                // ranges[0] = c.getTime();
                // final long ADAY = 24*3600000;
                // c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                // c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
                // c.set(Calendar.HOUR_OF_DAY,
                // c.getActualMaximum(Calendar.HOUR_OF_DAY));
                // c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                // c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                // ranges[1] = c.getTime();
                // fix:跨年、跨月
                final long ADAY = 24 * 3600000;
                int d = c.get(Calendar.DAY_OF_WEEK);
                // fix:月初，回溯到上个月
                long time = c.getTimeInMillis() - (d - 2 < 0 ? 6 : d - 2) * ADAY;
                c.setTimeInMillis(time);
                c.setTimeInMillis(time);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                ranges[0] = c.getTime();
                c.setTimeInMillis(time + 6 * ADAY);
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                ranges[1] = c.getTime();
                break;
            case month:
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                ranges[0] = c.getTime();
                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMaximum(Calendar.DAY_OF_MONTH));
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                ranges[1] = c.getTime();
                break;
            case season:
                // TODO:
                break;
            case year:
                c.set(Calendar.MONTH, c.getActualMinimum(Calendar.MONTH));
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.MILLISECOND, 0);
                ranges[0] = c.getTime();
                c.set(Calendar.MONTH, c.getActualMaximum(Calendar.MONTH));
                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMaximum(Calendar.DAY_OF_MONTH));
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMaximum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
                ranges[1] = c.getTime();
                break;
        }
        return ranges;
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

    /**
     * @param date
     * @param calendarType {@link Calendar.HOUR}, {@link Calendar.HOUR_OF_DAY},
     *                     {@link Calendar.DAY_OF_MONTH}, {@link Calendar.MONTH},
     *                     {@link Calendar.YEAR}
     * @return
     */
    public static void setActualMinimumDate(Calendar c, int calendarType) {
        switch (calendarType) {
            case Calendar.HOUR:
                c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
                break;
            case Calendar.HOUR_OF_DAY:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMinimum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
                break;
            case Calendar.DAY_OF_MONTH:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMinimum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));

                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMinimum(Calendar.DAY_OF_MONTH));
                break;
            case Calendar.MONTH:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMinimum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));

                c.set(Calendar.DAY_OF_MONTH,
                        c.getActualMinimum(Calendar.DAY_OF_MONTH));
                c.set(Calendar.MONTH, c.getActualMinimum(Calendar.MONTH));
                break;
            case Calendar.YEAR:
                c.set(Calendar.HOUR_OF_DAY,
                        c.getActualMinimum(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
                c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
                break;
        }
    }

    /**
     * 把西方的周期算法转化为中国的周期算法:星期一、星期二...星期日
     *
     * @param c
     * @param index (1...7)
     * @return
     */
    public static void setDateByWeekIndex(Calendar c, int index) {
        if (index == 7) {
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        } else {
            c.set(Calendar.DAY_OF_WEEK, index + 1);
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

    public static String getContentFromFile(String path) {
        FileInputStream fis = null;
        ByteArrayBuffer bb = new ByteArrayBuffer(500);
        try {
            fis = new FileInputStream(path);
            int line;
            while ((line = fis.read()) != -1) {
                bb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.e("getStringFromFile", e.getLocalizedMessage());
                }
            }
        }
        return EncodingUtils.getString(bb.toByteArray(), "utf-8");
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
    public static Date getDateFromIDCard(String idCard) throws Exception {
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
        return stringPhaseDate(tempStr.toString(), Constants.FORMAT_ISO_DATE);
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
            int age = c.get(Calendar.YEAR) - Integer.parseInt(sb.toString());
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

    /**
     * 显示错误提示信息
     *
     * @param activity 所属的activity
     * @param e        抛出的异常
     */
    public static void showError(HospBaseActivity activity, Exception e) {
        if (activity == null || e == null) {
            return;
        }
        Logger.e(activity.getLocalClassName(), "showError", e);
        if (e instanceof ServerError) {
            ServerError serverError = (ServerError) e;
            activity.showToast(activity.getString(R.string.msg_service_interval_eror) + "（" + serverError.networkResponse.statusCode + "）");
        } else if (e instanceof GWIVolleyError) {
            VolleyError volleyError = (VolleyError) e;
            if (e.getCause() != null) {
//                activity.showToast(e.getCause().getLocalizedMessage());
                Toast.makeText(activity, e.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                activity.showToast(R.string.msg_service_disconnected);
            }
        } else if (e instanceof VolleyError) {
//            VolleyError volleyError = (VolleyError) e;
//            if (e.getCause() != null) {
//                activity.showToast(e.getCause().getLocalizedMessage());
//            } else {
//                activity.showToast(R.string.msg_service_disconnected);
//            }
            activity.showToast(R.string.msg_service_disconnected);
        } else {
            if (e.getLocalizedMessage() != null) {
                activity.showToast(e.getLocalizedMessage());
            } else {
                activity.showToast(R.string.msg_service_disconnected);
            }
        }
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    /**
     * 根据参数表中的数据过滤挂号和预约挂号科室列表
     *
     * @param target
     * @return
     */
    public static List<G1211> filterRegistDeptsByLimits(List<G1211> target, G1011 patientInfo) {
        List<G1211> result = new ArrayList<>();
        List<String> mDeptsLimit = new ArrayList<>();
        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
        
        boolean isPeds = false;

        if (patientInfo.getBirthday() != null) {
            try {
                Date birth = CommonUtils.stringPhaseDate(patientInfo.getBirthday(), Constants.FORMAT_ISO_DATE);
                String birthLimit = HospitalParams.getValue(params, HospitalParams.CODE_PEDS_AGE_LIMIT);
                final int DEFAULT_AGE_LIMIT = 14;
                isPeds = new Date().getYear() - birth.getYear() < (birthLimit == null ? DEFAULT_AGE_LIMIT : Integer.parseInt(birthLimit));
                if (!isPeds) {
                    //如果是成人,添加儿科限制
                    mDeptsLimit.addAll(HospitalParams.getFields(params.get(HospitalParams.CODE_PEDS_ID)));
                }
            } catch (ParseException e) {
                Logger.e("filterRegistDeptsByLimits", e.getLocalizedMessage());
            }
        }

        if (patientInfo.getSex() != null) {
            Integer sex = Integer.parseInt(patientInfo.getSex());
            final int male = 1;
            if (sex == male) {
                mDeptsLimit.addAll(HospitalParams.getFields(params.get(HospitalParams.CODE_MALE_PATIENT_LIMITED_DEPTS)));
            } else {
                mDeptsLimit.addAll(HospitalParams.getFields(params.get(HospitalParams.CODE_FEMALE_PATIENT_LIMITED_DEPTS)));
            }
        }


        for (int targetIndex = 0, size = target.size(); targetIndex < size; targetIndex++) {
            G1211 dept = target.get(targetIndex);
            //过滤掉受限科室
           if (!mDeptsLimit.contains(dept.getDeptID())) {
                //添加过滤后的科室
                result.add(dept);
            }
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static  void restartApp(Context context) {
        Logger.d("test","restartApp");
        if (context != null) {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    public static String getFormatFee(double f) {
        return String.format("%.2f%s", f, "元");
    }

    public static String phareDateBetween(Date startDate, Date endDate) {
        String start = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, startDate);
        String end = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, endDate);
        return "从" + start + "至" + end;
    }

//    public static void gotoAlipayFakeActivity(Context context, String money) {
//        Bundle b = new Bundle();
//        b.putString(Constants.KEY_BUNDLE, money);
//        ((HospBaseActivity) context).openActivity(AlipayFakeActivity.class, b);
//    }

    /**
     * 设置输入框文本是否密文
     *
     * @param view      输入框框架
     * @param isVisible true明文 false密文
     */
    public static void setInputType(EditText view, boolean isVisible) {
        if (isVisible) {
            // 文本正常显示
            view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // 文本以密码形式显示
            view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        // 使光标始终在最后位置
        Editable etable = view.getText();
        Selection.setSelection(etable, etable.length());
    }
}
