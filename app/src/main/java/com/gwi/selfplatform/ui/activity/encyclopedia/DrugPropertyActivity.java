package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.content.Intent;
import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DrugPropertyFragment;


public class DrugPropertyActivity extends HospBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        Intent intent = getIntent();
        String propertyKindCode = intent.getStringExtra("PropertyKindCode");
        String propertyName = intent.getStringExtra("PropertyName");
        int pFunCode = intent.getIntExtra("pFunCode", 0);
        if (propertyName != null) {
            setTitle(propertyName);
        }

        if (pFunCode == 0 || propertyKindCode == null) {
            propertyKindCode = "0";
            pFunCode++;
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, DrugPropertyFragment.newInstance(propertyKindCode, pFunCode), "Body Part")
                    .commit();
            addHomeButton();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, DrugPropertyFragment.newInstance(propertyKindCode, pFunCode), "Body Part")
                    .commit();
            addHomeButton();
        }
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
