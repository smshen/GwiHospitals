package com.gwi.selfplatform.ui.fragment.query;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1216;
import com.gwi.selfplatform.module.net.response.G1613;
import com.gwi.selfplatform.module.net.response.GBodyDatable;
import com.gwi.selfplatform.ui.activity.query.WaitingQueueDepartActivity;
import com.gwi.selfplatform.ui.activity.registration.DeptsSelectActivity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 候诊队列
 * Created by 毅 on 2016-1-15.
 */
public class QueueListInfoFragment extends HospBaseFragment {

    private static final String TAG = QueueListInfoFragment.class.getSimpleName();
    @Bind(R.id.loading_indicator)
    View mLoadingView;

    @Bind(android.R.id.list)
    ListView mLvList;

    @Bind(R.id.queue_empty)
    View mEmptyView;

    QueueAdapter mAdapter;

    private List<GBodyDatable> mQueueList;
    private T_Phr_BaseInfo mCurMember;
    private T_Phr_CardBindRec mCardInfo;
    private G1011 mPatientInfo;

    @OnItemClick(android.R.id.list)
    void onItemClick(int position) {

    }

    @OnClick(R.id.btn_refresh)
    void onRefresh() {
        loadQueueListAsync();
    }

    @OnClick(R.id.btn_queue)
    void onDepartQueue() {
        Bundle b = new Bundle();
        b.putSerializable(Constants.KEY_NEXT_ACTIVITY, WaitingQueueDepartActivity.class);
        getBaseActivity().openActivity(DeptsSelectActivity.class, b);
    }

    public QueueListInfoFragment() {
    }

    public static QueueListInfoFragment newInstance() {
        Bundle bundle = new Bundle();
        QueueListInfoFragment fragment = new QueueListInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        mQueueList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_queue_list, null);
        ButterKnife.bind(this,view);

        mCardInfo = GlobalSettings.INSTANCE.getCurCardInfo();
        mPatientInfo = GlobalSettings.INSTANCE.getCurPatientInfo();

        mAdapter = new QueueAdapter(getContext(), mQueueList);
        mLvList.setAdapter(mAdapter);

        if (mCardInfo != null && mPatientInfo != null) {
            loadQueueListAsync();
        } else if (mCardInfo != null) {
            loadPatientInfo();
        } else {
            loadBindedCardAsync();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadQueueListAsync();
        }
    }

    private void loadBindedCardAsync() {

        ApiCodeTemplate.loadBindedCardAsync(getBaseActivity(), mLoadingView, TAG, mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    loadPatientInfo();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }

    private void loadPatientInfo() {
        ApiCodeTemplate.loadPatientInfoAsync(getBaseActivity(), TAG, mLoadingView, mCardInfo, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mPatientInfo = result;
                loadQueueListAsync();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }


    private void loadQueueListAsync() {
        if (mQueueList == null||mAdapter==null) return;
        mQueueList.clear();
        mAdapter.notifyDataSetChanged();
        getBaseActivity().doProgressAsyncTask(mLoadingView,
                new AsyncCallback<List<GBodyDatable>>() {

                    @Override
                    public List<GBodyDatable> callAsync() throws Exception {
                        List<GBodyDatable> result = new ArrayList<>();
                        Exception drugException = null, checkException = null;
                        //if (mHasDrugQueue) {
                        try {
                            result.addAll(ApiCodeTemplate.getDrugQueueList(getContext(), mPatientInfo, mCardInfo));
                        } catch (Exception e) {
                            drugException = e;
                        }
                        //}
                        try {
                            result.addAll(ApiCodeTemplate.getQueueList(getContext(), mPatientInfo, mCardInfo));
                        } catch (Exception e) {
                            checkException = e;
                        }
                        if (!result.isEmpty()) {
                            CommonUtils.removeNull(result);
                            Collections.sort(result);
                            return result;
                        } else {
                            if (drugException != null) {
                                throw drugException;
                            } else if (checkException != null) {
                                throw checkException;
                            } else {
                                return result;
                            }
                        }
                    }

                    @Override
                    public void onPostCall(List<GBodyDatable> result) {
                        if (result != null && !result.isEmpty()) {
                            mQueueList.addAll(result);
                            mAdapter.notifyDataSetChanged();
                            mEmptyView.setVisibility(View.GONE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCallFailed(Exception exception) {
                        if (exception != null && exception.getLocalizedMessage() != null) {
                            Logger.e(TAG, exception.getLocalizedMessage());
                        } else {
                            getBaseActivity().showToast(R.string.msg_service_disconnected);
                        }
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private class QueueAdapter extends ArrayAdapter<GBodyDatable> {

        LayoutInflater mInflater;

        public QueueAdapter(Context context, List<GBodyDatable> objects) {
            super(context, 0, objects);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listitem_queue_query, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            GBodyDatable queueBase = getItem(position);
            if (queueBase instanceof G1216) {
                G1216 queue = (G1216) queueBase;
                String curNo;
                if (!TextUtil.isEmpty(queue.getFrontCount())) {
                    curNo = queue.getFrontCount();
                } else {
                    curNo = queue.getCurrentNo();
                }
                holder.txtQueueInfo.setText(Html.fromHtml("您在 " + "<b>" + queue.getDeptName() + "</b>"
                        + "<b> " + queue.getRegSourceName() + "</b>" + " 处的就诊序号为<b>" + queue.getWaitNo()
                        + "</b>，前面有<b>" + curNo + "</b>人未就诊" + (TextUtils.isEmpty(queue.getExecLocation()) ? "" : "，请到<b>" + queue.getExecLocation())
                        + "</b>就诊。"));
            } else {
                G1613 drugQueue = (G1613) queueBase;
                holder.txtQueueInfo.setText(Html.fromHtml("您在 " + "<b>" + drugQueue.getDeptName() + "</b>"
                        + " 处的取药序号为<b>" + drugQueue.getCurrentNo()
                        + "</b>，前面有<b>" + drugQueue.getWaitNo() + "</b>人未取药，请到<b>" + drugQueue.getExecLocation() + "号</b>窗口取药。"));
            }

            return convertView;
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'listitem_queue_query.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.txt_queue_info)
        TextView txtQueueInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
