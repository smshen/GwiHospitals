package com.gwi.selfplatform.ui.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.gwi.ccly.android.commonlibrary.common.Constant;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.registration.DeptsExpandableFragment;

import butterknife.Bind;

/**
 * 科室选择页面
 */
public class DeptsSelectActivity extends HospBaseActivity {

    private boolean mFromIntentIsTypeRegist = true;

    @Bind(R.id.depts_select_content)
    FrameLayout mFlContent;

    @Override
    protected void initViews() {
        Intent i = getIntent();
        Class<?> clz=null;
        if (i != null) {
            mFromIntentIsTypeRegist = i.getBooleanExtra(Constants.KEY_IS_TYPE_REGIST, true);
            clz = (Class<?>) i.getSerializableExtra(Constants.KEY_NEXT_ACTIVITY);
        }
        if (clz != null) {
            setTitle("科室列表");
        } else {
            if (mFromIntentIsTypeRegist) {
                setTitle("挂号");
            } else {
                setTitle("预约挂号");
            }
        }

        loadContentFragment();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_regist);
        addHomeButton();
        addBackListener();
        initViews();
        initEvents();
    }

    private void loadContentFragment() {
        Bundle b = getIntent().getExtras();
        DeptsExpandableFragment fragment = DeptsExpandableFragment.newInstance(b);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.depts_select_content, fragment)
                .commit();
    }
}
