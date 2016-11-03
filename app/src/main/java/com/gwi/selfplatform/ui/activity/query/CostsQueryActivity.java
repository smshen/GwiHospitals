package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.fragment.query.CostsRecordFragment;

import java.util.ArrayList;

/**
 * 门诊费用记录页面
 *
 * @version v2.0
 * @date 2015-12-15
 */
public class CostsQueryActivity extends AbstractQueryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.outpatient_costs_query);
    }

    @Override
    public ArrayList<Fragment> getFragemnts() {
        mFragments.add(CostsRecordFragment.newInstance(CostsRecordFragment.TAG_WEEK));
        mFragments.add(CostsRecordFragment.newInstance(CostsRecordFragment.TAG_MONTH));
        mFragments.add(CostsRecordFragment.newInstance(CostsRecordFragment.TAG_OTHER));
        return mFragments;
    }
}
