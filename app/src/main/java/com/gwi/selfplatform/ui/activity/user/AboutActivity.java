package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;

import com.gwi.selfplatform.ui.base.HospBaseActivity;

public class AboutActivity extends HospBaseActivity {

    private TextView mTvAppVersion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        setTitle("关于");
        mTvAppVersion = (TextView) findViewById(R.id.about_tv_app_version);
        
        mTvAppVersion.setText(CommonUtils.getAppVersion(this));
        
        TextView appName = (TextView) findViewById(R.id.about_tv_app_name);
        appName.setText(GlobalSettings.INSTANCE.getHospitalName());
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
        switch(item.getItemId()) {
        case android.R.id.home:
            finish(R.anim.push_right_in, R.anim.push_right_out);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}
