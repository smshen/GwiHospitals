package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBBodyPart;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DiseaseCommonActivity;
import com.gwi.selfplatform.ui.adapter.BodyPartListAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class BodyPartListFragment extends ListFragment implements LoaderCallbacks<List<KBBodyPart>> {
    

    private BodyPartListAdapter mAdapter = null;
    private List<KBBodyPart> mBodyPartList = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBodyPartList = new ArrayList<KBBodyPart>();
        mAdapter = new BodyPartListAdapter(getActivity(), mBodyPartList);
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBBodyPart part = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putString("BodyPartCode", part.getCode());
        b.putString("BodyPartName", part.getName());
        ((HospBaseActivity)getActivity()).openActivity(DiseaseCommonActivity.class, b);
    }

    @Override
    public Loader<List<KBBodyPart>> onCreateLoader(int arg0, Bundle arg1) {
        return new DiseaseLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<KBBodyPart>> arg0,
            List<KBBodyPart> data) {
        mBodyPartList.clear();
        if(data!=null&&!data.isEmpty()) {
            mBodyPartList.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBBodyPart>> arg0) {
        mBodyPartList.clear();
    }



    private static class DiseaseLoader extends AsyncTaskLoader<List<KBBodyPart>> {
        
        private List<KBBodyPart> mBodyPartList = null;

        public DiseaseLoader(Context context) {
            super(context);
        }

        @Override
        public List<KBBodyPart> loadInBackground() {
            try {
                return ApiCodeTemplate.getBodyPartsAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBBodyPart> data) {
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

}
