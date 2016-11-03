package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBTreatmentKind;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.TreatmentListActivity;
import com.gwi.selfplatform.ui.adapter.TreatmentKindsAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TreatmentKindsFragment extends ListFragment implements LoaderCallbacks<List<KBTreatmentKind>> {
    
    private List<KBTreatmentKind> mTreatmentKinds = null;
    private TreatmentKindsAdapter mAdapter = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTreatmentKinds = new ArrayList<KBTreatmentKind>();
        setEmptyText("没有记录");
        setListShown(false);
        mAdapter = new TreatmentKindsAdapter(getActivity(), mTreatmentKinds);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle b = new Bundle();
        KBTreatmentKind kind = mAdapter.getItem(position);
        b.putString("TreatmentKindCode", kind.getCode());
        b.putString("TreatmentName", kind.getName());
        ((HospBaseActivity)(getActivity())).openActivity(TreatmentListActivity.class,b);
    }

    @Override
    public Loader<List<KBTreatmentKind>> onCreateLoader(int arg0, Bundle arg1) {
        return new KBTreatmentLoader(getActivity());
    } 

    @Override
    public void onLoadFinished(Loader<List<KBTreatmentKind>> arg0,
            List<KBTreatmentKind> data) {
        mTreatmentKinds.clear();
        if(data!=null&&!data.isEmpty()) {
            mTreatmentKinds.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBTreatmentKind>> arg0) {
        mTreatmentKinds.clear();
    }
    
    
    private static class KBTreatmentLoader extends AsyncTaskLoader<List<KBTreatmentKind>> {
        
        private List<KBTreatmentKind> mKinds;

        public KBTreatmentLoader(Context context) {
            super(context);
        }

        @Override
        public List<KBTreatmentKind> loadInBackground() {
            try {
                return ApiCodeTemplate.getTreatmentKinds(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBTreatmentKind> data) {
            mKinds = data;
            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mKinds = null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mKinds != null) {
                deliverResult(mKinds);
            }

            if (mKinds == null || takeContentChanged()) {
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
