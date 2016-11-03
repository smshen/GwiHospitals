package com.gwi.selfplatform.ui.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.utils.ACache;
import com.gwi.ccly.android.commonlibrary.common.utils.CommonUtil;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.request.T1210;
import com.gwi.selfplatform.module.net.response.G1210;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * 挂号类型选择页面
 * @version 2.0
 */
public class RegistCategoryActivity extends HospBaseActivity {

    private static final String TAG = RegistCategoryActivity.class.getSimpleName();

    private static final String KEY_CACHE_CATEGORY_REGIST = "key_cache_category_regist";
    private static final String KEY_CACHE_CATEGORY_ORDER_REGIST = "key_cache_category_order_regist";

    private boolean mFromIntentIsRegist = true;
    private List<G1210> mCtgryList;

    @Bind(R.id.loading_indicator)
    View mLoadingView;

    @Bind(android.R.id.list)
    ListView mLvCtgryList;

    RegisterCategoryAdapter mAdapter;

    @OnItemClick(android.R.id.list)
    void onItemClick(int pos) {
        Bundle b = new Bundle();
        b.putBoolean(Constants.KEY_IS_TYPE_REGIST, mFromIntentIsRegist);
        b.putString(Constants.KEY_TYPE_ID, mCtgryList.get(pos).getTypeID());
        if (HospitalParams.getDepthOfDept(GlobalSettings.INSTANCE.getHospitalParams()) == 1) {
            openActivity(DepartAlphabetActivity.class, b);
        } else {
            openActivity(DeptsSelectActivity.class, b);
        }
    }

    @Override
    protected void initViews() {
        Intent i = getIntent();
        if (i != null) {
            mFromIntentIsRegist = i.getBooleanExtra(Constants.KEY_IS_TYPE_REGIST, true);
        } else {
            CommonUtils.restartApp(this);
            finish();
        }
        mCtgryList = new ArrayList<>();
        mAdapter = new RegisterCategoryAdapter(this, mCtgryList);
        mLvCtgryList.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_list);
        initViews();
        initEvents();
        loadRegistCategoryAsync();
    }

    private void loadRegistCategoryAsync() {

        ApiCodeTemplate.loadDoctorsCategoryAsync(this, TAG,mLoadingView, mFromIntentIsRegist, new RequestCallback<List<G1210>>() {
            @Override
            public void onRequestSuccess(List<G1210> result) {
                CommonUtils.removeNull(result);
                if (result != null) {
                    CommonUtil.removeNull(result);
                    mCtgryList.addAll(result);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast("没有获取到挂号类别!");
                }

            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegistCategoryActivity.this, (Exception) error.getException());
            }
        });
    }

    private class RegisterCategoryAdapter extends ArrayAdapter<G1210> {

        LayoutInflater mInflater;

        public RegisterCategoryAdapter(Context context, List<G1210> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_regist_category, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else  holder = (ViewHolder) convertView.getTag();
            G1210 data = getItem(position);
            holder.title.setText(data.getTypeName());
            return convertView;
        }

    }

    class ViewHolder {
        @Bind(R.id.item_regist_ctgry_title)
        TextView title;

        public ViewHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }
}
