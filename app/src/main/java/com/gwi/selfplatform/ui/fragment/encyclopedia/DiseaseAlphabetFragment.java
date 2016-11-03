package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DiseaseDetailsActivity;
import com.gwi.selfplatform.ui.adapter.DiseaseAlphaExpandableAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ExSideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 健康百科:疾病库按字母显示Fragment
 * @author 彭毅
 *
 */
public class DiseaseAlphabetFragment extends Fragment implements
        LoaderCallbacks<List<KBDiseaseDetails>>,OnChildClickListener{

    private ExpandableListView mExListView;
    private TextView mEmptyTextView;
    private View mLoadingView;
    private ExSideBar mSideBar;
    // 指示域
    private TextView mDialogText;

    private List<KBDiseaseDetails> mDiseaseList;

    private DiseaseAlphaExpandableAdapter mAdapter;
    WindowManager mManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDiseaseList = new ArrayList<KBDiseaseDetails>();
        setListShow(false);
        setEmptyText("没有记录");

        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        mManager.addView(mDialogText, lp);
        mAdapter = new DiseaseAlphaExpandableAdapter(getActivity(),
                mDiseaseList,mExListView);
        mExListView.setAdapter(mAdapter);
        mExListView.setGroupIndicator(null);
        //屏蔽点击事件
        mExListView.setOnGroupClickListener(new OnGroupClickListener() {
            
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                return true;
            }
        });
        mSideBar.setExpandableListView(mExListView, mAdapter);
        mSideBar.setTextView(mDialogText);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_list_alphabet, container,
                false);
        mExListView = (ExpandableListView) v.findViewById(android.R.id.list);
        mEmptyTextView = (TextView) v.findViewById(android.R.id.empty);
        mSideBar = (ExSideBar) v.findViewById(R.id.sideBar);
        mLoadingView = v.findViewById(R.id.progressContainer2);
        mExListView.setOnChildClickListener(this);
        mDialogText = (TextView) inflater.inflate(R.layout.list_position, null);
        return v;
    }
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
        KBDiseaseDetails disease = (KBDiseaseDetails) mAdapter.getChild(groupPosition, childPosition);
        Bundle b = new Bundle();
        b.putString("DiseaseId", disease.getDiseaseId());
        b.putString("DiseaseName", disease.getDiseaseName());
        ((HospBaseActivity)(getActivity())).openActivity(DiseaseDetailsActivity.class, b);
        return true;
    }

    public void setEmptyText(CharSequence text) {
        mEmptyTextView.setText(text);
        mEmptyTextView.setVisibility(View.GONE);
    }

    public void setListShow(boolean isShow) {
        if (isShow) {
            System.out.println("mDiseaseList: "+mDiseaseList.size());
            mLoadingView.setVisibility(View.GONE);

            if (mDiseaseList.isEmpty()) {
                mEmptyTextView.setVisibility(View.VISIBLE);
            } else {
                mExListView.setVisibility(View.VISIBLE);
            }
        } else {
            mEmptyTextView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
            mExListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        mManager.removeView(mDialogText);
        super.onDestroy();
    }

    @Override
    public Loader<List<KBDiseaseDetails>> onCreateLoader(int arg0, Bundle arg1) {
        return new DiseaseLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<KBDiseaseDetails>> loader,
            List<KBDiseaseDetails> data) {
        mDiseaseList.clear();
        if (data != null && !data.isEmpty()) {
            mDiseaseList.addAll(data);
        }
        setListShow(true);
        mAdapter.notifyDataSetChanged();
        //Expand the list.
        for(int i=0,n=mAdapter.getGroupCount();i<n;i++) {
            mExListView.expandGroup(i);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<KBDiseaseDetails>> arg0) {
        mDiseaseList.clear();
    }

    private static class DiseaseLoader extends
            AsyncTaskLoader<List<KBDiseaseDetails>> {

        private List<KBDiseaseDetails> mDiseaseList = null;

        public DiseaseLoader(Context context) {
            super(context);
        }


        @Override
        public List<KBDiseaseDetails> loadInBackground() {
            try {
                // 获取所有的疾病列表
                return ApiCodeTemplate.getDiseaseList(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBDiseaseDetails> data) {
            mDiseaseList = data;

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mDiseaseList = null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mDiseaseList != null) {
                deliverResult(mDiseaseList);
            }

            if (mDiseaseList == null || takeContentChanged()) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }

    }

}
