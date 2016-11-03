package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.db.gen.T_Base_DatumClass;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.ui.activity.expand.HealthEduActivity;
import com.gwi.selfplatform.ui.base.WebActivity;

import java.util.ArrayList;
import java.util.List;

public class HealthEduAdapter extends FragmentPagerAdapter implements
        HealthEduActivity.onDataChangedListener {

    private Context mContext;
    private List<T_HealthEdu_Datum> mItems = null;
    private List<T_Base_DatumClass> mTitles = new ArrayList<T_Base_DatumClass>();

    public HealthEduAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public void setTitles(List<T_Base_DatumClass> titles) {
        mTitles.clear();
        mTitles.addAll(titles);
        notifyDataSetChanged();
    }

    public List<T_Base_DatumClass> getTitles() {
        return mTitles;
    }
    
    @Override
    public Fragment getItem(int pos) {
        return HealthEduFragment.newInstance(mItems, pos);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HealthEduFragment f = (HealthEduFragment) super.instantiateItem(
                container, position);
        f.mItems = mItems;
        f.mPos = position;
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getName();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void OnDataChanged(List<T_HealthEdu_Datum> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public static class HealthEduFragment extends Fragment implements
            OnItemClickListener {

        private List<T_HealthEdu_Datum> mItems;
        private int mPos;

        public static HealthEduFragment newInstance(
                List<T_HealthEdu_Datum> items, int pos) {
            HealthEduFragment f = new HealthEduFragment();
            f.mItems = items;
            f.mPos = pos;
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(
                    R.layout.layout_health_edu_list, container, false);
            ListView listView = (ListView) v.findViewById(R.id.health_edu_list);
            
            listView.setAdapter(new HealtItemsListAdapter(getActivity(), mItems));
            listView.setOnItemClickListener(this);
            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            String url = null;
            HealtItemsListAdapter.ViewHold hold = (HealtItemsListAdapter.ViewHold) view.getTag();
            if (hold != null && hold.url != null) {
                url = hold.url;
            }
            Bundle b = new Bundle();
            b.putString(WebActivity.KEY_TITLE, getString(R.string.title_health_edu));
            b.putString(WebActivity.KEY_URL, url);
            ((HealthEduActivity) getActivity()).openActivity(
                    WebActivity.class, b);

        }

    }
}
