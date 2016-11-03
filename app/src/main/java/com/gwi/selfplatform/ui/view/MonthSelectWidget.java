package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.dialog.DateTimePickerDialog;
import com.gwi.selfplatform.ui.view.DateTimePicker.Mode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 月份选择控件
 * @author 彭毅
 *
 */
public class MonthSelectWidget extends LinearLayout implements OnClickListener{
    
    ImageButton mIBMore;
    /** The first of date from left to right */
    Button mBtnDate1;
    Button mBtnDate2;
    Button mBtnDate3;
    Button mBtnDate4;
    /** The last */
    Button mBtnDate5;
    String mBtnMoreMaxDate;
    
    TextView mCurrentMonthTitle;
    TextView mCurrentMonth;
    
    private Date mMaxDate;
    
    private OnMonthSelectListener mMonthSelectListener;
    
    private static final String YEAR_AND_MONTH_FORMAT = "yyyy年\nMM月";

    public void setMonthSelectListener(OnMonthSelectListener l) {
        mMonthSelectListener = l;
    }

    public MonthSelectWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MonthSelectWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthSelectWidget(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        //<merge /> required.
        View content = LayoutInflater.from(getContext()).inflate(R.layout.layout_month_select,this,true);
        mIBMore = (ImageButton) content.findViewById(R.id.month_select_more);
        mBtnDate1 = (Button) content.findViewById(R.id.month_select_1);
        mBtnDate2 = (Button) content.findViewById(R.id.month_select_2);
        mBtnDate3 = (Button) content.findViewById(R.id.month_select_3);
        mBtnDate4 = (Button) content.findViewById(R.id.month_select_4);
        mBtnDate5 = (Button) content.findViewById(R.id.month_select_5);
        
        mCurrentMonthTitle = (TextView) content.findViewById(R.id.month_select_current_label);
        mCurrentMonth = (TextView) content.findViewById(R.id.month_select_current);
        
        mIBMore.setOnClickListener(this);
        mBtnDate1.setOnClickListener(this);
        mBtnDate2.setOnClickListener(this);
        mBtnDate3.setOnClickListener(this);
        mBtnDate4.setOnClickListener(this);
        mBtnDate5.setOnClickListener(this);
        //默认当前日期
        refreshWidget(null);
    }
    
    private void refreshWidget(Date maxDate) {
        List<DateStrHolder> dateList = new ArrayList<DateStrHolder>();
        List<String> dateStrList = getBeforeMonthString(dateList,maxDate);
        
        mBtnDate1.setText(dateStrList.get(4));
        mBtnDate2.setText(dateStrList.get(3));
        mBtnDate3.setText(dateStrList.get(2));
        mBtnDate4.setText(dateStrList.get(1));
        mBtnDate5.setText(dateStrList.get(0));
        
        mBtnDate1.setTag(dateList.get(4));
        mBtnDate2.setTag(dateList.get(3));
        mBtnDate3.setTag(dateList.get(2));
        mBtnDate4.setTag(dateList.get(1));
        mBtnDate5.setTag(dateList.get(0));
        
        //more button 显示的最大时间
        long dayUnit = 24 * 3600000;
        Date d;
        try {
            d = CommonUtils.stringPhaseDate(dateStrList.get(4), YEAR_AND_MONTH_FORMAT);
            long time = d.getTime() - dayUnit;
            mBtnMoreMaxDate = CommonUtils.phareDateFormat(Constants.YEAR_AND_MONTH_FORMAT, new Date(time));
        } catch (ParseException e) {
            mBtnMoreMaxDate = transferDateFormat(dateStrList.get(4));
        }
        
        updateBottomIndicator(transferDateFormat(dateStrList.get(0)),dateList.get(0));
    }
    
    /**
     * 更新底部显示的当前选择日期：“当前查询月份：2014年12月”
     * @param dateStr
     * @param holder
     */
    private void updateBottomIndicator(String dateStr,DateStrHolder holder) {
        mCurrentMonth.setText(dateStr);
        mCurrentMonth.setTag(holder);
    }
    
    /**
     * yyyy年\nMM月 ==>yyyy年MM月
     * @param dateStr
     * @return
     */
    private String transferDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_AND_MONTH_FORMAT);
        Date d;
        try {
            d = CommonUtils.stringPhaseDate(dateStr, YEAR_AND_MONTH_FORMAT);
        } catch (ParseException e) {
            d = new Date();
        }
        sdf.applyPattern(Constants.YEAR_AND_MONTH_FORMAT);
        dateStr = sdf.format(d);
        return dateStr;
    }
    
    public void setMaxDate(Date d) {
        mMaxDate = d;
        refreshWidget(d);
    }
    
    public void setCurrentDate(Date d) {
        if(d.before(mMaxDate)) {
            String dateStr = CommonUtils.phareDateFormat(Constants.YEAR_AND_MONTH_FORMAT, d);
            updateBottomIndicator(dateStr, getAMonthRange(d, new SimpleDateFormat(Constants.FORMAT_ISO_DATE)));
        }
    }
    
    /**
     * 获取当前选择月份的起始和结束时间
     * @return {start,end}
     */
    public String[] getCurrentSelectDate() {
        DateStrHolder holder = (DateStrHolder) mCurrentMonth.getTag();
        return new String[]{holder.start,holder.end};
    }
    
    /**
     * date Str:"yyyy年\nMM月","start,end:yyyy-MM-dd"
     * 获取前5个月的月份string(包括当前月)
     * @return
     */
    public List<String> getBeforeMonthString(List<DateStrHolder> dateList,Date maxDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(YEAR_AND_MONTH_FORMAT);
        SimpleDateFormat isoSdf = new SimpleDateFormat(Constants.FORMAT_ISO_DATE);
        Calendar c = Calendar.getInstance();
        if(maxDate!=null) {
            c.setTime(maxDate);
        }
        long dayUnit = 24 * 3600000;
        List<String> result = new ArrayList<String>();
        result.add(sdf.format(c.getTime()));
        dateList.add(getAMonthRange(c.getTime(), isoSdf));
        //得到月底最后一天和第一天的时间
        for(int i=0;i<4;i++) {
            int firsDayOfMonth = c.getMinimum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DAY_OF_MONTH, firsDayOfMonth);
            long milliseconds = c.getTimeInMillis();
            milliseconds -=dayUnit;
            c.setTimeInMillis(milliseconds);
            result.add(sdf.format(c.getTime()));
            dateList.add(getAMonthRange(c.getTime(), isoSdf));
        }
        return result;
    }
    
    /**
     * 获取一个月的起始和结束日期字符串:yyyy-MM-dd(supported)
     * @param d
     * @param sdf
     * @return
     */
    public DateStrHolder getAMonthRange(Date d,SimpleDateFormat sdf) {
        DateStrHolder holder = new DateStrHolder();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        holder.start = sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        holder.end = sdf.format(c.getTime());
        return holder;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.month_select_more) {
            showMoreMonthSelectDialog();
        }else {
            if(mMonthSelectListener!=null) {
                DateStrHolder holder;
                holder = (DateStrHolder) v.getTag();
                String dateStr = ((Button)v).getText().toString();
                mCurrentMonth.setText(transferDateFormat(dateStr));
                mMonthSelectListener.onMonthSelect(holder.start, holder.end);
            }
        }
    }
    
    /**
     * 显示更多日期:选择之前的日期
     */
    private void showMoreMonthSelectDialog() {
        final DateTimePickerDialog dtpDlg = new DateTimePickerDialog(getContext(), Mode.yearAndMonth);
        try {
            //设置最大值
            dtpDlg.setCurrentDate(CommonUtils.stringPhaseDate(mCurrentMonth.getText().toString(), Constants.YEAR_AND_MONTH_FORMAT).getTime());
            dtpDlg.setMaxDate(CommonUtils.stringPhaseDate(mBtnMoreMaxDate, Constants.YEAR_AND_MONTH_FORMAT).getTime());
            dtpDlg.setLeftButton(getContext().getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_ISO_DATE);
                    Calendar c = dtpDlg.getCurrentDate();
                    DateStrHolder holder = getAMonthRange(c.getTime(), sdf);
                    updateBottomIndicator(dtpDlg.getCurrentDateTimeString(),holder);
                    mMonthSelectListener.onMonthSelect(holder.start, holder.end);
                    dialog.dismiss();
                }
            });
            dtpDlg.show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    class DateStrHolder {
        String start;
        String end;
    }
    
    public interface OnMonthSelectListener {
        /**
         * 
         * @param start 开始日期
         * @param end 结束日期
         */
        void onMonthSelect(String start, String end);
    }
    
}
