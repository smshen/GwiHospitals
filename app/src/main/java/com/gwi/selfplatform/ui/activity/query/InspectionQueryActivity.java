package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.fragment.query.InspectionRecordFragment;

import java.util.ArrayList;

/**
 * 检查查询页面
 *
 * @version v2.0
 * @date 2015-12-15
 */
public class InspectionQueryActivity extends AbstractQueryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.label_inspection_details);
    }

    @Override
    public ArrayList<Fragment> getFragemnts() {
        mFragments.add(InspectionRecordFragment.newInstance(InspectionRecordFragment.TAG_WEEK));
        mFragments.add(InspectionRecordFragment.newInstance(InspectionRecordFragment.TAG_MONTH));
        mFragments.add(InspectionRecordFragment.newInstance(InspectionRecordFragment.TAG_OTHER));
        return mFragments;
    }
}
