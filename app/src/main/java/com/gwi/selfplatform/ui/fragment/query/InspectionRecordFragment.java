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
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.request.T2510;
import com.gwi.selfplatform.module.net.response.G2510;
import com.gwi.selfplatform.ui.activity.query.InspectionQueryDetailActivity;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 检查查询内容页面(依据TAG的不同，做不同的处理)
 *
 * @version v2.0
 * @date 2015-12-15
 */
public class InspectionRecordFragment extends AbstractRecordFragment {
    private static final String TAG = InspectionRecordFragment.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;

    private List<G2510> mDataList;
    private RegistRecordAdapter mAdapter;

    public InspectionRecordFragment() {

    }

    public static InspectionRecordFragment newInstance(String tag) {
        InspectionRecordFragment fragment = new InspectionRecordFragment();
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
        mEmptyTextView.setText(R.string.no_inspection_record);
        return contentView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void loadingAsync(boolean isReload) {
        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            showLoadingDialog(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<G2510> tmp = DemoGenerator.getDemoData(new T2510());
                    mDataList.addAll(tmp);
                    mDataList.addAll(tmp);
                    mDataList.addAll(tmp);
                    mDataList.addAll(tmp);
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
//            loadCardBindAsync();
        }
    }

    private void loadCardBindAsync() {
        T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        ApiCodeTemplate.loadBindedCardAsync(getBaseActivity(), TAG, member, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    // mCardInfo = result.get(0);
                    getCheckReportAsync(result.get(0));
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

    private void getCheckReportAsync(ExT_Phr_CardBindRec cardInfo) {
        ApiCodeTemplate.getCheckReportAsync(TAG, null, cardInfo, getStartTime(), getEndTime(), new RequestCallback<List<G2510>>() {
            @Override
            public void onRequestSuccess(List<G2510> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    mDataList.clear();
                    mDataList.addAll(result);
                    mAdapter = new RegistRecordAdapter(getActivity(), mDataList);
                    mRecordsRecyclerView.setAdapter(mAdapter);
                    mStatefulLayout.showContent();
                }else  mStatefulLayout.showEmpty();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(getBaseActivity(), (Exception) error.getException());
                mStatefulLayout.showEmpty();
            }
        });
    }

    public class CostsRecordViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_assay_type)
        TextView txtAssayType;
        @Bind(R.id.txt_doctor_name)
        TextView txtDoctorName;
        @Bind(R.id.txt_location)
        TextView txtLocation;
        @Bind(R.id.txt_time)
        TextView txtTime;
        @Bind(R.id.ly_root)
        LinearLayout lyRoot;

        public CostsRecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class RegistRecordAdapter extends RecyclerView.Adapter<CostsRecordViewHolder> {

        Context mContext;
        List<G2510> mRecords;


        public RegistRecordAdapter(Context context, List<G2510> records) {
            this.mContext = context;
            mRecords = records;
        }

        @Override
        public CostsRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_assay_record, parent, false);
            CostsRecordViewHolder holder = new CostsRecordViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CostsRecordViewHolder holder, int position) {
            holder.lyRoot.getLayoutParams().height = AbViewUtil.scaleValue(mContext, 150);
//            holder.txtAssayType.setText("妇产科");
//            holder.txtDoctorName.setText("李医生");
//            holder.txtLocation.setText("门诊西药房");
//            holder.txtTime.setText("20160112 04:30");

            holder.txtAssayType.setText(mRecords.get(position).getRepName());
            holder.txtDoctorName.setText(mRecords.get(position).getDocName());
            holder.txtLocation.setText(mRecords.get(position).getDeptName());
            holder.txtTime.setText(mRecords.get(position).getRepTime());

            final int pos = position;
            holder.lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BUNDLE, mRecords.get(pos));
                    getBaseActivity().openActivity(InspectionQueryDetailActivity.class, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (null == mRecords) ? 0 : mRecords.size();
        }
    }
}