package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DrugResuceFragment;


public class DrugResuceActivity extends HospBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new DrugResuceFragment(), "Body Part")
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {

    }

}
