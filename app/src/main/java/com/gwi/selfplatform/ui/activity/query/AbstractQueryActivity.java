package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.AbstractRecordFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 抽象查询页面
 *
 * @version v2.0
 * @date 2015-12-15
 */
public abstract class AbstractQueryActivity extends HospBaseActivity {
    private static final String TAG = AbstractQueryActivity.class.getSimpleName();

    private static final int[] TITLE_TABS = new int[]{
            R.string.recent_week, R.string.recent_mont, R.string.other_months
    };

    protected ArrayList<Fragment> mFragments;

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.regist_query_view_pager)
    ViewPager mViewPager;

    @Override
    protected void initViews() {
        setTitle(R.string.outpatient_costs_query);

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setAdapter(new MonthSplitQueryAdapter(getSupportFragmentManager(), getFragemnts()));
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setText(TITLE_TABS[i]);
        }
    }

    @Override
    protected void initEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.i(TAG, "position = " + position);
                onFragmentSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_query);
        ButterKnife.bind(this);

        initDatas();
        initViews();
        initEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 进去默认加载第一页
        onFragmentSelected(0);
    }

    private void initDatas() {
        mFragments = new ArrayList<>();
    }

    public class MonthSplitQueryAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        public MonthSplitQueryAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (null == fragments) ? null : fragments.get(position);
        }

        @Override
        public int getCount() {
            return (null == fragments) ? 0 : fragments.size();
        }
    }

    public abstract ArrayList<Fragment> getFragemnts();

    private void onFragmentSelected(int position) {
        if (null != mFragments) {
            if (mFragments.size() - 1 == position) {
                ((AbstractRecordFragment) mFragments.get(position)).showMonthSelectDialog();
            } else {
                ((AbstractRecordFragment) mFragments.get(position)).loadingAsync(false);
            }
        }
    }
}
