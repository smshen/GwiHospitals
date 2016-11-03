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
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.cache.AppMemoryCache;
import com.gwi.selfplatform.module.net.beans.KBDrugUseKind;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugClassifyActivity;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugLibraryActivity;
import com.gwi.selfplatform.ui.adapter.DrugCommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class DrugCommonFragment extends ListFragment implements LoaderCallbacks<List<KBDrugUseKind>> {


    private DrugCommonAdapter mAdapter = null;
    private List<KBDrugUseKind> mDrugList = null;

    private String mUseKindDicts = null;
    private String mKeySearch = null;
    private static final String KEY_CACHE = "key_drug_all";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("UseKindDicts")) {
                mUseKindDicts = b.getString("UseKindDicts");
            }
            if (b.containsKey(DrugLibraryActivity.KEY_SEARCH_KEY)) {
                //是否需要搜索
                mKeySearch = b.getString(DrugLibraryActivity.KEY_SEARCH_KEY);
            }
        }

        mDrugList = new ArrayList<>();
        mAdapter = new DrugCommonAdapter(getActivity(), mDrugList);
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
        AppMemoryCache cache = GlobalSettings.INSTANCE.getAppMemoryCache();
        if (cache.containsKey(KEY_CACHE)) {
            cache.remove(KEY_CACHE);
        }
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBDrugUseKind drugKind = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("DrugCode", drugKind.getCode());
        b.putString("DrugName", drugKind.getName());
        b.putString("GetFuncode", "2");
        ((BaseActivity) (getActivity())).openActivity(DrugClassifyActivity.class, b);
    }

    @Override
    public Loader<List<KBDrugUseKind>> onCreateLoader(int arg0, Bundle arg1) {
        return new DiseaseLoader(getActivity(), mUseKindDicts, mKeySearch != null);
    }

    @Override
    public void onLoadFinished(Loader<List<KBDrugUseKind>> arg0,
                               List<KBDrugUseKind> data) {
        mDrugList.clear();
        if (data != null && !data.isEmpty()) {
            mDrugList.addAll(data);
            mAdapter.setAllSearchResults(data);
        }
        //如果key为空,直接刷新页面.
        if (mKeySearch == null) {
            mAdapter.notifyDataSetChanged();
        } else {
            //否则进行搜索
            mAdapter.search(mKeySearch);
        }
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBDrugUseKind>> arg0) {
        mDrugList.clear();
    }


    private static class DiseaseLoader extends AsyncTaskLoader<List<KBDrugUseKind>> {

        private List<KBDrugUseKind> mDrugList = null;

        private String mUseKindDicts;
        private boolean mIsNeedCaching = false;

        public DiseaseLoader(Context context, String useKindDicts, boolean isNeedCaching) {
            super(context);
            mUseKindDicts = useKindDicts;
            mIsNeedCaching = isNeedCaching;
        }

        @Override
        public List<KBDrugUseKind> loadInBackground() {
            try {
                if (mUseKindDicts != null) {
                    return ApiCodeTemplate.getCommonDrugKindDicts(mUseKindDicts);
                } else {
                    if (mIsNeedCaching) {
                        AppMemoryCache cache = GlobalSettings.INSTANCE.getAppMemoryCache();
                        if (cache.containsKey(KEY_CACHE)) {
                            return cache.get(KEY_CACHE);
                        } else {
                            List<KBDrugUseKind> result = ApiCodeTemplate.getCommonDrugKindDicts(null);
                            if (result != null && !result.isEmpty()) {
                                cache.put(KEY_CACHE, result);
                            }
                            return result;
                        }
                    } else {
                        return WebServiceController.getDrugKindDicts(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void deliverResult(List<KBDrugUseKind> data) {
            mDrugList = data;

            if (isStarted()) {
                super.deliverResult(data);
            }
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mDrugList = null;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mDrugList != null) {
                deliverResult(mDrugList);
            }

            if (mDrugList == null || takeContentChanged()) {
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
