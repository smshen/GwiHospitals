package com.gwi.selfplatform.ui.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.request.T1017;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 分医院选择页面
 *
 * @version 2.0
 * @Date 2015-12-05
 */
public class SubHosSelectActivity extends HospBaseActivity {

    private static final String TAG = SubHosSelectActivity.class.getSimpleName();
    private List<G1017> mSubHospitalList;
    private boolean mFromIntentIsRegist = true;

    @Bind(R.id.loading_indicator)
    View mLoadingView;

    @Bind(android.R.id.list)
    ListView mLvList;

    SubHospitalAdapter mAdapter;


    @OnItemClick(android.R.id.list)
    void onItemClick(int position) {
        Bundle b = new Bundle();
        b.putBoolean(Constants.KEY_IS_TYPE_REGIST,mFromIntentIsRegist);
        openActivity(RegistCategoryActivity.class,b);
    }

    @Override
    protected void initViews() {
        Intent i = getIntent();
        if (i != null) {
            mFromIntentIsRegist = i.getBooleanExtra(Constants.KEY_IS_TYPE_REGIST, true);
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
        addBackListener();
        initViews();
        initEvents();
        loadSubHospitalsAsync();
    }

    private void loadSubHospitalsAsync() {
        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            mHandler = new Handler();
            mLoadingView.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSubHospitalList = DemoGenerator.getDemoData(new T1017());
                    mLoadingView.setVisibility(View.GONE);
                    mAdapter = new SubHospitalAdapter(SubHosSelectActivity.this, mSubHospitalList);
                    mLvList.setAdapter(mAdapter);
                }
            },1000);
            return;
        }
        ApiCodeTemplate.getSubHospitalInfoAsync(this, TAG, new RequestCallback<List<G1017>>() {
            @Override
            public void onRequestSuccess(List<G1017> result) {
                if (result != null && !result.isEmpty()) {
                    CommonUtils.removeNull(result);
                    //TODO:
                } else {
                    showToast("没有查询到分院信息");
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(SubHosSelectActivity.this, (Exception) error.getException());
            }
        });
    }

    private class SubHospitalAdapter extends ArrayAdapter<G1017> {

        LayoutInflater mInflater;

        public SubHospitalAdapter(Context context, List<G1017> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_common_1, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else  holder = (ViewHolder) convertView.getTag();
            G1017 data = getItem(position);
            holder.indicator.setBackgroundResource(R.drawable.selector_hospital_indicator);
            holder.title.setText(data.getHospName());
            holder.subTitle.setText(String.format("地址：%s", data.getAddress()));
            return convertView;
        }

    }

    class ViewHolder {
        @Bind(R.id.item_common_indicator)
        ImageView indicator;
        @Bind(R.id.item_commonn_title)
        TextView title;
        @Bind(R.id.item_common_subtitle)
        TextView subTitle;

        public ViewHolder(View parent) {
            ButterKnife.bind(this,parent);
        }
    }
}

