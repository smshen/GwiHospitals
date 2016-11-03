package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.beans.KBTestCheckDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;

public class TestCheckDetailsActivity extends HospBaseActivity {

    TextView mTvSummary;
    TextView mTvReference;
    TextView mTvClinicalSignificance;
    View mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_check_detail);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        initViews();
        initEvents();
        Bundle b = getIntent().getExtras();
        Long testId = null;
        if (b != null) {
            if (b.containsKey("TestId")) {
                testId = b.getLong("TestId");
            }
            if(b.containsKey("TestName")) {
                setTitle(b.getString("TestName"));
            }
        }
        if(testId!=null) {
            loadingTestCheckDetails(testId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mTvSummary = (TextView) findViewById(R.id.test_details_summary);
        mTvReference = (TextView) findViewById(R.id.test_details_reference);
        mTvClinicalSignificance = (TextView) findViewById(R.id.test_details_clinicalSignificance);
        mLoading = findViewById(R.id.test_detail_progress_bar);
    }

    @Override
    protected void initEvents() {

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

    private void loadingTestCheckDetails(final Long testId) {
        doProgressAsyncTask(mLoading, new AsyncCallback<List<KBTestCheckDetails>>() {

            @Override
            public List<KBTestCheckDetails> callAsync() throws Exception {
                return ApiCodeTemplate.getTestDetail(String.valueOf(testId));
            }

            @Override
            public void onPostCall(List<KBTestCheckDetails> result) {
                if(result!=null&&!result.isEmpty()) {
                    KBTestCheckDetails detail = result.get(0);
                    mTvSummary.setText(Html.fromHtml(detail.getSummary()));
                    mTvReference.setText(Html.fromHtml(detail.getReference()));
                    mTvClinicalSignificance.setText(Html.fromHtml(detail.getClinicalSignificance()));
                    mTvSummary.startAnimation(AnimationUtils.loadAnimation(TestCheckDetailsActivity.this,android.R.anim.fade_in));
                    mTvReference.startAnimation(AnimationUtils.loadAnimation(TestCheckDetailsActivity.this,android.R.anim.fade_in));
                    mTvClinicalSignificance.startAnimation(AnimationUtils.loadAnimation(TestCheckDetailsActivity.this,android.R.anim.fade_in));
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e("TestCheckDetailsActivity", exception.getLocalizedMessage());
                showToast(R.string.msg_service_disconnected);
            }
        });
    }
}
