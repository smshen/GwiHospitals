package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.selfplatform.module.net.beans.KBTestCheckKind;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.TestKindChildActivity;
import com.gwi.selfplatform.ui.activity.encyclopedia.TestListActivity;
import com.gwi.selfplatform.ui.adapter.TestKindsAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 化验知识分类页面
 */
public class TestKindsFragment extends ListFragment implements
        LoaderCallbacks<List<KBTestCheckKind>> {

    private List<KBTestCheckKind> mKinds = null;
    private TestKindsAdapter mAdapter = null;
    private String mTesKindCode = null;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle b = new Bundle();
        KBTestCheckKind kind = mAdapter.getItem(position);
        b.putString("TestKindCode", kind.getCode());
        b.putString("TestKindName", kind.getName());
        //先跳转到子一级分类
        if (mTesKindCode != null && !mTesKindCode.equals("0")) {
            ((HospBaseActivity) (getActivity())).openActivity(TestListActivity.class, b);
        } else {
            ((HospBaseActivity) (getActivity())).openActivity(TestKindChildActivity.class, b);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("没有记录");
        mKinds = new ArrayList<KBTestCheckKind>();
        mAdapter = new TestKindsAdapter(getActivity(), mKinds);
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<KBTestCheckKind>> onCreateLoader(int arg0, Bundle arg1) {
        Bundle b = getArguments();
        if (b != null) {
            if (b.containsKey("TestKindCode")) {
                mTesKindCode = b.getString("TestKindCode");
            }
        }
        return new TestKindsLoader(getActivity(), mTesKindCode);
    }

    @Override
    public void onLoadFinished(Loader<List<KBTestCheckKind>> arg0,
                               List<KBTestCheckKind> data) {
        mKinds.clear();
        if (data != null && !data.isEmpty()) {
            mKinds.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBTestCheckKind>> arg0) {
        mKinds.clear();
    }

    private static class TestKindsLoader extends
            AsyncTaskLoader<List<KBTestCheckKind>> {

        private List<KBTestCheckKind> mKinds;
        private String mTesKindCode = null;

        public TestKindsLoader(Context context, String testKindCode) {
            super(context);
            mTesKindCode = testKindCode;
        }

        @Override
        public List<KBTestCheckKind> loadInBackground() {
            try {
                return ApiCodeTemplate.getTestKinds(mTesKindCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
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

        @Override
        public void deliverResult(List<KBTestCheckKind> data) {
            mKinds = data;
            if (isStarted()) {
                super.deliverResult(data);
            }
        }

    }

}
