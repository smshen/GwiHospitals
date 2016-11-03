package com.gwi.selfplatform.ui.fragment.query;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1035;
import com.gwi.selfplatform.ui.activity.query.HospInfoDetailActivity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-4-12.
 */
public class HospInfoQueryFragment extends HospBaseFragment {
    private final static String TAG = HospInfoQueryFragment.class.getSimpleName();
    private String mTypeID;
    private List<G1035> mListData;
    private InfoAdapter mInfoAdapter;

    @Bind(R.id.list_view)
    ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mTypeID = bundle.getString(Constants.KEY_BUNDLE);

        mListData = new ArrayList<>();
        // getFakeG1035s();
        mInfoAdapter = new InfoAdapter(getActivity(), mListData);
    }

    @NonNull
    private List<G1035> getFakeG1035s() {
        G1035 item;
        for (int i = 0; i < 5; i++) {
            item = new G1035();
            item.setTitle("标题是-" + mTypeID + "-" + i);
            item.setTypeID(mTypeID);
            item.setCreateUserName("创建人是-" + mTypeID + "-" + i);
            mListData.add(item);
        }
        return mListData;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hosp_info_query, container, false);
        ButterKnife.bind(this, view);

        mListView.setAdapter(mInfoAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_BUNDLE, mListData.get(position));
                getBaseActivity().openActivity(HospInfoDetailActivity.class, bundle);
            }
        });

        request(mTypeID);
        return view;
    }

    private void request(String typeID) {
        ApiCodeTemplate.queryHospInfo(getContext(), TAG, typeID, new RequestCallback<List<G1035>>() {
            @Override
            public void onRequestSuccess(List<G1035> result) {
                if (null != result) {
                    mListData.clear();
                    mListData.addAll(result);
                    mInfoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                getFakeG1035s();
                mInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    public HospInfoQueryFragment() {
    }

    /**
     * @param tag 空值：查询所有
     *            1：就诊指南
     *            2：医院动态
     * @return
     */
    public static HospInfoQueryFragment newInstance(String tag) {
        HospInfoQueryFragment fragment = new HospInfoQueryFragment();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.KEY_BUNDLE, tag);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class InfoAdapter extends ArrayAdapter<G1035> {
        private LayoutInflater mInflater;

        public InfoAdapter(Context context, List<G1035> objects) {
            this(context, 0, objects);
        }

        public InfoAdapter(Context context, int resource, List<G1035> objects) {
            super(context, resource, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_hosp_info, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            G1035 item = getItem(position);
            holder.txtTitle.setText(item.getTitle());
            holder.txtBoby.setText(item.getCreateUserName());
            return convertView;
        }

        public class ViewHolder {
            @Bind(R.id.txt_title)
            TextView txtTitle;
            @Bind(R.id.txt_boby)
            TextView txtBoby;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
