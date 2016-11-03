package com.gwi.selfplatform.ui.activity.expand;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.cache.AppMemoryCache;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_Base_DatumClass;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.ui.adapter.HealthEduAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

public class HealthEduActivity extends HospBaseActivity {
    private static final String TAG = HealthEduActivity.class.getSimpleName();

    private TabLayout mTabPageIndicator = null;
    private ViewPager mContainer = null;
    private View mProgressBar = null;
    private Button mBtnRetry = null;

    private HealthEduAdapter mAdapter = null;

    private AppMemoryCache mCache = null;

    private onDataChangedListener mOnChangedListener = null;

    public void setOnChangedListener(onDataChangedListener onChangedListener) {
        this.mOnChangedListener = onChangedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_edu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
        mHandler.sendEmptyMessageDelayed(Constants.MSG_LAZY_LOADING, 300);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish(R.anim.push_right_in, R.anim.push_right_out);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mContainer = (ViewPager) findViewById(R.id.health_edu_vp_container);
        mTabPageIndicator = (TabLayout) findViewById(R.id.health_edu_indicator);
        mProgressBar = findViewById(R.id.health_edu_pb_loading);
        mBtnRetry = (Button) findViewById(R.id.health_edu_pb_retry);
        mCache = GlobalSettings.INSTANCE.getAppMemoryCache();

        mHandler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case Constants.MSG_LAZY_LOADING:
                    
                    if (mCache.containsKey(AppMemoryCache.KEY_HEALTH_CATEGORY)) {
                        List<T_Base_DatumClass> list = mCache.get(AppMemoryCache.KEY_HEALTH_CATEGORY);
                        if(list.isEmpty()) {
                            loadCategoryNewAsync();
                        }else {
                            mAdapter.setTitles(list);
                            updateTitles(list);
                            loadDataNewAsync();
                        }
                    } else {
                        loadCategoryNewAsync();
                    }
                    break;
                }
                return false;
            }
        });

    }

    @Override
    protected void initEvents() {
        mAdapter = new HealthEduAdapter(this, getSupportFragmentManager());
        mContainer.setAdapter(mAdapter);
        mTabPageIndicator.setupWithViewPager(mContainer);
        setOnChangedListener(mAdapter);
        mContainer.addOnPageChangeListener(new OnPageChangeListener() {
                        int lastIndex = 0;

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.d(TAG, "onPageScrolled()");
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && lastIndex != mContainer.getCurrentItem()) {
                    if (GlobalSettings.INSTANCE.isIsLogined()) {
                        loadDataNewAsync();
                        lastIndex = mContainer.getCurrentItem();
                    }
                }
            }
        });

        mBtnRetry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mCache.containsKey(AppMemoryCache.KEY_HEALTH_CATEGORY)) {
                    loadCategoryNewAsync();
                } else {
                    loadDataNewAsync();
                }
                showRetry(false);
            }
        });
    }

    private void showRetry(boolean isShow) {
        if (isShow) {
            mBtnRetry.setVisibility(View.VISIBLE);
        } else {
            mBtnRetry.setVisibility(View.GONE);
        }
    }

    private void updateTitles(List<T_Base_DatumClass> result) {
        mTabPageIndicator.setupWithViewPager(mContainer);
        mTabPageIndicator.setTabsFromPagerAdapter(mAdapter);
//        int count = mTabPageIndicator.getTabCount();
//        for (int i=0;i<count;i++) {
//            mTabPageIndicator.getTabAt(i).setText(result.get(i).getName());
//        }
    }

    private void loadCategoryNewAsync() {
        final Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_HEALTH_EDU_CATEGORY, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mProgressBar)
                .mappingInto(new TypeToken<List<T_Base_DatumClass>>() {
                })
                .execute(TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        List<T_Base_DatumClass> result = (List<T_Base_DatumClass>) o;
                        if (result != null) {
                            mAdapter.setTitles(result);
                            updateTitles(result);
                            mCache.put(AppMemoryCache.KEY_HEALTH_CATEGORY,
                                    result);
                            loadDataNewAsync();
                        } else {
                            showRetry(true);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(HealthEduActivity.this, (Exception) error.getException());
                        showRetry(true);
                    }
                });
    }

    private void loadDataNewAsync() {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request,Request.FUN_CODE_HEALTH_EDU_LIST,true);
        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        T_HealthEdu_Datum curHealhTab = new T_HealthEdu_Datum();
        curHealhTab.setDatumClass(mContainer.getCurrentItem() + 1);
        request.getBody().setHealthEdu_Datum(curHealhTab);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mProgressBar)
                .mappingInto(new TypeToken<List<T_HealthEdu_Datum>>() {
                })
                .execute(TAG, new RequestCallback<List<T_HealthEdu_Datum>>() {
                    @Override
                    public void onRequestSuccess(List<T_HealthEdu_Datum> o) {
                        showRetry(false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!isDestroyed()) {
                                mOnChangedListener.OnDataChanged(o);
                            }
                        } else {
                            if (!HealthEduActivity.this.isFinishing()) {
                                mOnChangedListener.OnDataChanged(o);
                            }
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(HealthEduActivity.this, (Exception) error.getException());
                        showRetry(true);
                    }
                });
    }

    public interface onDataChangedListener {

        void OnDataChanged(List<T_HealthEdu_Datum> items);

    }

    @Override
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        super.openActivity(pClass, pBundle);
    }

}
