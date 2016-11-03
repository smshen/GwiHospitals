package com.gwi.selfplatform.ui.fragment.encyclopedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBTestCheckDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.encyclopedia.TestCheckDetailsActivity;
import com.gwi.selfplatform.ui.adapter.TestListAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 化验知识列表
 */
public class TestListFragment extends ListFragment implements LoaderCallbacks<List<KBTestCheckDetails>> {

    private List<KBTestCheckDetails> mList;

    private String mKeySearch;
    public static final String KEY_CACHE = "key_cache_test_list";
    private TestListAdapter mAdapter = null;

    public static TestListFragment newInstance(String key) {
        TestListFragment f = new TestListFragment();
        Bundle b = new Bundle();
        b.putString("key_search", key);
        b.putString("TestKindCode", "-1");
        f.setArguments(b);
        return f;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        KBTestCheckDetails testCheck = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putLong("TestId", testCheck.getTestId());
        b.putString("TestName", testCheck.getTestName());
        ((HospBaseActivity) (getActivity())).openActivity(TestCheckDetailsActivity.class, b);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList = new ArrayList<KBTestCheckDetails>();
        setEmptyText(getActivity().getString(R.string.msg_no_record));
        mAdapter = new TestListAdapter(getActivity(), mList);
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<KBTestCheckDetails>> onCreateLoader(int arg0, Bundle arg1) {
        Bundle b = getArguments();
        String testkindCode = null;
        if (b != null) {
            if (b.containsKey("TestKindCode")) {
                testkindCode = b.getString("TestKindCode");
            }
            if (b.containsKey("key_search")) {
                mKeySearch = b.getString("key_search");
            }
        }
        return new TestListLoader(getActivity(), testkindCode, mKeySearch);
    }

    @Override
    public void onLoadFinished(Loader<List<KBTestCheckDetails>> arg0,
                               List<KBTestCheckDetails> data) {
        mList.clear();
        if (data != null && !data.isEmpty()) {
            mList.addAll(data);
        }
        if (mKeySearch != null) {
            mAdapter.setTotals(data);
            mAdapter.search(mKeySearch);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<KBTestCheckDetails>> arg0) {
        mList.clear();
    }

    private static final class TestListLoader extends AsyncTaskLoader<List<KBTestCheckDetails>> {

        private List<KBTestCheckDetails> mKinds;
        private String mTestCode = null;
        private String mKeySearch = null;

        public TestListLoader(Context context, String testCode, String keySearch) {
            super(context);
            mTestCode = testCode;
            mKeySearch = keySearch;
        }

        @Override
        public List<KBTestCheckDetails> loadInBackground() {

            try {
                return ApiCodeTemplate.getTestList(mTestCode, mKeySearch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void deliverResult(List<KBTestCheckDetails> data) {
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
