package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBDrugProperty;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugClassifyActivity;
import com.gwi.selfplatform.ui.activity.encyclopedia.DrugPropertyActivity;
import com.gwi.selfplatform.ui.adapter.DrugPropertyAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 药物分类
 */
public class DrugPropertyFragment extends ListFragment implements LoaderCallbacks<List<KBDrugProperty>> {


    private DrugPropertyAdapter mAdapter = null;
    private List<KBDrugProperty> mPropertyList = null;

    private static String PropertyKindCode;
    private static int pFunCode;

    public static DrugPropertyFragment newInstance(String propertyKindCode, int pFunCode) {
        DrugPropertyFragment f = new DrugPropertyFragment();
        Bundle b = new Bundle();
        if (propertyKindCode != null) {
            b.putString("PropertyKindCode", propertyKindCode);
        }
        b.putInt("pFunCode", pFunCode);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPropertyList = new ArrayList<KBDrugProperty>();
        mAdapter = new DrugPropertyAdapter(getActivity(), mPropertyList);
        setEmptyText("没有记录");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        PropertyKindCode = b.getString("PropertyKindCode");
        pFunCode = b.getInt("pFunCode");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<List<KBDrugProperty>> onCreateLoader(int arg0, Bundle arg1) {
        return new DiseaseLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<KBDrugProperty>> arg0,
                               List<KBDrugProperty> data) {
        mPropertyList.clear();
        if (data != null && !data.isEmpty()) {
            mPropertyList.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBDrugProperty>> arg0) {
        mPropertyList.clear();
    }


    private static class DiseaseLoader extends AsyncTaskLoader<List<KBDrugProperty>> {

        private List<KBDrugProperty> mBodyPartList = null;

        public DiseaseLoader(Context context) {
            super(context);
        }

        @Override
        public List<KBDrugProperty> loadInBackground() {
            try {
                return ApiCodeTemplate.getDrugProperty(PropertyKindCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBDrugProperty> data) {
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
        KBDrugProperty property = mAdapter.getItem(position);

        Bundle b = new Bundle();
        if (pFunCode == 3) {
            b.putString("DrugCode", property.getCode());
            b.putString("DrugName", property.getName());
            b.putString("GetFuncode", "1");
            ((HospBaseActivity) (getActivity())).openActivity(DrugClassifyActivity.class, b);
        } else {
            pFunCode++;
            b.putString("PropertyKindCode", property.getCode());
            b.putString("PropertyName", property.getName());
            b.putInt("pFunCode", pFunCode);
            //getActivity().finish();
            ((HospBaseActivity) (getActivity())).openActivity(DrugPropertyActivity.class, b);
        }
    }

}
