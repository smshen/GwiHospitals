package com.gwi.selfplatform.ui.dialog;

import android.content.Context;
import android.view.View;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.view.DateTimePicker;
import com.gwi.selfplatform.ui.view.DateTimePicker.Mode;
import com.gwi.selfplatform.ui.view.DateTimePicker.OnDateTimeChangedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 显示日期和时间选择的对话框
 * 
 * @author Peng Yi
 * 
 */
public class DateTimePickerDialog extends BaseDialog {

    DateTimePicker mPicker = null;
    private Mode mCurrentMode = Mode.all;

    public DateTimePickerDialog(Context context,Mode mode) {
        super(context);
        mCurrentMode = mode; 
        initViews();
    }

    private void initViews() {
        View v = setDialogContentView(R.layout.include_datetime);
        mPicker = (DateTimePicker) v.findViewById(R.id.datetime_picker);
        mPicker.setMode(mCurrentMode);
        showHeader(true);
        showFooter(true);
        setTitle("选择日期和时间");
    }
    
    public Mode getMode() {
        return mCurrentMode;
    }
    
    public void setCurrentDate(long curDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curDate);
        switch(mCurrentMode) {
        case all:
            break;
        case date:
            mPicker.setCurrentMonth(c.get(Calendar.MONTH));
            mPicker.setCurrentYear(c.get(Calendar.YEAR));
            mPicker.setCurrentDay(c.get(Calendar.DAY_OF_MONTH));
            break;
        case time:
            break;
        case yearAndMonth:
            mPicker.setCurrentMonth(c.get(Calendar.MONTH));
            mPicker.setCurrentYear(c.get(Calendar.YEAR));
            break;
        }
    }
    
    public void setMinDate(long minDate) {
        mPicker.setMinDate(minDate);
    }
    
    public void setMaxDate(long maxDate) {
        mPicker.setMaxDate(maxDate);
    }
    
    /**
     * 返回的时间字符串根据显示的模式不同而不同。
     * @return
     */
    public String getCurrentDateTimeString() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mPicker.getYear());
        c.set(Calendar.MONTH, mPicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, mPicker.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, mPicker.getCurrentHour());
        c.set(Calendar.MINUTE, mPicker.getCurrentMinute());
        SimpleDateFormat sdf;
        switch(mCurrentMode) {
        case all:
            sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT,
                    Locale.getDefault());
            return sdf.format(c.getTime());
        case date:
            sdf = new SimpleDateFormat(Constants.DATE_FORMAT,
                    Locale.getDefault());
            return sdf.format(c.getTime());
        case yearAndMonth:
            sdf = new SimpleDateFormat(Constants.YEAR_AND_MONTH_FORMAT,
                    Locale.getDefault());
            return sdf.format(c.getTime());
        case time:
            sdf = new SimpleDateFormat(Constants.TIME_FORMAT,
                    Locale.getDefault());
            return sdf.format(c.getTime());
        }

        return null;
    }
    
    public Calendar getCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mPicker.getYear());
        c.set(Calendar.MONTH, mPicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, mPicker.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, mPicker.getCurrentHour());
        c.set(Calendar.MINUTE, mPicker.getCurrentMinute());
        return c;
    }

    public void setOnDateTimeChangeListener(OnDateTimeChangedListener l) {
        if (mPicker != null) {
            mPicker.setOnDateTimeChangeListener(l);
        }
    }

}
