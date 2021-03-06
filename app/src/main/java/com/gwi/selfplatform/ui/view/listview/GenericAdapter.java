package com.gwi.selfplatform.ui.view.listview;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A child class shall subclass this Adapter and
 * implement method getDataRow(int position, View convertView, ViewGroup parent),
 * which supplies a View present data in a ListRow.
 * <p/>
 * This parent Adapter takes care of displaying ProgressBar in a row or
 * indicating that it has reached the last row.
 */
public abstract class GenericAdapter<T> extends BaseAdapter {
    // the main data list to save loaded data
    protected List<T> dataList;

    protected Activity mActivity;

    // the serverListSize is the total number of items on the server side,
    // which should be returned from the web request results
    protected int serverListSize = -1;

    // Two view types which will be used to determine whether a row should be displaying
    // data or a Progressbar
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;

    private boolean isOffline = false;
    private View.OnClickListener mRetryListener;

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public void setRetryListener(View.OnClickListener retryListner) {
        mRetryListener = retryListner;
    }

    public GenericAdapter(Activity activity, List<T> list) {
        mActivity = activity;
        if (list == null) {
            dataList = new ArrayList<>();
        } else {
            dataList = list;
        }
    }


    public void setServerListSize(int serverListSize) {
        this.serverListSize = serverListSize;
    }

    public void clear() {
        if (dataList != null) {
            dataList.clear();
        }
    }

    public void addAll(List<T> nData) {
        dataList.addAll(nData);
    }


    /**
     * disable click events on indicating rows
     */
    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    /**
     * One type is normal data row, the other type is Progressbar
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }


    /**
     * the size of the List plus one, the one is the last row, which displays a Progressbar
     */
    @Override
    public int getCount() {
        return dataList.size() + 1;
    }


    /**
     * return the type of the row,
     * the last row indicates the user that the ListView is loading more data
     */
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return (position >= dataList.size()) ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? dataList
                .get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position
                : -1;
    }

    /**
     * returns the correct view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            // display the last row
            return getFooterView(position, convertView, parent);
        }
        View dataRow = convertView;
        dataRow = getDataRow(position, convertView, parent);

        return dataRow;
    }

    ;

    /**
     * A subclass should override this method to supply the data row.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getDataRow(int position, View convertView, ViewGroup parent);


    /**
     * returns a View to be displayed in the last row.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getFooterView(int position, View convertView,
                              ViewGroup parent) {
        if (position >= serverListSize && serverListSize > 0) {
            // the ListView has reached the last row
            TextView tvLastRow = new TextView(mActivity);
            tvLastRow.setText("已到最后一行~");
            tvLastRow.setGravity(Gravity.CENTER);
            if (mCloseBottomRefresh) tvLastRow.setVisibility(View.GONE);
            return tvLastRow;
        } else if(isOffline) {
            TextView tvLastRow = new TextView(mActivity);
            tvLastRow.setText("网络不给力，点击重试");
            tvLastRow.setGravity(Gravity.CENTER);
            if (mRetryListener != null) {
                tvLastRow.setOnClickListener(mRetryListener);
            }
            if (mCloseBottomRefresh) tvLastRow.setVisibility(View.GONE);
        }

        View row = convertView;
        if (row == null) {
            row = mActivity.getLayoutInflater().inflate(
                    R.layout.footer_proggress, parent, false);
        }
        if (mCloseBottomRefresh) row.setVisibility(View.GONE);
        return row;
    }

    boolean mCloseBottomRefresh = false;
    public void closeBottomRefresh() {
        mCloseBottomRefresh = true;
        setServerListSize(getCount());
    }

    public void openBottomRefresh() {
        mCloseBottomRefresh = false;
        setServerListSize(-1);
    }
}
