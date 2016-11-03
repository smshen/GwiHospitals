package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.TreatmentListFragment;


public class TreatmentListActivity extends HospBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        Bundle b = getIntent().getExtras();
        if (b != null && b.containsKey("TreatmentName")) {
            setTitle(b.getString("TreatmentName"));
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, Fragment.instantiate(this, TreatmentListFragment.class.getName(), b), "treatment list")
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
