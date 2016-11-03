package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.activity.nav.BodyAndSymptomActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.encyclopedia.DiseaseCommonFragment;


/**
 * 按常用疾病分类
 *
 * @author 彭毅
 */
public class DiseaseCommonActivity extends HospBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baike);
        addHomeButton();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("BodyPartName")) {
                setTitle(b.getString("BodyPartName"));
            } else if (b.containsKey("DeptName")) {
                setTitle(b.getString("DeptName"));
            } else if (b.containsKey(BodyAndSymptomActivity.KEY_SYMPTOM_NAME)) {
                setTitle(b.getString(BodyAndSymptomActivity.KEY_SYMPTOM_NAME));
            }
        } else {
            setTitle("常见疾病");
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, Fragment.instantiate(this, DiseaseCommonFragment.class.getName(), b), "Common Disease")
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
