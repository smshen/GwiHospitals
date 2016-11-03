package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBTreatmentDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.TreatmentDetailsActivity;
import com.gwi.selfplatform.ui.adapter.TreatmentListAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 抢救列表页面
 */
public class TreatmentListFragment extends ListFragment implements
        LoaderCallbacks<List<KBTreatmentDetails>> {
    private List<KBTreatmentDetails> mTreatmentKinds = null;
    private TreatmentListAdapter mAdapter = null;
    private String mKey = null;
    
    public static final String KEY_CACHE = "key_cache";

    public static TreatmentListFragment newInstantce(String key) {
        TreatmentListFragment f = new TreatmentListFragment();
        Bundle b = new Bundle();
        b.putString("key_search", key);
        b.putString("TreatmentKindCode", "-1");
        f.setArguments(b);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTreatmentKinds = new ArrayList<KBTreatmentDetails>();
        setEmptyText("没有记录");
        mAdapter = new TreatmentListAdapter(getActivity(), mTreatmentKinds);
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBTreatmentDetails treatment = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("TreatmentId", treatment.getTreatmentId());
        b.putString("TreatmentName", treatment.getTreatmentName());
        ((HospBaseActivity) (getActivity())).openActivity(TreatmentDetailsActivity.class, b);
    }

    @Override
    public Loader<List<KBTreatmentDetails>> onCreateLoader(int arg0, Bundle arg1) {
        String code = null;
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("TreatmentKindCode")) {
                code = b.getString("TreatmentKindCode");
            }
            if(b.containsKey("key_search")){
                mKey = b.getString("key_search");
            }
        }
        return new KBTreatmentLoader(getActivity(), code,mKey);
    }

    @Override
    public void onLoadFinished(Loader<List<KBTreatmentDetails>> arg0,
            List<KBTreatmentDetails> data) {
        mTreatmentKinds.clear();
        if (data != null && !data.isEmpty()) {
            mTreatmentKinds.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
//        if(mKey!=null) {
//            mAdapter.setTotals(data);
//            mAdapter.search(mKey);
//        }else {
//        }
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBTreatmentDetails>> arg0) {
        mTreatmentKinds.clear();
    }

    private static class KBTreatmentLoader extends
            AsyncTaskLoader<List<KBTreatmentDetails>> {

        private List<KBTreatmentDetails> mKinds;
        private String mTreatmentKindCode;
        private String mKey;

        public KBTreatmentLoader(Context context, String treatmentKindCode,String key) {
            super(context);
            mTreatmentKindCode = treatmentKindCode;
            mKey = key;
        }

        @Override
        public List<KBTreatmentDetails> loadInBackground() {
            try {
                return ApiCodeTemplate.getTreatmentList(mTreatmentKindCode, mKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBTreatmentDetails> data) {
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
