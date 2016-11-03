package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DiseaseAlphabetFragment;


public class DiseaseAlphaActivity extends HospBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        setTitle("按字母查询");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new DiseaseAlphabetFragment(), "Alphabet")
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
        // TODO Auto-generated method stub

    }

}
