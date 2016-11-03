package com.gwi.selfplatform.ui.activity.query;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.QueueCheckInFragment;
import com.gwi.selfplatform.ui.fragment.query.QueueListInfoFragment;

import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class QueueQueryActivity extends HospBaseActivity {

    private static final String TAG = QueueQueryActivity.class.getSimpleName();

    @Bind(R.id.queue_query_tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.queue_query_content)
    ViewPager mVpPager;


    @Override
    protected void initViews() {
        setTitle(R.string.label_queue_query);

        mVpPager.setAdapter(new QueueQueryContentAdatper(this, getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mVpPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        int count =  mTabLayout.getTabCount();
        if (count == 2) {
            mTabLayout.getTabAt(0).setText("诊间报到");
            mTabLayout.getTabAt(1).setText("排队查询");
        } else {
            mTabLayout.getTabAt(0).setText("排队查询");
            mTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_query);
        addBackListener();
        initViews();
        initEvents();
    }

    public static class QueueQueryContentAdatper extends FragmentPagerAdapter {

        Context context;
        int nCount;

        public QueueQueryContentAdatper(Context context,FragmentManager fm) {
            super(fm);
            this.context = context;
            Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
            List<String> result = HospitalParams.getFields(HospitalParams.getValue(params,
                    HospitalParams.CODE_MOBILE_FUNCTION_LIST));
            final String QUEUE_LOG_IN = "1502";
            if (result.contains(QUEUE_LOG_IN)) {
                nCount = 2;
            } else {
                nCount = 1;
            }
        }


        @Override
        public int getCount() {
            return nCount;
        }

        @Override
        public Fragment getItem(int position) {
            if (getCount() == 2) {
                if (position == 0) {
                    return QueueCheckInFragment.newInstance();
                } else {
                    return QueueListInfoFragment.newInstance();
                }
            } else {
                return QueueListInfoFragment.newInstance();
            }

        }

    }


}
