package com.gwi.selfplatform.ui.activity.query;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.utils.ACache;
import com.gwi.ccly.android.commonlibrary.common.utils.CommonUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1013;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import java.util.List;

import butterknife.Bind;

/**
 * 医院介绍
 * @version 2.0
 * @date 2016-1-14
 */
public class HospitalIntroductionV2Activity extends HospBaseActivity {

    private static final String TAG = HospitalIntroductionV2Activity.class.getSimpleName();

    private static final String KEY_CACHE = "key_cache_hosp";

    @Bind(R.id.hosp_intro_title)
    TextView title;
    @Bind(R.id.hosp_intro_address)
    TextView address;
    @Bind(R.id.hospital_intro_v2_content)
    TextView textView;
    @Bind(R.id.hospital_intro_v2_header_image)
    ImageView mHeaderImage;

    @Override
    protected void initViews() {
//        textView.setText(Html.fromHtml(content));
        mHeaderImage.getLayoutParams().height = AbViewUtil.scaleValue(this, 340);
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("医院概述");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_introduction_v2);
        initViews();
        initEvents();
        loadHospitalIntroduction();
    }

    private void loadHospitalIntroduction() {
        G1013.HospitalInfo info = (G1013.HospitalInfo) ACache.get(this).getAsObject(KEY_CACHE);
        if (info != null) {
            setValue(info);
            return;
        }
        ApiCodeTemplate.loadHospitalInfoAsync(this, TAG, null, new RequestCallback<List<G1013.HospitalInfo>>() {
            @Override
            public void onRequestSuccess(List<G1013.HospitalInfo> result) {
                CommonUtil.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    G1013.HospitalInfo info = result.get(0);
                    setValue(info);
                    ACache.get(HospitalIntroductionV2Activity.this).put(KEY_CACHE, info);
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    private void setValue(G1013.HospitalInfo info) {
        title.setText(info.getHospitalName()+"简介");
        address.setText("地址：" + info.getAddress());
        textView.setText(info.getIntroduction());
    }


}
