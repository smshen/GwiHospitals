package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.content.Intent;
import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DrugClassifyFragment;


public class DrugClassifyActivity extends HospBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String drugCode = intent.getStringExtra("DrugCode");
        String drugName = intent.getStringExtra("DrugName");
        String getFuncode = intent.getStringExtra("GetFuncode");
        String searchMsg = intent.getStringExtra(DrugLibraryActivity.KEY_SEARCH_KEY);
        setTitle(drugName);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, DrugClassifyFragment.newInstance(drugCode, getFuncode, searchMsg), "Body Part")
                .commit();
        addHomeButton();
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
