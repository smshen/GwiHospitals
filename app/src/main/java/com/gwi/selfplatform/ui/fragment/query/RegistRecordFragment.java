package com.gwi.selfplatform.ui.fragment.query;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.RecyclerItemClickListener;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TimeUtils;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1310;
import com.gwi.selfplatform.module.net.response.G1510;
import com.gwi.selfplatform.module.net.response.GRegistRecordBase;
import com.gwi.selfplatform.ui.activity.query.RegisterQueryDetailActivity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;
import com.gwi.selfplatform.ui.dialog.DateTimePickerDialog;
import com.gwi.selfplatform.ui.view.DateTimePicker;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
public class RegistRecordFragment extends HospBaseFragment {

    public static final String TAG_WEEK = "week";
    public static final String TAG_MONTH = "month";
    public static final String TAG_OTHER = "other";

    private static final String KEY_TAG = "key_tag";
    public static final String KEY_CARD_INFO = "key_card_info";
    public static final String KEY_RECORD = "key_order_regist";

    private Date mDateStart;
    private Date mDateEnd;

    private String mTagType;

    public RegistRecordFragment() {
    }

    /**
     * 当前tab页是其他月份时，需要弹出窗口选择日期
     */
    @Bind(R.id.regist_record_date_select)
    TextView mTvDateSelect;

    @Bind(R.id.regist_record_recycler_view)
    RecyclerView mRecordsRecyclerView;

    @Bind(R.id.stateful_layout)
    StatefulLayout mStatefulLayout;

    RegistRecordAdapter mAdapter;

    List<GRegistRecordBase> mRecordList;

    T_Phr_BaseInfo mCurMemer;
    ExT_Phr_CardBindRec mCardInfo;


    @OnClick(R.id.regist_record_date_select)
    void onDateSelect() {
        showMonthSelectDialog();
    }

    public static RegistRecordFragment newInstance(String tag, ExT_Phr_CardBindRec cardInfo) {
        RegistRecordFragment fragment = new RegistRecordFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_TAG, tag);
        arguments.putSerializable(KEY_CARD_INFO, cardInfo);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey(KEY_TAG)) {
            mTagType = b.getString(KEY_TAG);
            mCardInfo = (ExT_Phr_CardBindRec) b.getSerializable(KEY_CARD_INFO);
        }
        mCurMemer = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_regist_record, container, false);
        ButterKnife.bind(this, contentView);

        mRecordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecordsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mStatefulLayout.setEmptyText(R.string.no_data);

        if (TAG_OTHER.equalsIgnoreCase(mTagType)) {
            mTvDateSelect.setClickable(true);
            Drawable right = getResources().getDrawable(R.drawable.arrows_down);
            mTvDateSelect.setCompoundDrawables(null, null, right, null);
        } else if (TAG_WEEK.equalsIgnoreCase(mTagType)) {
            mTvDateSelect.setClickable(false);
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(now.getTimeInMillis() - AlarmManager.INTERVAL_DAY);
            mDateStart = getMondayOfCurWeek(now);
            mDateEnd = getSundayOfCurWeek(now);
        } else {
            mTvDateSelect.setClickable(false);
            Calendar now = Calendar.getInstance();
            mDateStart = getFirstDayOfMonth(now);
            mDateEnd = getLastDayOfMonth(now);
        }

        mRecordList = new ArrayList<>();
        mAdapter = new RegistRecordAdapter(getContext(), mRecordList);
        mRecordsRecyclerView.setAdapter(mAdapter);

        mRecordsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecordsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle b = new Bundle();
                b.putSerializable(KEY_CARD_INFO, mCardInfo);
                b.putSerializable(KEY_RECORD, mRecordList.get(position));
                //getBaseActivity().openActivity(RegisterQueryDetailActivity.class, b);
                Intent i = new Intent(getContext(), RegisterQueryDetailActivity.class);
                i.putExtras(b);
                startActivityForResult(i, 1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        if (!mTagType.equalsIgnoreCase(TAG_OTHER)) {
            loadRegistAndAppointAsync();
        }
        return contentView;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mTagType.equalsIgnoreCase(TAG_OTHER)) {
                showMonthSelectDialog();
            } else {
                loadRegistAndAppointAsync();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadRegistAndAppointAsync();
                }
            }, 700);
        }
    }

    private void showMonthSelectDialog() {
        final DateTimePickerDialog dtpDlg = new DateTimePickerDialog(getContext(), DateTimePicker.Mode.yearAndMonth);
        //设置最大值
        Calendar dateCal = Calendar.getInstance();
        dtpDlg.setCurrentDate(dateCal.getTimeInMillis());
        dateCal.add(Calendar.YEAR, 2);
        dtpDlg.setMaxDate(dateCal.getTimeInMillis());
        dtpDlg.setLeftButton(getContext().getString(R.string.dialog_cofirm), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar c = dtpDlg.getCurrentDate();
                mDateStart = getFirstDayOfMonth(c);
                mDateEnd = getLastDayOfMonth(c);
                dialog.dismiss();
                loadRegistAndAppointAsync();
            }
        });
        dtpDlg.show();
    }


    private void  loadRegistAndAppointAsync() {
        if (mRecordList == null) {
            return;
        }
        mRecordList.clear();
        mAdapter.notifyDataSetChanged();
        final String starDate = TimeUtils.getTime(mDateStart.getTime(), TimeUtils.DATE_FORMAT_DAY);
        final String endDate = TimeUtils.getTime(mDateEnd.getTime(), TimeUtils.DATE_FORMAT_DAY);
        mTvDateSelect.setText(String.format("从%s至%s", starDate, endDate));
        AsyncTasks.doAsyncTask(getActivity(), getString(R.string.dialog_content_loading), new AsyncCallback<List<GRegistRecordBase>>() {
            @Override
            public List<GRegistRecordBase> callAsync() throws Exception {
                List<G1510> appiontResults = new ArrayList<G1510>();
                try {

                    appiontResults = ApiCodeTemplate.getAppointInfo(mCardInfo, starDate, endDate);
                } catch (Exception e) {

                }
                CommonUtils.removeNull(appiontResults);
                List<G1310> registRecords = new ArrayList<G1310>();

                try {

                    registRecords = ApiCodeTemplate.getRegisertInfo(mCardInfo, starDate, endDate);
                } catch (Exception e) {
                }
                CommonUtils.removeNull(registRecords);
                List<GRegistRecordBase> results = new ArrayList<>();
                results.addAll(appiontResults);
                results.addAll(registRecords);
                if (results.isEmpty()) {
                    throw new Exception("没有挂号记录与预约记录");
                }
                Collections.sort(results, new Comparator<GRegistRecordBase>() {
                    @Override
                    public int compare(GRegistRecordBase lhs, GRegistRecordBase rhs) {
                        String lhsDateStr;
                        String rhsDateStr;
                        if (lhs instanceof G1510) {
                            lhsDateStr = ((G1510) lhs).getOrderDate();
                        } else lhsDateStr = ((G1310) lhs).getRegDate();
                        if (rhs instanceof G1310) rhsDateStr = ((G1310) rhs).getRegDate();
                        else rhsDateStr = ((G1510) rhs).getOrderDate();

                        try {
                            Date lhsDate = CommonUtils.stringPhaseDate(lhsDateStr, Constants.FORMAT_ISO_DATE);
                            Date rhsDate = CommonUtils.stringPhaseDate(rhsDateStr, Constants.FORMAT_ISO_DATE);
                            return -lhsDate.compareTo(rhsDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return 0;
                    }
                });
                return results;
            }

            @Override
            public void onPostCall(List<GRegistRecordBase> gRegistRecordBases) {
                if (gRegistRecordBases != null && !gRegistRecordBases.isEmpty()) {
                    CommonUtils.removeNull(gRegistRecordBases);
                    mRecordList.clear();
                    mRecordList.addAll(gRegistRecordBases);
                    mAdapter.notifyDataSetChanged();
                    mStatefulLayout.showContent();
                } else {
                    getBaseActivity().showToast("没有挂号记录");
                    mStatefulLayout.showEmpty();
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                if (exception != null && exception.getLocalizedMessage() != null) {
                    Logger.e("registRecordFragment", exception.getLocalizedMessage());
                }
                mStatefulLayout.showEmpty();
            }
        }, true);
    }

    private Date getSundayOfCurWeek(Calendar now) {
        now.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        now.setTimeInMillis(now.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
        return now.getTime();
    }

    private Date getMondayOfCurWeek(Calendar now) {
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

    public class RegistRecordViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_regist_record_layout)
        View layoutReistRecord;

        @Bind(R.id.item_regist_record_dept)
        TextView dept;

        @Bind(R.id.item_regist_record_indicator)
        CheckedTextView indicator;

        @Bind(R.id.item_regist_record_order_time)
        TextView orderTime;

        @Bind(R.id.item_regist_record_state)
        CheckedTextView state;

        //职称
        @Bind(R.id.item_regist_record_identity)
        TextView rankId;

        @Bind(R.id.item_regist_record_dct_name)
        TextView doctName;

        public RegistRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class RegistRecordAdapter extends RecyclerView.Adapter<RegistRecordViewHolder> {

        Context mContext;
        List<GRegistRecordBase> mRecords;
        String[] orderStatusName;

        public RegistRecordAdapter(Context context, List<GRegistRecordBase> records) {
            this.mContext = context;
            mRecords = records;
            orderStatusName = getContext().getResources().getStringArray(R.array.orderStatus);
        }

        @Override
        public RegistRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_regist_record, parent, false);
            RegistRecordViewHolder holder = new RegistRecordViewHolder(view);
            holder.layoutReistRecord.getLayoutParams().height = AbViewUtil.scaleValue(mContext, 150);
            return holder;
        }

        @Override
        public void onBindViewHolder(RegistRecordViewHolder holder, int position) {
            GRegistRecordBase data = mRecords.get(position);
            if (data instanceof G1310) {
                G1310 regData = (G1310) data;
                holder.indicator.setChecked(true);
                holder.indicator.setText("直接挂号单");
                holder.dept.setText(String.format("科室：%s", regData.getDeptName()));

                holder.orderTime.setText(String.format("就诊时间：%s", regData.getRegDate()));
                holder.doctName.setText(regData.getDoctName());
                holder.rankId.setText(regData.getRankName());
                //0为不可退号，1为可退号
                if (regData.getCanRefund() == 1) {
                    holder.state.setText("未就诊");
                } else {
                    holder.state.setText("已就诊");
                }
                holder.state.setChecked(regData.getCanRefund() == 1);
            } else {
                G1510 appointData = (G1510) data;
                holder.indicator.setChecked(false);
                holder.indicator.setText("预约挂号单");
                holder.dept.setText(String.format("科室：%s", appointData.getDeptName()));
                holder.orderTime.setText(String.format("就诊时间：%s", appointData.getOrderDate()));
                holder.rankId.setText(appointData.getRankName());
                holder.doctName.setText(appointData.getDoctName());
                int stateIndex = appointData.getOrderState() < 1 || appointData.getOrderState() > 4 ? 0 : appointData.getOrderState() - 1;
                holder.state.setText(orderStatusName[stateIndex]);
                holder.state.setChecked(stateIndex != 0);
            }
        }

        @Override
        public int getItemCount() {
            return mRecords.size();
        }
    }
}