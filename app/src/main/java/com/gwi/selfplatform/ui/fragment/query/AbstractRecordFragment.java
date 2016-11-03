package com.gwi.selfplatform.ui.fragment.query;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.base.HospBaseFragment;
import com.gwi.selfplatform.ui.dialog.DateTimePickerDialog;
import com.gwi.selfplatform.ui.view.DateTimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.kinst.jakub.view.StatefulLayout;

/**
 * 挂号查询内容页面(依据TAG的不同，做不同的处理)
 *
 * @version v2.0
 * @date 2015-12-15
 */
public abstract class AbstractRecordFragment extends HospBaseFragment {
    private static final String TAG = AbstractRecordFragment.class.getSimpleName();

    public static final String TAG_WEEK = "week";
    public static final String TAG_MONTH = "month";
    public static final String TAG_OTHER = "other";
    protected static final String KEY_TAG = "key_tag";

    private Date mDateStart;
    private Date mDateEnd;

    private String mTagType;

    public AbstractRecordFragment() {
    }

    /**
     * 当前tab页是其他月份时，需要弹出窗口选择日期
     */
    @Bind(R.id.regist_record_date_select)
    TextView mTvDateSelect;

    @Bind(R.id.regist_record_recycler_view)
    RecyclerView mRecordsRecyclerView;

    @Bind(R.id.regist_record_stateful_layout)
    StatefulLayout mStatefulLayout;
    TextView mEmptyTextView;

    @OnClick(R.id.regist_record_date_select)
    void onDateSelect() {

    }

//    public static AbstractRecordFragment newInstance(String tag) {
//        AbstractRecordFragment fragment = new AbstractRecordFragment();
//        Bundle arguments = new Bundle();
//        arguments.putString(KEY_TAG, tag);
//        fragment.setArguments(arguments);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey(KEY_TAG)) {
            mTagType = b.getString(KEY_TAG);
        }

        Logger.i(TAG, "onCreate mTagType= " + mTagType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_costs_record, container, false);
        ButterKnife.bind(this, contentView);

        mRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecordsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mStatefulLayout.setEmptyView(View.inflate(getContext(),R.layout.layout_empty_query_common,null));
        mEmptyTextView = (TextView) mStatefulLayout.findViewById(R.id.empty_query_text);

        Logger.i(TAG, "onCreateView mTagType= " + mTagType);
        if (TAG_OTHER.equalsIgnoreCase(mTagType)) {
            mTvDateSelect.setClickable(true);
            Drawable right = getResources().getDrawable(R.drawable.arrows_down);
            mTvDateSelect.setCompoundDrawables(null, null, right, null);

            Calendar now = Calendar.getInstance();
            mDateEnd = now.getTime();
            mDateStart = getFirstDayOfMonth(now);
        } else if (TAG_WEEK.equalsIgnoreCase(mTagType)) {
            Calendar now = Calendar.getInstance();
            mDateEnd = now.getTime();
            mDateStart = getMondayOfCurWeek(now);
        } else {
            Calendar now = Calendar.getInstance();
            mDateEnd = now.getTime();
            mDateStart = getFirstDayOfMonth(now);
        }

        mTvDateSelect.setText(CommonUtils.phareDateBetween(mDateStart, mDateEnd));

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showMonthSelectDialog() {
        final DateTimePickerDialog dtpDlg = new DateTimePickerDialog(getContext(), DateTimePicker.Mode.yearAndMonth);
        //设置最大值
        Calendar dateCal = Calendar.getInstance();
        dtpDlg.setCurrentDate(dateCal.getTimeInMillis());
        dateCal.add(Calendar.YEAR, 2);
        dtpDlg.setMaxDate(dateCal.getTimeInMillis());
        dtpDlg.setLeftButton(getContext().getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT_ISO_DATE);
                Calendar c = dtpDlg.getCurrentDate();
                mDateStart = getFirstDayOfMonth(c);
                mDateEnd = getLastDayOfMonth(c);
                mTvDateSelect.setText(CommonUtils.phareDateBetween(mDateStart, mDateEnd));
                loadingAsync(true);
                dialog.dismiss();
            }
        });
        dtpDlg.show();
    }

    private Date getMondayOfCurWeek(Calendar now) {
        //now.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        Logger.i(TAG, "dayOfWeek = " + dayOfWeek);
        if (1 == dayOfWeek) {
            now.add(Calendar.DAY_OF_MONTH, -1);
        }

        now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return now.getTime();
    }

    private Date getFirstDayOfMonth(Calendar now) {
        now.set(Calendar.DAY_OF_MONTH, 1);
        return now.getTime();
    }

    private Date getLastDayOfMonth(Calendar now) {
        now.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
        return now.getTime();
    }

    protected String getStartTime() {
        return CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, mDateStart);
    }

    protected String getEndTime() {
        return CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, mDateEnd);
    }

    public abstract void loadingAsync(boolean isReload);
}