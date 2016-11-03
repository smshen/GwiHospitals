package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DiseaseDeparFragment;


public class DiseaseDepartActivity extends HospBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        setTitle("按科室查询");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new DiseaseDeparFragment(), "Disease Department")
                .commit();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initEvents() {
        // TODO Auto-generated method stub

    }

}
