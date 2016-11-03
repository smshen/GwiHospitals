package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.adapter.TabsAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.MonthAgoFragment;
import com.gwi.selfplatform.ui.fragment.query.MonthListFragment;
import com.gwi.selfplatform.ui.view.MonthSelectWidget;

import java.util.List;

/**
 * 支付订单记录查询页面
 */
public class PayRecordActivity extends HospBaseActivity {
    private static final String TAG = "PayRecordActivity";

    private static final String TAG_MONTH = "one_month";
    private static final String TAG_MONTH_AGO = "month_ago";

    private ExT_Phr_CardBindRec mCardInfo = null;
    private G1011 mPatientInfo;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    private T_Phr_BaseInfo mMember;

    private MonthSelectWidget mMonthSelectWidget;
    private String mStrDateStart;
    private String mStrDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_record);
        addBackListener();
        mMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        loadCardBindingAsync(mMember);

        initViews();
        initEvents();
    }

//    public static ExT_Phr_CardBindRec getCard() {
//        return mCardInfo;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_refresh) {
            if (mTabsAdapter != null) {
                getSupportLoaderManager().destroyLoader(0);
                getSupportLoaderManager().restartLoader(0, null,
                        (MonthListFragment) mTabsAdapter.getCurrentItem(0));
                getSupportLoaderManager().destroyLoader(1);
                getSupportLoaderManager().restartLoader(1, null,
                        (MonthAgoFragment) mTabsAdapter.getCurrentItem(1));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTabHost(Bundle savedInstanceState) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        View tabs = findViewById(android.R.id.tabs);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);


        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(
                mTabHost.newTabSpec(TAG_MONTH).setIndicator("近一个月记录"),
                MonthListFragment.class, null);
        mTabsAdapter.addTab(
                mTabHost.newTabSpec(TAG_MONTH_AGO).setIndicator("一个月前记录"),
                MonthAgoFragment.class, null);
        mTabHost.setCurrentTab(0);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    private void initTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        View tabs = findViewById(android.R.id.tabs);
        mTabHost.setup();

        Bundle b = new Bundle();
        b.putSerializable(T_Phr_CardBindRec.class.getSimpleName(), mCardInfo);
        b.putSerializable(G1011.class.getSimpleName(), mPatientInfo);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        mTabsAdapter.addTab(
                mTabHost.newTabSpec(TAG_MONTH).setIndicator("近一个月记录"),
                MonthListFragment.class, b);
        mTabsAdapter.addTab(
                mTabHost.newTabSpec(TAG_MONTH_AGO).setIndicator("一个月前记录"),
                MonthAgoFragment.class, b);
//        mTabsAdapter.addTab(
//                mTabHost.newTabSpec(TAG_MONTH_AGO).setIndicator("一个月前记录"),
//                MonthAgoFragment.class, b);
        mTabHost.setCurrentTab(0);

        setTabBottomLineColor();
    }

    void setTabBottomLineColor() {
        TabWidget tabWidget = mTabHost.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            tabWidget.getChildAt(i).setBackgroundResource(R.drawable.apptheme_tab_indicator_holo);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTabHost != null && TextUtils.isEmpty(mTabHost.getCurrentTabTag())) {
            outState.putString("tab", mTabHost.getCurrentTabTag());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mMonthSelectWidget = (MonthSelectWidget) findViewById(R.id.month_select);
        String[] startEnd = mMonthSelectWidget.getCurrentSelectDate();
        mMonthSelectWidget.setVisibility(View.GONE);
        mStrDateStart = startEnd[0];
        mStrDateEnd = startEnd[1];
        mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.MSG_SHOULD_FINISH:
                        finish(R.anim.push_right_in, R.anim.push_right_out);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void initEvents() {
        mMonthSelectWidget.setMonthSelectListener(new MonthSelectWidget.OnMonthSelectListener() {

            @Override
            public void onMonthSelect(String start, String end) {
                mStrDateStart = start;
                mStrDateEnd = end;
                //loadCardBindingAsync(mMember);
            }
        });
    }

    private void loadCardBindingAsync(final T_Phr_BaseInfo mMember) {

        ApiCodeTemplate.loadBindedCardAsync(this, TAG, mMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    for (ExT_Phr_CardBindRec cardInfo : result) {
                        if (cardInfo != null) {
                            mCardInfo = cardInfo;
                            initTabHost();
                        }
                    }
                } else {
                    CommonUtils.showNoCardDialog(PayRecordActivity.this,
                            new INoCardCallback() {
                                @Override
                                public void isBindNow(boolean isBind) {
                                    if (isBind) {
                                        openActivityForResult(
                                                MyMedicalCardActivity.class,
                                                1);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(PayRecordActivity.this, (Exception) error.getException());
                finish(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }
}
