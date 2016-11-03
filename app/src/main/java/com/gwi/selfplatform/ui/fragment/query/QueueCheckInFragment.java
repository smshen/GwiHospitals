package com.gwi.selfplatform.ui.fragment.query;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1218;
import com.gwi.selfplatform.module.net.response.G1219;
import com.gwi.selfplatform.ui.base.HospBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 诊间报到页面
 * Created by 毅 on 2016-1-15.
 */
public class QueueCheckInFragment extends HospBaseFragment {

    private static final String TAG = QueueCheckInFragment.class.getSimpleName();
    @Bind(R.id.loading_indicator)
    View mLoadingView;

    @Bind(android.R.id.list)
    RecyclerView mRecylerView;

    G1011 mPatientInfo;
    ExT_Phr_CardBindRec mCardInfo;
    T_Phr_BaseInfo mCurMember;

    List<G1218> mQueueList;

    RecordAdapter mAdapter;

    @OnClick(R.id.btn_refresh)
    void onRefresh() {
        loadQueueCheckInAsync();
    }


    public QueueCheckInFragment() {
    }

    public static QueueCheckInFragment newInstance() {
        Bundle bundle = new Bundle();
        QueueCheckInFragment fragment = new QueueCheckInFragment();
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mQueueList = new ArrayList<>();
        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queue_check_in, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new RecordAdapter(getContext(), mQueueList);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecylerView.setAdapter(mAdapter);
        loadCardBindedAsync();
        return view;
    }

    public void onEvent(G1219 result) {
        loadQueueCheckInAsync();
    }

    private void loadCardBindedAsync() {
        mQueueList.clear();
        mAdapter.notifyDataSetChanged();
        ApiCodeTemplate.loadBindedCardAsync(getActivity(), mLoadingView, TAG, mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    loadPatientInfoAsync();
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }




    private void loadPatientInfoAsync() {
        ApiCodeTemplate.loadPatientInfoAsync(getContext(), TAG, mLoadingView, mCardInfo, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mPatientInfo = result;
                loadQueueCheckInAsync();
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }


    private void loadQueueCheckInAsync() {
        mQueueList.clear();
        mAdapter.notifyDataSetChanged();
        ApiCodeTemplate.loadQueueUncheckInAsync(getActivity(), TAG, mLoadingView, mCardInfo, mPatientInfo, new RequestCallback<List<G1218>>() {
            @Override
            public void onRequestSuccess(List<G1218> result) {
                if (result != null && !result.isEmpty()) {
                    CommonUtils.removeNull(result);
                    mQueueList.addAll(result);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public class RecordAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        Context mContext;
        List<G1218> mData;


        public RecordAdapter(Context context,List<G1218> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_queue_check_in, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            final G1218 item = mData.get(position);
            holder.dctName.setText(String.format("挂号医生：%s",item.getRegSourceName()));
            holder.deptNamwithLocation.setText(String.format("%s(%s)", item.getDeptName(), item.getExecLocation()));
            holder.regDate.setText(item.getRegDate());
            holder.checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiCodeTemplate.doQueueCheckInAysnc(mContext, TAG, null, mCardInfo, mPatientInfo, item.getRegID(), new RequestCallback<G1219>() {
                        @Override
                        public void onRequestSuccess(G1219 result) {
                            Toast.makeText(mContext, "报到成功!", Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(result);
                        }

                        @Override
                        public void onRequestError(RequestError error) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.queue_check_in_dct_name)
        TextView dctName;
        @Bind(R.id.queue_check_in_dept_with_location)
        TextView deptNamwithLocation;
        @Bind(R.id.queue_check_in_date)
        TextView regDate;
        @Bind(R.id.queue_check_in)
        TextView checkIn;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
