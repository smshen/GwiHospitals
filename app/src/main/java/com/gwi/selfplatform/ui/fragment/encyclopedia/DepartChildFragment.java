package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.beans.KBDepart;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DiseaseCommonActivity;
import com.gwi.selfplatform.ui.adapter.DiseaseDepartAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DepartChildFragment extends ListFragment implements LoaderCallbacks<List<KBDepart>> {
    private List<KBDepart> mKBDeparts = null;
    private DiseaseDepartAdapter mAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mKBDeparts = new ArrayList<KBDepart>();
        mAdapter = new DiseaseDepartAdapter(getActivity(), mKBDeparts);
        setEmptyText("无记录");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBDepart depart = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("DeptCode", depart.getCode());
        b.putString("DeptName", depart.getName());
        ((HospBaseActivity)(getActivity())).openActivity(DiseaseCommonActivity.class, b);
    }


    @Override
    public Loader<List<KBDepart>> onCreateLoader(int arg0, Bundle arg1) {
        Bundle b = getArguments();
        String deptCode = null;
        if(b!=null&&b.containsKey("DeptCode")) {
            deptCode = b.getString("DeptCode");
        }
        return new DepartLoader(getActivity(),deptCode);
    }

    @Override
    public void onLoadFinished(Loader<List<KBDepart>> arg0, List<KBDepart> data) {
        mKBDeparts.clear();
        if(data!=null&&!data.isEmpty()) {
            mKBDeparts.addAll(data);
        }
        setListShown(true);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<KBDepart>> arg0) {
        mKBDeparts.clear();
    }


    @Deprecated
    private static class DepartLoader extends AsyncTaskLoader<List<KBDepart>> {
        
        private List<KBDepart> mDiseaseDepart;
        String mDeptCode;

        public DepartLoader(Context context,String deptCode) {
            super(context);
            mDeptCode = deptCode; 
        }


        @Override
        public List<KBDepart> loadInBackground() {
            try {
                List<KBDepart> result;
                result = ApiCodeTemplate.getDiseaseDepartAsync(mDeptCode);
                return filter(result);
            } catch (Exception e) {
                Logger.e("DiseaseDeparFragment", "loadInBackground", e);
            }
            return null;
        }
        
        private List<KBDepart> filter(List<KBDepart> source){
            List<KBDepart> result = new ArrayList<KBDepart>();
            if(source==null||source.isEmpty()) {
                return result;
            }
            for(int i=0,n=source.size();i<n;i++) {
                if(source.get(i).getParentCode().equals(mDeptCode)) {
                    result.add(source.get(i));
                }
            }
            return result;
        }
        
        @Override
        public void deliverResult(List<KBDepart> data) {
            mDiseaseDepart = data;

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mDiseaseDepart = null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mDiseaseDepart != null) {
                deliverResult(mDiseaseDepart);
            }

            if (mDiseaseDepart == null || takeContentChanged()) {
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
