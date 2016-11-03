package com.gwi.selfplatform.ui.fragment.query;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.DemoGenerator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.request.T1910;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1910;
import com.gwi.selfplatform.ui.activity.query.CostsQueryDetailActivity;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 挂号查询内容页面(依据TAG的不同，做不同的处理)
 *
 * @version v2.0
 * @date 2015-12-15
 */
public class CostsRecordFragment extends AbstractRecordFragment {
    private static final String TAG = CostsRecordFragment.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;

    private List<G1910> mDataList;
    private RegistRecordAdapter mAdapter;

    public CostsRecordFragment() {
    }

    public static CostsRecordFragment newInstance(String tag) {
        CostsRecordFragment fragment = new CostsRecordFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_TAG, tag);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        mEmptyTextView.setText(R.string.no_cost_record);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadingAsync(boolean isReload) {
        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            showLoadingDialog(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDataList = DemoGenerator.getDemoData(new T1910());
                    mAdapter = new RegistRecordAdapter(getActivity(), mDataList);
                    mRecordsRecyclerView.setAdapter(mAdapter);
                    hideLoadingDialog();
                }
            }, 500);
        } else {
            if (isReload) {
                loadCardBindAsync();
            } else {
                if (mDataList != null && mDataList.size() > 0) {
                    if (null != mAdapter) {
                        mAdapter = new RegistRecordAdapter(getActivity(), mDataList);
                        mRecordsRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    loadCardBindAsync();
                }
            }
        }
    }

    public class CostsRecordViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.total_payment)
        TextView totalPayment;
        @Bind(R.id.doctor_name)
        TextView doctorName;
        @Bind(R.id.executive_location)
        TextView executiveLocation;
        @Bind(R.id.ly_root)
        LinearLayout lyRoot;

        public CostsRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class RegistRecordAdapter extends RecyclerView.Adapter<CostsRecordViewHolder> {
        Context mContext;
        List<G1910> mRecords;


        public RegistRecordAdapter(Context context, List<G1910> records) {
            this.mContext = context;
            mRecords = records;
        }

        @Override
        public CostsRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_costs_record, parent, false);
            CostsRecordViewHolder holder = new CostsRecordViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CostsRecordViewHolder holder, int position) {
            holder.lyRoot.getLayoutParams().height = AbViewUtil.scaleValue(mContext, 150);

            holder.doctorName.setText(mRecords.get(position).getDocName());
            holder.totalPayment.setText(CommonUtils.getFormatFee(mRecords.get(position).getItemFee()));
            holder.executiveLocation.setText(mRecords.get(position).getExecDeptName());

            final int pos = position;
            holder.lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BUNDLE, mRecords.get(pos));
                    if (pos % 2 == 0) {
                        getBaseActivity().openActivity(CostsQueryDetailActivity.class, bundle);
                    } else {
                        // getBaseActivity().openActivity(CostsQueryDetail2Activity.class, bundle);
                        getBaseActivity().openActivity(CostsQueryDetailActivity.class, bundle);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null == mRecords) ? 0 : mRecords.size();
        }
    }

    private void loadCardBindAsync() {
        T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        ApiCodeTemplate.loadBindedCardAsync(getBaseActivity(), TAG, member, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    // mCardInfo = result.get(0);
                    // loadPatientInfoAsnc(result.get(0));
                    loadPayRecordsAsync(result.get(0));
                } else {
                    CommonUtils.showNoCardDialog(getBaseActivity(), new INoCardCallback() {

                        @Override
                        public void isBindNow(boolean isBind) {
                            if (isBind) {
                                // getBaseActivity().openActivityForResult(HosCardOperationActivity.class, REQUEST_CODE_BIND);
                            } else {
                                getBaseActivity().finish(R.anim.push_right_in, R.anim.push_right_out);
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }

    private void loadPatientInfoAsnc(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadPatientInfoAsync(getBaseActivity(), TAG, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                // mPatientInfo = result;
                // mTvCardBalance.setText(CommonUtils.formatCash(Double.valueOf(mPatientInfo.getMoney()),"元"));
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
            }
        });
    }

    private void loadPayRecordsAsync(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadPayRecordsAsync(getBaseActivity(), TAG, null, cardBindRec, getStartTime(), getEndTime(),
                new RequestCallback<List<G1910>>() {
                    @Override
                    public void onRequestSuccess(List<G1910> result) {
                        if (null != result && result.size() > 0) {
                            CommonUtils.removeNull(result);
                            mDataList.clear();
                            mDataList.addAll(result);
                            mAdapter = new RegistRecordAdapter(getActivity(), mDataList);
                            mRecordsRecyclerView.setAdapter(mAdapter);
                            mStatefulLayout.showContent();
                        } else {
                            mStatefulLayout.showEmpty();
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
                        mStatefulLayout.showEmpty();
                    }
                });
    }
}