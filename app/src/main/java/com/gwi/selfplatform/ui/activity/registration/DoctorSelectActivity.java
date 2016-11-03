package com.gwi.selfplatform.ui.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.utils.CommonUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1412;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 医生选择页面
 *
 * @version v2.0
 * @date 2015-12-10
 */
public class DoctorSelectActivity extends HospBaseActivity {

    private static final String TAG = DoctorSelectActivity.class.getSimpleName();
    @Bind(R.id.dct_select_date_select)
    TabLayout mTlDateSelectLayout;

    @Bind(R.id.dct_select_list)
    ExpandableListView mElvDctSelectView;

    @Bind(R.id.dct_select_order_date)
    TextView mTvOrderDate;

    @Bind(R.id.text_empty)
    TextView mTvEmptyText;

    @Bind(R.id.loading_indicator)
    View mLoadingView;

    DoctorSelectExpandAdapter mAdapter;

    Date mOrderDate;

    private String mPostionOfOrderDate;

    private boolean mFromIntentIsTypeRegist = true;
    private String mFromIntentOrderDateStr;
    private String mFromIntentTypeID;
    private G1211 mFromIntentDept;

    /**
     * 已分组的医生号源列表。用于一次性给所有号源，需要APP进行日期筛选的情况(比如，佛五)
     */
    private List<List<G1417>> mGroupedDoctList;

    @Override
    protected void initViews() {
        mPostionOfOrderDate = HospitalParams.getDateSelectPosition();
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            mFromIntentIsTypeRegist = b.getBoolean(Constants.KEY_IS_TYPE_REGIST, true);
            mFromIntentDept = (G1211) b.getSerializable(Constants.KEY_DEPT);
            if (mFromIntentDept == null) {
                Logger.e(TAG, "dept is null");
                CommonUtils.restartApp(this);
                finish();
            }
            if (b.containsKey(Constants.KEY_ORDER_DATE)) {
                mFromIntentOrderDateStr = b.getString(Constants.KEY_ORDER_DATE);
            }
            if (b.containsKey(Constants.KEY_TYPE_ID)) {
                mFromIntentTypeID = b.getString(Constants.KEY_TYPE_ID);
            } else {
                mFromIntentTypeID = Constants.DEFAULT_TYPE_ID;
            }
        }

        //TODO:DEMO节省时间
        mTlDateSelectLayout.setVisibility(View.GONE);
        mTvOrderDate.setVisibility(View.GONE);
    }

    private List<G1417> filterByTimeRegion(List<G1417> data,String selectedTimeRegion) {
        List<G1417> result = new ArrayList<>();
        for (G1417 dct : data) {
            if (dct.getTimeRegion().equalsIgnoreCase(selectedTimeRegion)) {
                result.add(dct);
            }
        }
        return result;
    }

    @Override
    protected void initEvents() {
        mElvDctSelectView
                .setOnChildClickListener(
                        new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                Bundle b = new Bundle();
                                b.putBoolean(Constants.KEY_IS_TYPE_REGIST, mFromIntentIsTypeRegist);
                                b.putString(Constants.KEY_TYPE_ID, mFromIntentTypeID);
                                b.putSerializable(Constants.KEY_DEPT, mFromIntentDept);
                                G1417 selectedDct = (G1417) mAdapter.getChild(groupPosition, childPosition);
                                b.putSerializable(Constants.KEY_DCT, selectedDct);
                                if (mFromIntentIsTypeRegist&&mFromIntentOrderDateStr==null) {
                                    mFromIntentOrderDateStr = CommonUtil.phareDateFormat(new Date(), Constants.FORMAT_ISO_DATE);
                                }
                                b.putString(Constants.KEY_ORDER_DATE, mFromIntentOrderDateStr);
                                if (mFromIntentIsTypeRegist
                                        ||!HospitalParams.VALUE_TWO.equalsIgnoreCase(mPostionOfOrderDate)) {
                                    b.putSerializable(HospSourceSelectActivity.KEY_GOOD_DOCTS, (Serializable) mAdapter.getGrpedDoctsOfChild(groupPosition, childPosition));
                                } else {
                                    b.putSerializable(HospSourceSelectActivity.KEY_GOOD_DOCTS,
                                            (Serializable)filterByTimeRegion( mGroupedDoctList.get(groupPosition),selectedDct.getTimeRegion()));
                                }
                                openActivity(HospSourceSelectActivity.class, b);
                                return false;
                            }
                        }

                );
        mTlDateSelectLayout
                .setOnTabSelectedListener(
                        new TabLayout.OnTabSelectedListener()

                        {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                try {
                                    mOrderDate = CommonUtils.stringPhaseDate(tab.getText().toString(), Constants.FORMAT_ISO_DATE);
                                    mTvOrderDate.setText(String.format("请选择预约时间：%s", CommonUtils.phareDateFormat("yyyy年MM月", mOrderDate)));
                                    loadDctByDate("");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        }

                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select);
        addHomeButton();
        addBackListener();
        initViews();
        initEvents();

        if (mFromIntentIsTypeRegist) {
            loadRegistDataAsync();
        } else loadApointmentDataAsync(mFromIntentOrderDateStr, mFromIntentOrderDateStr);
    }


    private View getTabView(G1412 tabData) throws ParseException {
        View tabView = View.inflate(this, R.layout.item_date_select, null);
        TextView title = (TextView) tabView.findViewById(R.id.item_date_select_title);
        TextView subTitle = (TextView) tabView.findViewById(R.id.item_date_select_subTitle);
        Calendar c = Calendar.getInstance();
        Date tabDate = CommonUtils.stringPhaseDate(tabData.getOrderDate(), Constants.FORMAT_ISO_DATE);
        c.setTime(tabDate);
        title.setText(CommonUtils.phareDateFormat(Constants.FORMAT_WEEK, tabDate));
        subTitle.setText(c.get(Calendar.DAY_OF_MONTH) + "");
        return tabView;
    }

    private void loadDctByDate(String dateString) {
//        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
//            mLoadingView.setVisibility(View.VISIBLE);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    List<G1417> doctList = DemoGenerator.getDemoData(new T1413());
//                    mLoadingView.setVisibility(View.GONE);
//                    if (mAdapter == null) {
//                        mAdapter = new DoctorSelectExpandAdapter(DoctorSelectActivity.this, doctList);
//                        mElvDctSelectView.setAdapter(mAdapter);
//                    } else {
//                        mAdapter.update(doctList);
//                    }
//                    for (int i = 0; i < mAdapter.getGroupCount(); i++) {
//                        mElvDctSelectView.expandGroup(i);
//                    }
//                }
//            },300);
//        }
    }

    private void expandGroup() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mElvDctSelectView.expandGroup(i);
        }
    }

    public void loadRegistDataAsync() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        ApiCodeTemplate.loadRegistDoctorsAsync(TAG, mLoadingView, Integer.parseInt(mFromIntentTypeID), mFromIntentDept.getDeptID(), new RequestCallback<List<G1417>>() {
            @Override
            public void onRequestSuccess(List<G1417> result) {
                if (result != null && result.size() > 0) {
                    CommonUtils.removeNull(result);
                    if (mAdapter == null) {
                        mAdapter = new DoctorSelectExpandAdapter(DoctorSelectActivity.this, result);
                        mElvDctSelectView.setAdapter(mAdapter);
                    } else {
                        mAdapter.update(result);
                    }
                    expandGroup();
                } else {
                    showToast("无排班医生");
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    public void loadApointmentDataAsync(final String startDate, final String endDate) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        ApiCodeTemplate.loadAppointDoctorsAsync(TAG, mLoadingView, Integer.parseInt(mFromIntentTypeID), mFromIntentDept.getDeptID(), startDate, endDate, new RequestCallback<List<G1417>>() {
            @Override
            public void onRequestSuccess(List<G1417> result) {
                if (result != null && result.size() > 0) {
                    CommonUtils.removeNull(result);
                    if (mAdapter == null) {
                        //position=1,表示先选择日期再选择医生
                        /* bug2120 LiuTao 20160804 change */
                        if (HospitalParams.VALUE_TWO.equalsIgnoreCase(mPostionOfOrderDate)) {
                            groupBy(result);
                        }
                        mAdapter = new DoctorSelectExpandAdapter(DoctorSelectActivity.this, result);
                        mElvDctSelectView.setAdapter(mAdapter);
                    } else {
                        mAdapter.update(result);
                    }
                    expandGroup();
                } else {
                    showToast("无预约医生");
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(DoctorSelectActivity.this, (Exception) error.getException());
            }
        });
    }

    /**
     * 号源分组：通过把号源按医生分组，得到每个医生的号源列表，用于后面做日期筛选.
     *
     * @param unGroupedDcts
     * @return 返回可预约医生的列表
     */
    private List<G1417> groupBy(List<G1417> unGroupedDcts) {
        if (unGroupedDcts == null) {
            return Collections.EMPTY_LIST;
        }
        if (mGroupedDoctList == null) {
            mGroupedDoctList = new ArrayList<>();
        }
        List<G1417> groupHeaders = new LinkedList<>();
        SparseBooleanArray groupedFlags = new SparseBooleanArray(unGroupedDcts.size());
        for (int unGrpIdx = 0, n = unGroupedDcts.size(); unGrpIdx < n; unGrpIdx++) {
            if (groupedFlags.get(unGrpIdx)) continue;
            List<G1417> curGroup = new LinkedList<>();
            G1417 curGrpStandard = unGroupedDcts.get(unGrpIdx);
            //把当前的分组标准也加入到分组列表中
            curGroup.add(curGrpStandard);
            for (int doGroupIdx = unGrpIdx + 1; doGroupIdx < n; doGroupIdx++) {
                G1417 compare = unGroupedDcts.get(doGroupIdx);
                try {
                    //分组条件
                    if (((TextUtils.isEmpty(curGrpStandard.getTypeID()) && TextUtils.isEmpty(compare.getTypeID())) || curGrpStandard.getTypeID().equals(compare.getTypeID()))
                            /*&&((TextUtils.isEmpty(curGrpStandard.getRegSourceName())&&TextUtils.isEmpty(compare.getRegSourceName()))||curGrpStandard.getRegSourceName().equals(compare.getRegSourceName()))*/
                            && ((TextUtils.isEmpty(curGrpStandard.getDoctID()) && TextUtils.isEmpty(compare.getDoctID())) || (curGrpStandard.getDoctID().equals(compare.getDoctID())))
                            /*&&((TextUtils.isEmpty(curGrpStandard.getRankID())&&TextUtils.isEmpty(compare.getRankID()))||curGrpStandard.getRankID().equals(compare.getRankID()))*/
                            && ((TextUtils.isEmpty(curGrpStandard.getDeptID()) && TextUtils.isEmpty(compare.getDeptID())) || curGrpStandard.getDeptID().equals(compare.getDeptID()))) {
                        curGroup.add(compare);
                        groupedFlags.put(doGroupIdx, true);
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "groupBy", e);
                    continue;
                }
            }
            mGroupedDoctList.add(curGroup);
            //分组后，把当前group标准加入到group头中,groupHeader用于显示医生列表
            groupHeaders.add(curGrpStandard);
        }
        return groupHeaders;
    }

    private class DoctorSelectExpandAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        LayoutInflater mInflater;
        private List<G1417> mDocts;
        private List<G1417> mGroups;

        private List<List<G1417>> mChildsGrpedByTimeRegion;
        private Map<String, List<G1417>> mChilrenMapped;


        int widthOfTimeRegion = 0;

        public DoctorSelectExpandAdapter(Context context, List<G1417> docts) {
            mDocts = docts;
            mContext = context;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mGroups = new ArrayList<>();
            mChildsGrpedByTimeRegion = new ArrayList<>();
            mChilrenMapped = new HashMap<>();
            groupBy(docts, false);
        }

        public void clear() {
            mDocts.clear();
            mGroups.clear();
            mChildsGrpedByTimeRegion.clear();
            mChilrenMapped.clear();
            notifyDataSetChanged();
        }

        public void update(List<G1417> dcts) {
            mDocts = dcts;
            groupBy(dcts, true);
            notifyDataSetChanged();
        }

        public List<G1417> getGrpedDoctsOfChild(int groupPos, int childPos) {
            G1417 selected = (G1417) getChild(groupPos, childPos);
            String composeKey = selected.getDoctID() + selected.getTimeRegion();
            return mChilrenMapped.get(composeKey);
        }

        private void groupBy(List<G1417> mDocts, boolean isUpdate) {
            List<List<G1417>> noGrpedChildren = new ArrayList<>();
            if (isUpdate) {
                mGroups.clear();
                mChilrenMapped.clear();
                mChildsGrpedByTimeRegion.clear();
            }
            //doctId->groupNo.
            Map<String, Integer> childMaps = new HashMap<>();
            for (int i = 0, n = mDocts.size(); i < n; i++) {
                G1417 dct = mDocts.get(i);
                if (childMaps.get(dct.getDoctID()) == null) {
                    //New Group.
                    mGroups.add(dct);
                    //Set the map.
                    childMaps.put(dct.getDoctID(), mGroups.size() - 1);
                    List<G1417> newGroup = new ArrayList<>();
                    newGroup.add(dct);
                    noGrpedChildren.add(newGroup);
                } else {
                    noGrpedChildren.get(childMaps.get(dct.getDoctID())/*Get the groupNo*/).add(dct);
                }
            }

            int count = mContext.getResources().getStringArray(R.array.timeRegion).length;
            for (int grpIdx = 0; grpIdx < mGroups.size(); grpIdx++) {
                boolean[] timeRegionFlags = new boolean[count];
                List<G1417> children = noGrpedChildren.get(grpIdx);
                mChildsGrpedByTimeRegion.add(new ArrayList<G1417>());
                int timeRegionNo;
                for (int cldIdx = 0; cldIdx < children.size(); cldIdx++) {
                    G1417 doct = children.get(cldIdx);
                    timeRegionNo = TextUtils.isEmpty(doct.getTimeRegion()) ? 1 : Integer.parseInt(doct.getTimeRegion());
                    if (!timeRegionFlags[timeRegionNo - 1]) {
                        mChildsGrpedByTimeRegion.get(grpIdx).add(doct);
                        timeRegionFlags[timeRegionNo - 1] = true;
                    }
                    if (mChilrenMapped.get(doct.getDoctID() + doct.getTimeRegion()) == null) {
                        List<G1417> curTimeRegion = new ArrayList<>();
                        curTimeRegion.add(doct);
                        mChilrenMapped.put(doct.getDoctID() + doct.getTimeRegion(), curTimeRegion);
                    } else {
                        mChilrenMapped.get(doct.getDoctID() + doct.getTimeRegion()).add(doct);
                    }
                }
            }
        }

        @Override
        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildsGrpedByTimeRegion.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChildsGrpedByTimeRegion.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            DctGrpViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_dct_grp_v2, parent, false);
                holder = new DctGrpViewHolder(convertView);
                convertView.setTag(holder);

                holder.doctorPhoto.measure(0, 0);
                widthOfTimeRegion = holder.doctorPhoto.getMeasuredWidth();
            } else holder = (DctGrpViewHolder) convertView.getTag();

            G1417 item = (G1417) getGroup(groupPosition);
            holder.doctorName.setText(item.getDoctName());
            holder.doctorRank.setText(item.getRankName());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            DctChildViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_dct_child_v2, parent, false);
                holder = new DctChildViewHolder(convertView);
                convertView.setTag(holder);

                if (widthOfTimeRegion != 0) {
                    holder.timeRegion.getLayoutParams().width = widthOfTimeRegion;
                }
            } else holder = (DctChildViewHolder) convertView.getTag();


            G1417 item = (G1417) getChild(groupPosition, childPosition);
            int timeRegionNo = Integer.parseInt(TextUtils.isEmpty(item.getTimeRegion()) ? "2" : item.getTimeRegion());
            String timeRegionName = mContext.getResources().getStringArray(R.array.timeRegion)[timeRegionNo - 1];
            holder.timeRegion.setText(timeRegionName);
            holder.regFee.setText(String.format("挂号费%s元", CommonUtils.formatCash(item.getTotalRegFee())));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class DctGrpViewHolder {
        @Bind(R.id.dct_grp_photo)
        ImageView doctorPhoto;
        @Bind(R.id.dct_grp_name)
        TextView doctorName;
        @Bind(R.id.dct_grp_rank)
        TextView doctorRank;

        public DctGrpViewHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }

    class DctChildViewHolder {
        @Bind(R.id.dct_child_timeRegion)
        TextView timeRegion;
        @Bind(R.id.dct_child_reg_fee)
        TextView regFee;

        public DctChildViewHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }
}
