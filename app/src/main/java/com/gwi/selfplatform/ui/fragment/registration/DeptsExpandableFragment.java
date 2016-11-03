package com.gwi.selfplatform.ui.fragment.registration;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1412;
import com.gwi.selfplatform.ui.activity.registration.DoctorSelectActivity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.kinst.jakub.view.StatefulLayout;

/**
 * A fragment representing an expandable list of Depts.
 * <p/>
 */
public class DeptsExpandableFragment extends HospBaseFragment {

    private static final String TAG = DeptsExpandableFragment.class.getSimpleName();
    private List<G1211> mDeptsList = null;
    private boolean mIsTypeRegist = true;
    private String mTypeID = Constants.DEFAULT_TYPE_ID;

    @Bind(android.R.id.list)
    ExpandableListView mELVDeptsList;

    @Bind(R.id.stateful_layout)
    StatefulLayout mStatefulLayout;

    @Bind(R.id.depts_expand_query_input)
    EditText mEtQueryInput;

    @Bind(R.id.depts_expand_query_submit)
    View mSubmit;

    @Bind(R.id.depts_select_order_date)
    TextView mTvOrderDate;

    @Bind(R.id.depts_select_date_select)
    TabLayout mTabLayout;

    Date mOrderDate;
    String mOrderDateStr;

    List<G1412> mAppointDateList;

    Class<?> mNextActivity;

    @OnClick(R.id.depts_expand_query_submit)
    void onQuerySubmit() {
        if (!TextUtils.isEmpty(mEtQueryInput.getText())) {
            mAdapter.getFilter().filter(mEtQueryInput.getText().toString());
        }
        hideKeyBoard(mSubmit);
    }

    @OnTextChanged(R.id.depts_expand_query_input)
    void onQueryTextChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mAdapter.resetQuery();
            expandListView();
        }
    }

    DeptsExpandAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeptsExpandableFragment() {
    }

    public static DeptsExpandableFragment newInstance(Bundle args) {
        DeptsExpandableFragment fragment = new DeptsExpandableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //TODO:
            mIsTypeRegist = getArguments().getBoolean(Constants.KEY_IS_TYPE_REGIST, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                mTypeID = getArguments().getString(Constants.KEY_TYPE_ID, Constants.DEFAULT_TYPE_ID);
            } else {
                if (getArguments().getString(Constants.KEY_TYPE_ID) != null) {
                    mTypeID = getArguments().getString(Constants.KEY_TYPE_ID);
                }
            }
            mNextActivity = (Class<?>) getArguments().getSerializable(Constants.KEY_NEXT_ACTIVITY);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_depts_expand_list, container, false);
        ButterKnife.bind(this, view);

        mDeptsList = new ArrayList<>();
        mAdapter = new DeptsExpandAdapter(getContext(), mDeptsList);
        mELVDeptsList.setAdapter(mAdapter);
        mELVDeptsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Bundle b = new Bundle();
                b.putBoolean(Constants.KEY_IS_TYPE_REGIST, mIsTypeRegist);
                if (!TextUtils.isEmpty(mOrderDateStr)) {
                    b.putString(Constants.KEY_ORDER_DATE, mOrderDateStr);
                }
                b.putSerializable(Constants.KEY_DEPT, (G1211) mAdapter.getChild(groupPosition, childPosition));
                if (mNextActivity != null) {
                    getBaseActivity().openActivity(mNextActivity, b);
                } else {
                    getBaseActivity().openActivity(DoctorSelectActivity.class, b);
                }
                return false;
            }
        });

        mEtQueryInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(mEtQueryInput.getText())) {
                        mAdapter.getFilter().filter(mEtQueryInput.getText().toString());
                    }
                    hideKeyBoard(mEtQueryInput);
                    return true;
                }
                return false;
            }
        });

        mStatefulLayout.setEmptyText(R.string.no_data);
        mStatefulLayout.setOfflineImageResource(R.drawable.img_sorry);
        mStatefulLayout.setOfflineText("网络异常，点击重试");
        mStatefulLayout.setRetryListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatefulLayout.showContent();
                if (!mIsTypeRegist) {
                    if (mAppointDateList == null) {
                        getAppointMentDate();
                    } else {
                        loadAppointDeptsByDate(mOrderDateStr);
                    }
                }else loadRegistDeptsByDate(mOrderDateStr);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsTypeRegist) {
            if (HospitalParams.getDateSelectPosition().equalsIgnoreCase(HospitalParams.VALUE_ZERO)) {
                getAppointMentDate();
            } else {
                mTvOrderDate.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.GONE);

                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date.getTime()+ AlarmManager.INTERVAL_DAY);
                mOrderDateStr = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, calendar.getTime());
                loadAppointDeptsByDate(mOrderDateStr);
            }
        } else {
            mTvOrderDate.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
            Date date = new Date();
            mOrderDateStr = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, date);
            loadRegistDeptsByDate(mOrderDateStr);
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {

                    mOrderDate = CommonUtils.stringPhaseDate(tab.getText().toString(), Constants.FORMAT_ISO_DATE);
                    mTvOrderDate.setText(String.format("请选择预约时间：%s", CommonUtils.phareDateFormat("yyyy年MM月", mOrderDate)));
                    G1412 dateObj = mAppointDateList.get(tab.getPosition());
                    mOrderDateStr = dateObj.getOrderDate();
                    loadAppointDeptsByDate(dateObj.getOrderDate());
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
        });
    }

    private void loadRegistDeptsByDate(String orrderDateStr) {
        mAdapter.clear();
        ApiCodeTemplate.getRegistDepartAsync(getActivity(), TAG, null, orrderDateStr, null, null, mTypeID, new RequestCallback<List<G1211>>() {
            @Override
            public void onRequestSuccess(List<G1211> result) {
                if (result != null && !result.isEmpty()) {
                    mDeptsList.addAll(result);
                    mAdapter.update();
                    expandListView();
                    mStatefulLayout.showContent();
                }else mStatefulLayout.showEmpty();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
                mStatefulLayout.showOffline();
            }
        });
    }

    private void loadAppointDeptsByDate(String orderDate) {
        mAdapter.clear();
        ApiCodeTemplate.getAppintDepartAsync(getActivity(), TAG, null, orderDate, null, null, null, new RequestCallback<List<G1211>>() {
            @Override
            public void onRequestSuccess(List<G1211> result) {
                if (result != null && !result.isEmpty()) {
                    mDeptsList.addAll(result);
                    mAdapter.update();
                    expandListView();
                    mStatefulLayout.showContent();
                } else {
                    mStatefulLayout.showEmpty();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
                mStatefulLayout.showOffline();
            }
        });
    }


    private void hideKeyBoard(View target) {
        InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(target.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }



    private MaterialDialog mMDDialog;

    private void showProgressDialog() {
        mMDDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_title_prompt)
                .content(R.string.dialog_content_loading)
                .progress(true, 0)
//                .progressIndeterminateStyle(true)
                .show();
        mMDDialog.setCanceledOnTouchOutside(false);
    }

    private void dimisssProgressDialog() {
        if (mMDDialog != null) {
            mMDDialog.dismiss();
        }
    }

    private void getAppointMentDate() {
        //TODO:
        ApiCodeTemplate.getAppointMentDateAsync(getActivity(), TAG, Integer.parseInt(mTypeID), null, null, new RequestCallback<List<G1412>>() {
            @Override
            public void onRequestSuccess(List<G1412> result) {
                boolean isFirst = true;
                if (result != null && !result.isEmpty()) {
                    mAppointDateList = result;
                    for (G1412 date : result) {
                        try {
                            View tabView = getTabView(date);
                            if (isFirst) {
                                tabView.measure(0, 0);
                                mTabLayout.getLayoutParams().height = tabView.getMeasuredHeight();
                                isFirst = false;
                                if (mAppointDateList.size() == 1) {
                                    mTabLayout.getLayoutParams().width = tabView.getMeasuredWidth()*2;
                                } else if (mAppointDateList.size() < 7) {
                                    //少于7个，自适应显示
                                    mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                                    mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                                } else {
//                                    mTabLayout.getLayoutParams().width = getActivity().getResources().getDisplayMetrics().widthPixels;
                                    mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
                                    mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                                }
                                isFirst = false;
                            }
                            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView).setText(date.getOrderDate()));
                        } catch (ParseException e) {
                            Logger.e(TAG, e.getLocalizedMessage(), e);
                        }
                    }
                } else {
                    getBaseActivity().showToast("没有查询到可预约的时间");
                    getBaseActivity().finish();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
                mStatefulLayout.showOffline();
            }
        });

    }

    private View getTabView(G1412 tabData) throws ParseException {
        View tabView = View.inflate(getContext(), R.layout.item_date_select, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                getActivity().getResources().getDisplayMetrics().widthPixels/5,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tabView.setLayoutParams(params);
        TextView title = (TextView) tabView.findViewById(R.id.item_date_select_title);
        TextView subTitle = (TextView) tabView.findViewById(R.id.item_date_select_subTitle);
        Calendar c = Calendar.getInstance();
        Date tabDate = CommonUtils.stringPhaseDate(tabData.getOrderDate(), Constants.FORMAT_ISO_DATE);
        c.setTime(tabDate);
        title.setText(CommonUtils.phareDateFormat(Constants.FORMAT_WEEK, tabDate));
        subTitle.setText(String.format("%d", c.get(Calendar.DAY_OF_MONTH)));
        return tabView;
    }

    private void expandListView() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mELVDeptsList.expandGroup(i);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static class DeptsExpandAdapter extends BaseExpandableListAdapter implements Filterable {

        private Context mContext;
        private List<G1211> mDepts;
        private List<G1211> mGrpData;
        private List<List<G1211>> mChildDataArray;
        LayoutInflater inflater;
        private List<G1211> mAllChildData = null;
        private List<G1211> mAllGrpData = null;

        private Filter mFilter;

        public DeptsExpandAdapter(Context context, List<G1211> depts) {
            this.mContext = context;
            this.mDepts = depts;
            mDepts = depts;
            mGrpData = new ArrayList<>();
            mChildDataArray = new ArrayList<>();
            mAllChildData = new ArrayList<>();
            mAllGrpData = new ArrayList<>();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            groupBy(depts,false);
        }

        public void update() {
            groupBy(mDepts,false);
            notifyDataSetChanged();
        }

        public void clear() {
            mDepts.clear();
            mGrpData.clear();
            mChildDataArray.clear();
            mAllChildData.clear();
            mAllGrpData.clear();
            notifyDataSetChanged();
        }

        private void groupBy(List<G1211> data,boolean isFilter) {
            if (isFilter) {
                mGrpData.clear();
                mChildDataArray.clear();
            }
            boolean[] flags = new boolean[data.size()];
            for (int i = 0; i < data.size(); i++) {
                G1211 item = data.get(i);
                if (TextUtil.isEmpty(item.getParDeptID())||item.getHasChildrenDept()==1) {
                    mGrpData.add(item);
                    if (!isFilter) {
                        mAllGrpData.add(item);
                    }
                    mChildDataArray.add(new ArrayList<G1211>());
                    flags[i] = true;
                }
            }
            for (int i = 0; i < data.size(); i++) {
                if (flags[i]) continue;
                else flags[i] = true;
                G1211 item = data.get(i);
                if (!isFilter) {
                    mAllChildData.add(item);
                }
                for (int grpIdx = 0; grpIdx < mGrpData.size(); grpIdx++) {
                    G1211 grpItem = mGrpData.get(grpIdx);
                    if (item.getParDeptID().equals(grpItem.getDeptID())) {
                        mChildDataArray.get(grpIdx).add(item);
                    }
                }
            }
            //Remove the empty group data.
            boolean [] removeFlags = new boolean[mGrpData.size()];
            for (int i = 0; i < removeFlags.length; i++) {
                if (mChildDataArray.get(i)==null||mChildDataArray.get(i).size()==0) {
                        removeFlags[i] = true;
                }
            }

            for (int i = removeFlags.length-1; i >=0; i--) {
                if (removeFlags[i]) {
                    mGrpData.remove(i);
                    mChildDataArray.remove(i);
                }
            }

        }

        @Override
        public int getGroupCount() {
            return mGrpData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mChildDataArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGrpData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mChildDataArray.get(groupPosition).get(childPosition);
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
            GroupHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_depart_expandable, parent, false);
                holder = new GroupHolder(convertView);
                convertView.setTag(holder);
            } else holder = (GroupHolder) convertView.getTag();
            holder.mTitle.setText(((G1211) getGroup(groupPosition)).getDeptName());
            holder.mTitle.setChecked(isExpanded);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChilHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_depart_expandable_child, parent, false);
                holder = new ChilHolder(convertView);
                convertView.setTag(holder);
            } else holder = (ChilHolder) convertView.getTag();
            holder.mContent.setText(((G1211) getChild(groupPosition, childPosition)).getDeptName());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public void resetQuery() {
            groupBy(mDepts, true);
            notifyDataSetChanged();
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new DeptsFilter(mAllChildData);
            }
            return mFilter;
        }

        public class DeptsFilter extends Filter {

            private List<G1211> mChildData;

            public DeptsFilter(List<G1211> childData) {
                this.mChildData = childData;
            }

            private final Object mLock = new Object();

            private final Comparator<G1211> ALPHACOMPARATOR = new Comparator<G1211>() {
                private final Collator sCollator = Collator.getInstance();
                @Override
                public int compare(G1211 lhs, G1211 rhs) {
                    return sCollator.compare(lhs.getDeptName(), rhs.getDeptName());
                }
            };

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                if (prefix == null || prefix.length() == 0) {
                    ArrayList<G1211> list;
                    synchronized (mLock) {
                        list = new ArrayList<>(mChildData);
                    }
                    results.values = list;
                    results.count = list.size();

                }else {
                    String prefixString = prefix.toString().toLowerCase(Locale.getDefault());
                    ArrayList<G1211> values;
                    final ArrayList<G1211> newValues = new ArrayList<G1211>();
                    synchronized (mLock) {
                        values = new ArrayList<>(mChildData);
                        final int count = values.size();
                        for(int i=0;i<count;i++) {
                            String valueText = values.get(i).getDeptName();
                            if(valueText.startsWith(prefixString)||valueText.contains(prefixString)) {
                                newValues.add(values.get(i));
                            }else if(PingYinUtil.getAllPinYin(valueText).startsWith(prefixString)){
                                //按拼音查询
                                newValues.add(values.get(i));
                            }else {
                                final String[] words = valueText.split(" ");
                                final int wordCount = words.length;

                                // Start at index 0, in case valueText starts with space(s)
                                for (int k = 0; k < wordCount; k++) {
                                    if (words[k].startsWith(prefixString)||words[k].contains(prefixString)) {
                                        newValues.add(values.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                        results.values = newValues;
                        results.count = newValues.size();
                    }

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<G1211> seachResults = (List<G1211>) results.values;
                seachResults.addAll(mGrpData);
                groupBy(seachResults,true);
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        }
    }

    public static class ChilHolder {
        @Bind(android.R.id.text2)
        TextView mContent;

        public ChilHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }

    public static class GroupHolder {
        @Bind(R.id.expand_group_title)
        CheckedTextView mTitle;

        public GroupHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }


}
