package com.gwi.selfplatform.ui.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.utils.CommonUtil;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import org.apache.http.impl.client.NullBackoffStrategy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 号源选择页面
 */
public class HospSourceSelectActivity extends HospBaseActivity {

    /**
     * 已经按照timeregion分组后的医生号源（如果已选择日期，则是该日期下的，如果尚未选择日期，则是该医生按timeregion
     * 分类的所有号源）
     */
    public static final String KEY_GOOD_DOCTS = "key_docts";
    private static final String TAG = HospSourceSelectActivity.class.getSimpleName();


    @Bind(R.id.hosp_source_order_date)
    TextView mTvOrderDate;

    @Bind(R.id.hosp_source_fee)
    TextView mTvFee;

    @Bind(R.id.hosp_source_doct_name)
    TextView mTvDctName;

    @Bind(R.id.hosp_source_rank_name)
    TextView mTvRankName;

    @Bind(R.id.hosp_source_tab_content)
    TabLayout mTlContentLayout;

    @Bind(R.id.hosp_source_viewpager_content)
    ViewPager mVpContentPager;

    @Bind(R.id.hosp_source_date_select)
    TabLayout mTlDateSelect;

    private String mTimeRegion;
    private Date mOrderDate;
    private List<G1417> mAppointDateList;
    private String mOrderDateStr;
    private List<G1417> mTotalData;
    private boolean mFromIntentIsTypeRegist;


    @Override
    protected void initViews() {
        Intent i = getIntent();
        if (i == null || i.getSerializableExtra(KEY_GOOD_DOCTS) == null) {
            CommonUtils.restartApp(this);
            finish();
            return;
        }
        mTotalData = (List<G1417>) i.getSerializableExtra(KEY_GOOD_DOCTS);
        mFromIntentIsTypeRegist = i.getBooleanExtra(Constants.KEY_IS_TYPE_REGIST, true);
        G1417 dct = (G1417) i.getSerializableExtra(Constants.KEY_DCT);
        String orderDate = i.getStringExtra(Constants.KEY_ORDER_DATE);

        if (mFromIntentIsTypeRegist) {
            if (TextUtil.isEmpty(orderDate)) {
                mOrderDateStr = dct.getOrderDate() == null ? dct.getDate() : dct.getOrderDate();
                mTvOrderDate.setText(String.format("挂号时间：%s", mOrderDate));
            } else {
                mTvOrderDate.setText(String.format("挂号时间：%s", orderDate));
            }
            mTlDateSelect.setVisibility(View.GONE);
        } else {
            mTvOrderDate.setText(String.format("请选择预约时间：%s", orderDate));
        }
        mTvDctName.setText(dct.getDoctName());
        mTvFee.setText(String.format("挂号费：%s元", CommonUtils.formatCash(dct.getTotalRegFee())));
        mTvRankName.setText(dct.getRankName());

        mTimeRegion = dct.getTimeRegion();

        //OrderDate为空说明要选择挂号日期
        if (orderDate == null) {
            initDateSelectControl(mTotalData);
            mVpContentPager.setAdapter(new ContentAdapter(this,
                    getTimeRegionClassfiedDctOfOneDate(mTotalData, mAppointDateList.get(0).getDate())));
        } else {
            mTlDateSelect.setVisibility(View.GONE);
            mVpContentPager.setAdapter(new ContentAdapter(this,mTotalData));
        }
        mTlContentLayout.setupWithViewPager(mVpContentPager);
        int count = mTlContentLayout.getTabCount();
        if (count == 2) {
            mTlContentLayout.getTabAt(0).setText("选择号源");
            mTlContentLayout.getTabAt(1).setText("医生简介");
        }
    }

    private void initDateSelectControl(List<G1417> dataFromIntent) {
        mAppointDateList = getDateClassifedDcts(dataFromIntent);
        boolean isFirst = true;
        for (G1417 dct : mAppointDateList) {
            try {
                View tabView = getTabView(dct);
                if (isFirst) {
                    tabView.measure(0, 0);
                    mTlDateSelect.getLayoutParams().height = tabView.getMeasuredHeight();
                    if(mAppointDateList.size()==1){
                        //只有一个的时候，靠左显示
                        mTlDateSelect.getLayoutParams().width = tabView.getMeasuredWidth()*2;
                    }else if (mAppointDateList.size() < 7) {
                        //少于7个，自适应显示
                        mTlDateSelect.setTabGravity(TabLayout.GRAVITY_FILL);
                        mTlDateSelect.setTabMode(TabLayout.MODE_FIXED);
//                        mTlDateSelect.getLayoutParams().width = tabView.getMeasuredHeight()*mAppointDateList.size();
                    }
                    isFirst = false;
                }
                mTlDateSelect.addTab(mTlDateSelect.newTab().setCustomView(tabView).setText(dct.getDate()));
            } catch (ParseException e) {
                LogUtil.e(TAG, e);
            }
        }
        try {
            setDateIndicatorValue(mTlDateSelect.getTabAt(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取不同日期的号源
     *
     * @param dcts
     * @return
     */
    private List<G1417> getDateClassifedDcts(List<G1417> dcts) {
        Map<String, G1417> mappedDcts = new HashMap<>();
        for (G1417 dct : dcts) {
            if (!TextUtil.isEmpty(dct.getDate())) {
                mappedDcts.put(dct.getDate(), dct);
            }
        }
////        if (dcts.size() < 5) {
////            Date now = new Date();
////            String nowStr = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, now);
//
//        }
        List<G1417> result = new ArrayList<>(mappedDcts.values());
       Collections.sort(result, new Comparator<G1417>() {
           @Override
           public int compare(G1417 lhs, G1417 rhs) {
               try {
                   Date lhsDate = CommonUtil.stringPhaseDate(lhs.getDate(), Constants.FORMAT_ISO_DATE);
                   Date rhsDate = CommonUtil.stringPhaseDate(rhs.getDate(), Constants.FORMAT_ISO_DATE);
                   return lhsDate.compareTo(rhsDate);
               } catch (ParseException e) {
                   e.printStackTrace();
               }
               return 0;
           }
       });
        return result;
    }

    /**
     * 获取指定日期的号源
     *
     * @param dcts
     * @param orderDate
     * @return
     */
    private List<G1417> getTimeRegionClassfiedDctOfOneDate(List<G1417> dcts, String orderDate) {
        //比较标识
        String flag = orderDate + mTimeRegion;
        List<G1417> result = new ArrayList<>();
        for (G1417 dct : dcts) {
            if (flag.equalsIgnoreCase(dct.getDate() + dct.getTimeRegion())) {
                result.add(dct);
            }
        }
        return result;
    }

    private View getTabView(G1417 dct) throws ParseException {
        View tabView = View.inflate(this, R.layout.item_date_select, null);
        TextView title = (TextView) tabView.findViewById(R.id.item_date_select_title);
        TextView subTitle = (TextView) tabView.findViewById(R.id.item_date_select_subTitle);
        Calendar c = Calendar.getInstance();
        Date tabDate = CommonUtils.stringPhaseDate(dct.getDate(), Constants.FORMAT_ISO_DATE);
        c.setTime(tabDate);
        title.setText(CommonUtils.phareDateFormat(Constants.FORMAT_WEEK, tabDate));
        subTitle.setText(String.format("%d", c.get(Calendar.DAY_OF_MONTH)));
        return tabView;
    }

    private void setDateIndicatorValue(TabLayout.Tab tab) throws Exception {
        mOrderDate = CommonUtils.stringPhaseDate(tab.getText().toString(), Constants.FORMAT_ISO_DATE);
        mTvOrderDate.setText(String.format("请选择预约时间：%s", CommonUtils.phareDateFormat("yyyy年MM月", mOrderDate)));
        G1417 dateObj = mAppointDateList.get(tab.getPosition());
        mOrderDateStr = dateObj.getDate();
    }

    @Override
    protected void initEvents() {
        mTlDateSelect.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    setDateIndicatorValue(tab);

                    List<G1417> dcts = getTimeRegionClassfiedDctOfOneDate(mTotalData, mOrderDateStr);
                    EventBus.getDefault().post(dcts);
                } catch (Exception e) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_source_select);
        addHomeButton();
        addBackListener();
        initViews();
        initEvents();
    }

    /**
     * 用于显示号源与医生简介
     */
    private class ContentAdapter extends PagerAdapter {

        Context mContext;
        List<G1417> mData;
        LayoutInflater mInflater;

        public ContentAdapter(Context context, List<G1417> data) {
            mContext = context;
            mData = data;
            mInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            if (position == 0) {
                view = mInflater.inflate(R.layout.layout_hos_source_tab_content_list, null);
                ListView dctListView = (ListView) view.findViewById(android.R.id.list);
                final DoctTimePointAdapter adapter = new DoctTimePointAdapter(mContext, mData);
                dctListView.setAdapter(adapter);

                dctListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle b = getIntent().getExtras();
                        G1417 dct = adapter.getItem(position);
                        b.putSerializable(Constants.KEY_DCT,dct);
                        if (!mFromIntentIsTypeRegist) {
                            if (mOrderDateStr == null) {
                                mOrderDateStr = TextUtil.isEmpty(dct.getOrderDate())?dct.getDate():dct.getOrderDate();
                            }
                            b.putString(Constants.KEY_ORDER_DATE, mOrderDateStr);
                        }
                        openActivity(RegistConfirmV2Activity.class, b);
                    }
                });

            } else {
                view = mInflater.inflate(R.layout.layout_hos_source_tab_content_text, null);
                TextView dctIntro = (TextView) view.findViewById(android.R.id.text1);
                if (mData.isEmpty()||TextUtil.isEmpty(mData.get(0).getSpecify())) {
                    dctIntro.setText("暂无简介");
                } else {
                    dctIntro.setText(mData.get(0).getSpecify());
                }
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 医生号源时间节点适配器
     */
    private class DoctTimePointAdapter extends ArrayAdapter<G1417> {


        public DoctTimePointAdapter(Context context, List<G1417> objects) {
            super(context, 0, objects);
            EventBus.getDefault().register(this);
        }

        public void onEvent(List<G1417> data) {
            clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                addAll(data);
            } else {
                for (G1417 dct : data) {
                    add(dct);
                }
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DctTimePointViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_hosp_source_time_point, parent, false);
                holder = new DctTimePointViewHolder(convertView);
                convertView.setTag(holder);
            } else holder = (DctTimePointViewHolder) convertView.getTag();
            G1417 item = getItem(position);
            if (TextUtil.isEmpty(item.getEndTime())) {
                holder.timeRange.setText(item.getStartTime());
            } else {
                holder.timeRange.setText(String.format("%s-%s", item.getStartTime(), item.getEndTime()));
            }
            if (item.getHaveCount() == 0) {
                holder.layoutRegistable.setVisibility(View.GONE);
                holder.noSourceLeft.setVisibility(View.VISIBLE);
            } else {
                holder.sourceCount.setText(String.valueOf(item.getHaveCount()));
            }
            return convertView;
        }
    }

    public class DctTimePointViewHolder {

        @Bind(R.id.item_hos_source_time)
        TextView timeRange;
        @Bind(R.id.item_hos_source_full_text)
        TextView noSourceLeft;
        @Bind(R.id.item_hos_source_registable_count_layout)
        View layoutRegistable;
        @Bind(R.id.item_hos_source_registatble_count)
        TextView sourceCount;

        public DctTimePointViewHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }
}
