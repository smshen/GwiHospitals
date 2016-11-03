package com.gwi.selfplatform.ui.activity.registration;

import android.os.Bundle;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.registration.DepartAlphabetFragment;

/**
 * 一级科室显示页面：按字母表排列
 * @author 彭毅
*
 */
public class DepartAlphabetActivity extends HospBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_fragment);
        Bundle b = getIntent().getExtras();
        if (b != null
                && b.containsKey(Constants.KEY_IS_TYPE_REGIST)) {
            boolean isTypRegist = b.getBoolean(Constants.KEY_IS_TYPE_REGIST);
            if (isTypRegist) {
                setTitle(R.string.regist);
            } else {
                setTitle(R.string.order_regist);
            }
            String typeId = b.getString(Constants.KEY_TYPE_ID);
            if (b.containsKey(Constants.KEY_ORDER_DATE)) {
                String orderDate = b
                        .getString(Constants.KEY_ORDER_DATE);
                if (getSupportFragmentManager()
                        .findFragmentById(android.R.id.content) == null) {
                    Class<?> clz = (Class<?>) b.get(Constants.KEY_NEXT_ACTIVITY);
                    DepartAlphabetFragment f = DepartAlphabetFragment.newInstance(isTypRegist, orderDate,clz);
                    if(b.containsKey(Constants.KEY_SUB_HOSPITAL)){
                        G1017 subHos = (G1017) b.getParcelable(Constants.KEY_SUB_HOSPITAL);
                        f.setSelectedSubHos((G1017) b.getParcelable(Constants.KEY_SUB_HOSPITAL));
                        getSupportActionBar().setSubtitle(subHos.getHospName());
                    }
                    f.setTypeID(typeId);
                    getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content,f)
                    .commit();
                }
            } else {
                if (getSupportFragmentManager().findFragmentById(
                        R.id.content) == null) {
                    DepartAlphabetFragment f = DepartAlphabetFragment.newInstance(isTypRegist,null,null);
                    if(b.containsKey(Constants.KEY_SUB_HOSPITAL)){
                        f.setSelectedSubHos((G1017) b.getParcelable(Constants.KEY_SUB_HOSPITAL));
                    }
                    f.setTypeID(typeId);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.content,f)
                            .commit();
                }
            }
        } else {
            showToast(R.string.msg_error_common);
            finish();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
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
