package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.view.View;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/24 0024.
 */
public class HealthEncyclopediaActivity extends HospBaseActivity {

    @OnClick({R.id.ly_disease_dictionary, R.id.ly_drug_dictionary, R.id.ly_first_aid, R.id.ly_report_analysis})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_disease_dictionary:
                openActivity(DiseaseLibraryActivity.class);
                break;
            case R.id.ly_drug_dictionary:
                openActivity(DrugLibraryActivity.class);
                break;
            case R.id.ly_first_aid:
                openActivity(TreatmentLibraryActivity.class);
                break;
            case R.id.ly_report_analysis:
                openActivity(TestLibraryActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_encyclopedia);
        ButterKnife.bind(this);
        setTitle("健康百科");
    }
}
