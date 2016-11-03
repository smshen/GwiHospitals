package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.fragment.query.AssayRecordFragment;

import java.util.ArrayList;

/**
 * 化验查询页面
 *
 * @version v2.0
 * @date 2015-12-15
 */
public class AssayQueryActivity extends AbstractQueryActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.check_assay_query);
    }

    @Override
    public ArrayList<Fragment> getFragemnts() {
        mFragments.add(AssayRecordFragment.newInstance(AssayRecordFragment.TAG_WEEK));
        mFragments.add(AssayRecordFragment.newInstance(AssayRecordFragment.TAG_MONTH));
        mFragments.add(AssayRecordFragment.newInstance(AssayRecordFragment.TAG_OTHER));
        return mFragments;
    }
}
