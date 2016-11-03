package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.HospInfoQueryFragment;

/**
 * Created by Administrator on 2016-4-12.
 */
public class HospInfoQueryActivity extends HospBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_info_query);

        String type = getIntent().getExtras().getString(Constants.KEY_BUNDLE);

        // 初始化标题
        if (Constants.HospInfoQuery.HOSPITAL_NEWS.equals(type)) {
            setTitle(R.string.label_hospital_news);
        } else if (Constants.HospInfoQuery.TREATMENT_GUIDELINES.equals(type)) {
            setTitle(R.string.label_treatment_guidelines);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HospInfoQueryFragment.newInstance(type))
                .commit();
    }
}
