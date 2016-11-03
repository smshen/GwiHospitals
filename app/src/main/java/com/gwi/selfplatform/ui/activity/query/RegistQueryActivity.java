package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.RegistRecordFragment;

import java.util.List;

import butterknife.Bind;

/**
 * 挂号查询页面
 * @version v2.0
 * @date 2015-12-15
 */
public class RegistQueryActivity extends HospBaseActivity {

    private static final int[] TITLE_TABS = new int[]{
            R.string.recent_week, R.string.recent_mont, R.string.other_months
    };

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.regist_query_view_pager)
    ViewPager mViewPager;

    T_Phr_BaseInfo mCurMember;
    ExT_Phr_CardBindRec mCardInfo;

    @Override
    protected void initViews() {
        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_query);
        initViews();
        initEvents();
        loadBindedCardAsync();
    }

    private void loadBindedCardAsync() {
        ApiCodeTemplate.loadBindedCardAsync(this, "RegistQueryActivity", mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    mViewPager.setAdapter(new MonthSplitQueryAdapter(getSupportFragmentManager(), mCardInfo));
                    mTabLayout.setupWithViewPager(mViewPager);

                    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                        mTabLayout.getTabAt(i).setText(TITLE_TABS[i]);
                    }
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    /**
     *
     */
    private class MonthSplitQueryAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 3;
        ExT_Phr_CardBindRec cardInfo;

        public MonthSplitQueryAdapter(FragmentManager fm,ExT_Phr_CardBindRec cardInfo) {
            super(fm);
            this.cardInfo = cardInfo;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RegistRecordFragment.newInstance(RegistRecordFragment.TAG_WEEK, cardInfo);
                case 1:
                    return RegistRecordFragment.newInstance(RegistRecordFragment.TAG_MONTH, cardInfo);
                case 2:
                    return RegistRecordFragment.newInstance(RegistRecordFragment.TAG_OTHER, cardInfo);
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}
