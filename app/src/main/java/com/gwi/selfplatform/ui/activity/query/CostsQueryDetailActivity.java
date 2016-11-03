package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
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
import com.gwi.selfplatform.module.net.request.T1911;
import com.gwi.selfplatform.module.net.response.G1615;
import com.gwi.selfplatform.module.net.response.G1910;
import com.gwi.selfplatform.module.net.response.G1911;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.AutoWrapView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CostsQueryDetailActivity extends HospBaseActivity {
    private static final String TAG = CostsQueryDetailActivity.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;
    private List<G1911> mDataList;
    private G1910 mG1910Info;
    private G1615 mC1615Info;

    @Bind(R.id.patient_name)
    TextView patientName;
    @Bind(R.id.prescribing_sections)
    TextView prescribingSections;
    @Bind(R.id.executive_departments)
    TextView executiveDepartments;
    @Bind(R.id.doctors_prescribing)
    TextView doctorsPrescribing;
    @Bind(R.id.prescribing_time)
    TextView prescribingTime;
    @Bind(R.id.medical_card)
    TextView medicalCard;
    @Bind(R.id.prescription_amount)
    TextView prescriptionAmount;
    @Bind(R.id.wrap_view)
    AutoWrapView wrapView;

    private T_Phr_CardBindRec mCardInfo;

    @Override
    protected void initViews() {
        setTitle(R.string.label_outpatient_costs);
    }

    private void refreshUpBox(G1910 obj, T_Phr_CardBindRec cardInfo) {
        if (null == obj) {
            return;
        }
        // String dateString = TimeUtils.getTime(obj.getDate(),TimeUtils.DATE_FORMAT_MM);
        if (null != GlobalSettings.INSTANCE.getCurrentFamilyAccount()) {
            patientName.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getName());
        }
        prescribingSections.setText(obj.getPrescDeptName());
        executiveDepartments.setText(obj.getExecDeptName());
        doctorsPrescribing.setText(obj.getDocName());
        prescribingTime.setText(obj.getDate());
        medicalCard.setText((null == cardInfo) ? "" : cardInfo.getCardNo());
        prescriptionAmount.setText(CommonUtils.getFormatFee(obj.getItemFee()));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costs_query_detail);
        ButterKnife.bind(this);

        initData();
        initViews();
        initEvents();

        loadingAsync();
    }

    private void initData() {
        mDataList = new ArrayList<>();

        mG1910Info = (G1910) getIntent().getSerializableExtra(Constants.KEY_BUNDLE);

        refreshUpBox(mG1910Info, null);
    }


    private void removeAutoWrapItem(View view) {
        wrapView.removeView(view);
    }

    private void clearAutoWrapView() {
        wrapView.removeAllViews();
    }

    private void addAutoWrapView(View view) {
        wrapView.addView(view);
    }

    private View creatAutoWrapHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_costs_detail_header, null);
        view.findViewById(R.id.line_bottom).setVisibility(View.GONE);
        return view;
    }

    private View creatAutoWrapItem(boolean isLast, Object obj) {
        if (null == obj) {
            return null;
        }
        G1911 info = (G1911) obj;

        View view = LayoutInflater.from(this).inflate(R.layout.listitem_costs_detail_body, null);
        view.findViewById(R.id.line_bottom).setVisibility(isLast ? View.VISIBLE : View.GONE);

        // double total = info.getItemPrice() * info.getItemQuantity();
        ((TextView) view.findViewById(R.id.txt_project_name)).setText(info.getItemName());
        ((TextView) view.findViewById(R.id.txt_specification)).setText(info.getItemSpec());
        ((TextView) view.findViewById(R.id.txt_unit)).setText(info.getItemUnit());
        ((TextView) view.findViewById(R.id.txt_count)).setText(info.getItemQuantity() + "");
        ((TextView) view.findViewById(R.id.txt_money)).setText(CommonUtils.getFormatFee(info.getItemPrice()));
        return view;
    }

    private void creatAutoWrapList() {
        clearAutoWrapView();
        addAutoWrapView(creatAutoWrapHeader());
        final int size = mDataList.size();
        for (int i = 0; i < size; i++) {
            addAutoWrapView(creatAutoWrapItem(size - 1 == i, mDataList.get(i)));
        }
    }

    private void loadingAsync() {
        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            showLoadingDialog(getString(R.string.common_loading));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDataList = DemoGenerator.getDemoData(new T1911());
                    // refreshUpBox(null);
                    creatAutoWrapList();
                    dismissLoadingDialog();
                }
            }, 500);
        } else {
            loadCardBindAsync();
        }
    }

    private void loadCardBindAsync() {
        T_Phr_BaseInfo curMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        ApiCodeTemplate.loadBindedCardAsync(this, TAG, curMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    // loadGuidePatientsAsync(mCardInfo);
                    loadPayRecordDetailsAsync(null);
                } else {
                    CommonUtils.showNoCardDialog(CostsQueryDetailActivity.this, new INoCardCallback() {

                        @Override
                        public void isBindNow(boolean isBind) {
                            if (isBind) {
                                // openActivityForResult(HosCardOperationActivity.class, REQUEST_CODE_BIND);
                            } else {
                                finish(R.anim.push_right_in, R.anim.push_right_out);
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(CostsQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }

    private void loadGuidePatientsAsync(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadGuidePatientsAsync(this, TAG, null, cardBindRec, new RequestCallback<List<G1615>>() {
            @Override
            public void onRequestSuccess(List<G1615> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    mC1615Info = result.get(0);
                    loadPayRecordDetailsAsync(result.get(0));
                } else {
                    loadPayRecordDetailsAsync(null);
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                // CommonUtils.showError(CostsQueryDetailActivity.this, (Exception) error.getException());
                loadPayRecordDetailsAsync(null);
            }
        });
    }

    private void loadPayRecordDetailsAsync(G1615 result) {
        ApiCodeTemplate.loadPayRecordDetailsAsync(this, TAG, null, mG1910Info, result, new RequestCallback<List<G1911>>() {
            @Override
            public void onRequestSuccess(List<G1911> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    mDataList.addAll(result);
                    refreshUpBox(mG1910Info, mCardInfo);
                    creatAutoWrapList();
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(CostsQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }
}
