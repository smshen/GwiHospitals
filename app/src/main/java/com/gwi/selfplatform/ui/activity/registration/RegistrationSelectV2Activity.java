package com.gwi.selfplatform.ui.activity.registration;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.ui.activity.query.RegistQueryActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 挂号选择页面
 * @Version 2.0
 * @Date 2015-12-04
 */
public class RegistrationSelectV2Activity extends HospBaseActivity {

    @Bind(R.id.reg_select_v2_items_layout)
    View mLayoutItems;

    @Bind(R.id.reg_select_v2_instruct_layout)
    View mLayoutInstruct;

    @Bind(R.id.reg_select_v2_click_to_collapse)
    TextView mTvClickToCollapse;

    @OnClick(R.id.reg_select_v2_appoint)
    void onAppointClick() {

        Bundle b = new Bundle();
        b.putBoolean(Constants.KEY_IS_TYPE_REGIST, false);
        if (HospitalParams.isShowDoctorCategory()) {
            openActivity(RegistCategoryActivity.class, b);
        } else {
            openActivity(DeptsSelectActivity.class, b);
        }
    }

    @OnClick(R.id.reg_select_v2_regist)
    void onRegistClick() {
        Bundle b = new Bundle();
        b.putBoolean(Constants.KEY_IS_TYPE_REGIST, true);
        if (HospitalParams.isShowDoctorCategory()) {
            openActivity(RegistCategoryActivity.class, b);
        } else {
            openActivity(DeptsSelectActivity.class,b);
        }
    }

    @OnClick(R.id.reg_select_v2_history)
    void onHistoryClick() {
        openActivity(RegistQueryActivity.class);
    }

    @OnClick(R.id.reg_select_v2_click_to_collapse)
    void clickToCollpase() {
        boolean needCollapse = (boolean) mTvClickToCollapse.getTag();
        handleCollapse(!needCollapse);
        if (needCollapse) {
            mTvClickToCollapse.setText("点击收缩");
        }else mTvClickToCollapse.setText("点击展开");
        mTvClickToCollapse.setTag(!needCollapse);
    }

    private void handleCollapse(boolean iscollapse) {
        int margin = getResources().getDimensionPixelSize(R.dimen.android_margin_normal);
        if (iscollapse) {
            // 重置滑动位置
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                mLayoutInstruct.setScrollY(0);
            } else {
                mLayoutInstruct.scrollTo(0, 0);
            }
            RelativeLayout.LayoutParams prams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, (int) (260 * getDensity()));
            prams.setMargins(margin,margin,margin,margin);
            mLayoutInstruct.setLayoutParams(prams);
        } else {
            RelativeLayout.LayoutParams prams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            prams.setMargins(margin,margin,margin,margin);
            mLayoutInstruct.setLayoutParams(prams);
        }
        // iscollapse=true,则
        containerScrollEnable(!iscollapse);
        mLayoutInstruct.invalidate();
    }

    private void containerScrollEnable(final boolean shouldScroll) {
        ((ScrollView) mLayoutInstruct)
                .setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return !shouldScroll;
                    }
                });
    }

    private float getDensity() {
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        return dm.density;
    }


    @Override
    protected void initViews() {
        mTvClickToCollapse.setTag(true);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_select_v2);
        addBackListener();
        initViews();
        initEvents();
    }
}
