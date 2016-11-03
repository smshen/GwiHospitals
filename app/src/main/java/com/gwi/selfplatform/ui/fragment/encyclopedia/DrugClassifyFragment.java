package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseActivity;
import com.gwi.selfplatform.module.net.beans.KBDrugDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugDetailActivity;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugLibraryActivity;
import com.gwi.selfplatform.ui.adapter.DrugClassifyAdapter;

import java.util.ArrayList;
import java.util.List;

public class DrugClassifyFragment extends ListFragment implements LoaderCallbacks<List<KBDrugDetails>> {


    private DrugClassifyAdapter mAdapter = null;
    private List<KBDrugDetails> mDrugList = null;

    private static String kindCode;
    private static String getFuncode;
    //For this:kindCode = -1 and getFuncode = 1 so that we can see the next list shows details.
    private static String mSearchMsg;

    public static DrugClassifyFragment newInstance(String kindCode, String getFuncode, String searchMsg) {
        DrugClassifyFragment f = new DrugClassifyFragment();
        Bundle b = new Bundle();
        b.putString("kindCode", kindCode);
        b.putString("getFuncode", getFuncode);
        b.putString("searchMsg", searchMsg);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrugList = new ArrayList<>();
        mAdapter = new DrugClassifyAdapter(getActivity(), mDrugList);
        setEmptyText("没有记录");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        kindCode = b.getString("kindCode");
        getFuncode = b.getString("getFuncode");
        mSearchMsg = b.getString("searchMsg");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<List<KBDrugDetails>> onCreateLoader(int arg0, Bundle arg1) {
        Bundle b = getArguments();
        if (b != null) {
            mSearchMsg = b.getString(DrugLibraryActivity.KEY_SEARCH_KEY);
        }
        return new DiseaseLoader(getActivity(), mSearchMsg);
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

        private String mSearchMsg;

        public DiseaseLoader(Context context, String searchMsg) {
            super(context);
            mSearchMsg = searchMsg;
        }

        @Override
        public List<KBDrugDetails> loadInBackground() {
            List<KBDrugDetails> result = null;
            try {
                if (getFuncode == null) {
                    result = ApiCodeTemplate.getDrugClassify(mSearchMsg, "-1");
                } else if (getFuncode.equals("1")) {
                    result = ApiCodeTemplate.getDrugClassify(mSearchMsg, kindCode);
                } else {
                    result = ApiCodeTemplate.getDrugCommons(kindCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void deliverResult(List<KBDrugDetails> data) {
            if (data != null && !data.isEmpty()) {
                mBodyPartList = data;
            }
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
        ((BaseActivity) (getActivity())).openActivity(DrugDetailActivity.class, b);
    }
}
