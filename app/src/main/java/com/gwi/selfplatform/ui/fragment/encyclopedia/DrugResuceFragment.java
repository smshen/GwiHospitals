package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBDrugDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugDetailActivity;
import com.gwi.selfplatform.ui.adapter.DrugClassifyAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 抢救药物
 */
public class DrugResuceFragment extends ListFragment implements LoaderCallbacks<List<KBDrugDetails>> {


    private DrugClassifyAdapter mAdapter = null;
    private List<KBDrugDetails> mDrugList = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrugList = new ArrayList<KBDrugDetails>();
        mAdapter = new DrugClassifyAdapter(getActivity(), mDrugList);
        setEmptyText("没有记录");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<List<KBDrugDetails>> onCreateLoader(int arg0, Bundle arg1) {
        return new DiseaseLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<KBDrugDetails>> arg0,
                               List<KBDrugDetails> data) {
        mDrugList.clear();
        if (data != null && !data.isEmpty()) {
            mDrugList.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBDrugDetails>> arg0) {
        mDrugList.clear();
    }


    private static class DiseaseLoader extends AsyncTaskLoader<List<KBDrugDetails>> {

        private List<KBDrugDetails> mBodyPartList = null;

        public DiseaseLoader(Context
                                     context) {
            super(context);
        }

        @Override
        public List<KBDrugDetails> loadInBackground() {
            try {
                return ApiCodeTemplate.getDrugResuce();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBDrugDetails> data) {
            mBodyPartList = data;

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mBodyPartList = null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mBodyPartList != null) {
                deliverResult(mBodyPartList);
            }

            if (mBodyPartList == null || takeContentChanged()) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        KBDrugDetails detail = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("DrugId", detail.getDurgId() + "");
        b.putString("DrugName", detail.getDrugName());
        ((HospBaseActivity) (getActivity())).openActivity(DrugDetailActivity.class, b);
    }

}
