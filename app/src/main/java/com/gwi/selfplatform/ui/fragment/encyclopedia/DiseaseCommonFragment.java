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
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DiseaseDetailsActivity;
import com.gwi.selfplatform.ui.activity.encyclopedia.DiseaseLibraryActivity;
import com.gwi.selfplatform.ui.activity.nav.BodyAndSymptomActivity;
import com.gwi.selfplatform.ui.adapter.DiseaseCommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiseaseCommonFragment extends ListFragment implements LoaderCallbacks<List<KBDiseaseDetails>> {

    private DiseaseCommonAdapter mAdapter = null;
    private List<KBDiseaseDetails> mDiseaseList = null;
    
    private String mBodyPartCode = null;
    private String mDeptCode = null;
    private String mKeySearch = null;
    private static final String KEY_CACHE = "key_disease_all";
    
    private String mSex = null;
    private String mSymptomId = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle b = getArguments();
        if(b!=null) {
            if(b.containsKey(BodyAndSymptomActivity.KEY_SYMPTOM_ID)) {
                mSymptomId = b.getString(BodyAndSymptomActivity.KEY_SYMPTOM_ID);
                if(b.containsKey(BodyAndSymptomActivity.KEY_SEX)) {
                    mSex = b.getString(BodyAndSymptomActivity.KEY_SEX);
                }
                
            }
            if(b.containsKey("BodyPartCode")) {
                mBodyPartCode = b.getString("BodyPartCode");
            }
            if(b.containsKey("DeptCode")){
                mDeptCode = b.getString("DeptCode");
            }
            if(b.containsKey(DiseaseLibraryActivity.KEY_SEARCH_KEY)) {
                //是否需要搜索
                mKeySearch = b.getString(DiseaseLibraryActivity.KEY_SEARCH_KEY);
            }
        }
        
        mDiseaseList = new ArrayList<KBDiseaseDetails>();
        mAdapter = new DiseaseCommonAdapter(getActivity(), mDiseaseList);
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
        if(cache.containsKey(KEY_CACHE)) {
            cache.remove(KEY_CACHE);
        }
        super.onDestroy();
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBDiseaseDetails disease = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("DiseaseId", disease.getDiseaseId());
        b.putString("DiseaseName", disease.getDiseaseName());
        ((BaseActivity)(getActivity())).openActivity(DiseaseDetailsActivity.class,b);
    }

    @Override
    public Loader<List<KBDiseaseDetails>> onCreateLoader(int arg0, Bundle arg1) {
        DiseaseLoader loader = new DiseaseLoader(getActivity(),mBodyPartCode,mDeptCode,mKeySearch);
        if(mSymptomId!=null) {
            loader.setSymptom(mSymptomId, mSex);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<KBDiseaseDetails>> arg0,
            List<KBDiseaseDetails> data) {
        mDiseaseList.clear();
        if(data!=null&&!data.isEmpty()) {
            mDiseaseList.addAll(data);
            mAdapter.setAllSearchResults(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBDiseaseDetails>> arg0) {
        mDiseaseList.clear();
    }
    
    
    private static class DiseaseLoader extends AsyncTaskLoader<List<KBDiseaseDetails>> {
        
        private List<KBDiseaseDetails> mDiseaseList = null;
        
        private String mBodyPartCode;
        private String mDeptCode = null;
        
        private String mSymptomId;
        private String mSex;
        private String mSearchMsg;

        public DiseaseLoader(Context context,String bodyPartCode,String deptCode,String searchMsg) {
            super(context);
            mBodyPartCode = bodyPartCode;
            mDeptCode = deptCode;
            mSearchMsg = searchMsg;
        }
        
        public void setSymptom(String symptomId,String sex) {
            mSymptomId = symptomId;
            mSex = sex;
        }

        @Override
        public List<KBDiseaseDetails> loadInBackground() {
            try {
//                if(mBodyPartCode!=null||mDeptCode!=null) {
//                    return ApiCodeTemplate.getDiseaseList(mBodyPartCode, mDeptCode);
//                }else if(mSymptomId!=null){
//                    return WebServiceController.getSymptomsToDisease(mSymptomId, mSex);
//                }else {
//                    return WebServiceController.getCommonDisease(mSearchMsg);
//                }
                if (mBodyPartCode != null || mDeptCode != null) {
                    return ApiCodeTemplate.getDiseaseList(mBodyPartCode, mDeptCode);
                } else {
                    return ApiCodeTemplate.getCommonDisease(mSearchMsg);
                }

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
