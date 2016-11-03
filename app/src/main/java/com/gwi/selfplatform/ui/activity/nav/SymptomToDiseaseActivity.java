package com.gwi.selfplatform.ui.activity.nav;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.module.net.beans.SymptomToDisease;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.activity.registration.RegistrationSelectV2Activity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 症状对于疾病列表页面
 * @author 彭毅
 *
 */
public class SymptomToDiseaseActivity extends HospBaseActivity {

    private static final String TAG = SymptomToDiseaseActivity.class.getSimpleName();

    
    View mProgressContainer;
    TextView mEmptyText;
    ExpandableListView mExList;
    SymToDisAdapter mAdapter;
    List<SymptomToDisease> mDiseaseList;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_expandable_list);
        addHomeButton();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initEvents();
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                getSymToDisListNewAsync();
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mProgressContainer = findViewById(R.id.progressContainer);
        mExList = (ExpandableListView) findViewById(android.R.id.list);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        
        mDiseaseList = new ArrayList<>();
        mAdapter = new SymToDisAdapter(this, mDiseaseList);

        mExList.setGroupIndicator(null);
        mExList.setAdapter(mAdapter);
        mExList.setEmptyView(mEmptyText);
        
        if(getIntent()!=null) {
            String title= getIntent().getStringExtra(BodyAndSymptomActivity.KEY_SYMPTOM_NAME);
            if(!TextUtils.isEmpty(title)) {
                setTitle(title);
            }
        }
    }

    @Override
    protected void initEvents() {
        
    }

    private void getSymToDisListNewAsync() {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_SYMPTOM_TO_DISEASE,true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(WebUtil.HOSP_CODE);

        String sid = getIntent().getStringExtra(BodyAndSymptomActivity.KEY_SID);
        String sex = getIntent().getStringExtra(BodyAndSymptomActivity.KEY_SEX);
        request.getBody().setSId(sid);
        request.getBody().setSex(sex);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mProgressContainer)
                .mappingInto(new TypeToken<List<SymptomToDisease>>() {
                })
                .execute(TAG, new RequestCallback<List<SymptomToDisease>>() {
                    @Override
                    public void onRequestSuccess(List<SymptomToDisease> result) {
                        mDiseaseList.clear();
                        if(result!=null&&!result.isEmpty()) {
                            mDiseaseList.addAll(result);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(SymptomToDiseaseActivity.this, (Exception) error.getException());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
    

    private class SymToDisAdapter extends BaseExpandableListAdapter {
        
        List<SymptomToDisease> mList;
        Context mContext;
        
        public SymToDisAdapter(Context context,List<SymptomToDisease> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public int getGroupCount() {
            return mList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mList.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sym_to_dis_group, parent,false);
            }
            CheckedTextView number = (CheckedTextView) convertView.findViewById(R.id.sym_to_dis_no);
            TextView name = (TextView) convertView.findViewById(R.id.sym_to_dis_name);
            number.setChecked(isExpanded);
            
            number.setText(String.valueOf(groupPosition+1));
            name.setText(((SymptomToDisease)getGroup(groupPosition)).getDiseaseName());
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sym_to_dis_child, parent,false);
            }
            SymptomToDisease std = ((SymptomToDisease)getGroup(groupPosition));
            TextView memo = (TextView) convertView.findViewById(R.id.sym_to_dis_child_memo);
            Button deptNameBtn = (Button) convertView.findViewById(R.id.sym_to_dis_dept_name);
            memo.setText(std.getMemo());
            if(!TextUtil.isEmpty(std.getDeptName())) {
                deptNameBtn.setVisibility(View.VISIBLE);
                deptNameBtn.setText(std.getDeptName());
                deptNameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(RegistrationSelectV2Activity.class);
                    }
                });
            }else {
                deptNameBtn.setVisibility(View.GONE);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }
        
    }

}
