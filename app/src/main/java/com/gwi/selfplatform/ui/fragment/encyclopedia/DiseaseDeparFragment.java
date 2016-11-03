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
import com.gwi.selfplatform.ui.activity.encyclopedia.DepartChildrenActivity;
import com.gwi.selfplatform.ui.adapter.DiseaseDepartAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 疾病对应科室列表
 */
public class DiseaseDeparFragment extends ListFragment implements LoaderCallbacks<List<KBDepart>> {
    
    
    private List<KBDepart> mKBDeparts = null;
    private DiseaseDepartAdapter mAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mKBDeparts = new ArrayList<>();
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
        b.putString("DepartName", depart.getName());
        ((HospBaseActivity)(getActivity())).openActivity(DepartChildrenActivity.class, b);
    }

    @Override
    public Loader<List<KBDepart>> onCreateLoader(int arg0, Bundle arg1) {
        return new DepartLoader(getActivity());
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
    
    private static class DepartLoader extends AsyncTaskLoader<List<KBDepart>> {
        
        private List<KBDepart> mDiseaseDepart;

        public DepartLoader(Context context) {
            super(context);
        }

        @Override
        public List<KBDepart> loadInBackground() {
            try {
                return ApiCodeTemplate.getDiseaseDepartAsync("0");
            } catch (Exception e) {
                Logger.e("DiseaseDeparFragment", "loadInBackground", e);
            }
            return null;
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
