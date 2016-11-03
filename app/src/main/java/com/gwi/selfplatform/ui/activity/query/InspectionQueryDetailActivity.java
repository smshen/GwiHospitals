package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.gwi.selfplatform.module.net.request.T2511;
import com.gwi.selfplatform.module.net.response.G2510;
import com.gwi.selfplatform.module.net.response.G2511;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InspectionQueryDetailActivity extends HospBaseActivity {
    private static final String TAG = InspectionQueryDetailActivity.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;

    private List<G2511> mG2511List;
    private G2510 mG2510Info;

    @Bind(R.id.patient_name)
    TextView patientName;
    @Bind(R.id.send_department)
    TextView sendDepartment;
    @Bind(R.id.project_name)
    TextView projectName;
    @Bind(R.id.assay_time)
    TextView assayTime;
    @Bind(R.id.assay_departments)
    TextView assayDepartments;
    @Bind(R.id.assay_doctor)
    TextView assayDoctor;
    @Bind(R.id.txt_assay_desp)
    TextView txtAssayDesp;
    @Bind(R.id.txt_assay_result)
    TextView txtAssayResult;
    @Bind(R.id.ly_sample)
    RelativeLayout lySample;


    @Override
    protected void initViews() {
        setTitle((null != mG2510Info) ? mG2510Info.getRepName() : getString(R.string.label_inspection_details));
        lySample.setVisibility(View.GONE);
    }

    private void refreshUpBox(G2510 obj1, G2511 obj2) {
        if (null == obj2) {
            return;
        }

        if (null != GlobalSettings.INSTANCE.getCurrentFamilyAccount()) {
            patientName.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getName());
        }
        sendDepartment.setText(obj2.getRepDeptName());
        projectName.setText(obj2.getRepName());
        assayTime.setText(obj2.getRepTime());
        assayDepartments.setText(obj2.getRepDeptName());
        assayDoctor.setText((null == obj1) ? "" : obj1.getDocName());

        txtAssayDesp.setText(obj2.getDiscription());
        txtAssayResult.setText(obj2.getResult());
    }

    @Override
    protected void initEvents() {

    }

    private void initData() {
        mG2511List = new ArrayList<>();
        mG2510Info = (G2510) getIntent().getSerializableExtra(Constants.KEY_BUNDLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assay_query_detail);
        ButterKnife.bind(this);

        initData();
        initViews();
        initEvents();

        loadingAsync();
    }

    private void loadingAsync() {
        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            showLoadingDialog(getString(R.string.common_loading));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mG2511List = DemoGenerator.getDemoData(new T2511());
                    CommonUtils.removeNull(mG2511List);
                    refreshUpBox(mG2510Info, (null == mG2511List) ? null : mG2511List.get(0));
                    dismissLoadingDialog();
                }
            }, 500);
        } else {
            loadCardBindAsync();
        }
    }

    private void loadCardBindAsync() {
        T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        ApiCodeTemplate.loadBindedCardAsync(this, TAG, member, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                CommonUtils.removeNull(result);
                if (result != null && !result.isEmpty()) {
                    // mCardInfo = result.get(0);
                    getCheckReportAsync(result.get(0));
                } else {
                    CommonUtils.showNoCardDialog(InspectionQueryDetailActivity.this, new INoCardCallback() {

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
                CommonUtils.showError(InspectionQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }

    private void getCheckReportAsync(ExT_Phr_CardBindRec cardInfo) {
        ApiCodeTemplate.getCheckReportDetailAsync2(this, TAG, cardInfo, mG2510Info.getRepNo(), new RequestCallback<G2511>() {
            @Override
            public void onRequestSuccess(G2511 result) {
                if (result != null) {
                    mG2511List.add(result);
                    refreshUpBox(mG2510Info, mG2511List.get(0));
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(InspectionQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }
}
