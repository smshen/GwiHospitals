package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.interfaces.IHyperlinkText;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.activity.registration.RegistrationSelectV2Activity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;

/**
 * 详细描述页面
 *
 * @author 彭毅
 */
public class DiseaseDetailsActivity extends HospBaseActivity {

    private TextView mTvSummary;
    private TextView mTvDepart;
    private TextView mTvReason;
    private TextView mTvSymptom;
    private TextView mTvTest;
    private TextView mTvDiagnosis;
    private TextView mTvPrevent;
    private TextView mTvSyndrome;
    private TextView mTvTreatment;

    private LinearLayout mLlReason;
    private LinearLayout mLlSymptom;
    private LinearLayout mLlTest;
    private LinearLayout mLlDiagnosis;
    private LinearLayout mLlPrevent;
    private LinearLayout mLlSyndrome;
    private LinearLayout mLlTreatment;

    private TextView mTvReasonTitle;
    private TextView mTvSymptomTitle;
    private TextView mTvTestTitle;
    private TextView mTvDiagnosisTitle;
    private TextView mTvPreventTitle;
    private TextView mTvSyndromeTitle;
    private TextView mTvTreatmentTitle;

    private LinearLayout mLlDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        setTitle(getString(R.string.app_name));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("DiseaseName")) {
                setTitle(b.getString("DiseaseName"));
            }
            if (b.containsKey("DiseaseId")) {
                //DiseaseName
                String diseaseId = b.getString("DiseaseId");
                getDiseaseDetail(diseaseId);
            }
        }

        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mTvSummary = (TextView) findViewById(R.id.disease_detail_summary);
        mTvDepart = (TextView) findViewById(R.id.disease_detail_depart);
        mTvReason = (TextView) findViewById(R.id.disease_details_reason_content);
        mTvSymptom = (TextView) findViewById(R.id.disease_details_symptom_content);
        mTvTest = (TextView) findViewById(R.id.disease_details_test_content);
        mTvDiagnosis = (TextView) findViewById(R.id.disease_details_diagnosis_content);
        mTvPrevent = (TextView) findViewById(R.id.disease_details_prevent_content);
        mTvSyndrome = (TextView) findViewById(R.id.disease_details_syndrome_content);
        mTvTreatment = (TextView) findViewById(R.id.disease_details_treatment_content);

        mLlReason = (LinearLayout) findViewById(R.id.disease_details_reaseon_layout);
        mLlSymptom = (LinearLayout) findViewById(R.id.disease_details_symptom_layout);
        mLlTest = (LinearLayout) findViewById(R.id.disease_details_test_layout);
        mLlDiagnosis = (LinearLayout) findViewById(R.id.disease_details_diagnosis_layout);
        mLlPrevent = (LinearLayout) findViewById(R.id.disease_details_prevent_layout);
        mLlSyndrome = (LinearLayout) findViewById(R.id.disease_details_syndrome_layout);
        mLlTreatment = (LinearLayout) findViewById(R.id.disease_details_treatment_layout);

        mTvReasonTitle = (TextView) findViewById(R.id.disease_details_reason);
        mTvSymptomTitle = (TextView) findViewById(R.id.disease_details_symptom);
        mTvTestTitle = (TextView) findViewById(R.id.disease_details_test);
        mTvDiagnosisTitle = (TextView) findViewById(R.id.disease_details_diagnosis);
        mTvPreventTitle = (TextView) findViewById(R.id.disease_details_prevent);
        mTvSyndromeTitle = (TextView) findViewById(R.id.disease_details_syndrome);
        mTvTreatmentTitle = (TextView) findViewById(R.id.disease_details_treatment);

        mLlDescription = (LinearLayout) findViewById(R.id.disease_details_layout_desc);

        TextView gotoRegister = (TextView) findViewById(R.id.disease_detail_goto_register);
        TextUtil.addUnderlineText(this, gotoRegister, 0, gotoRegister.getText().length(), new IHyperlinkText() {

            @Override
            public void hyperlinkClick() {
                openActivity(RegistrationSelectV2Activity.class);
            }
        });
    }

    @Override
    protected void initEvents() {

    }

    private void getDiseaseDetail(final String diseaseId) {
        doForcableAsyncTask(this, getText(R.string.dialog_content_loading), new AsyncCallback<List<KBDiseaseDetails>>() {

            @Override
            public List<KBDiseaseDetails> callAsync() throws Exception {
                return ApiCodeTemplate.getDiseaseDetailsAsync(diseaseId);
            }

            @Override
            public void onPostCall(List<KBDiseaseDetails> result) {
                if (result != null && !result.isEmpty()) {
                    KBDiseaseDetails details = result.get(0);
                    if (details != null) {
                        mTvSummary.setText(Html.fromHtml(details.getSummary() + ""));
                        mTvSummary.setVisibility(View.VISIBLE);
                        mTvSummary.startAnimation(AnimationUtils.loadAnimation(DiseaseDetailsActivity.this, android.R.anim.fade_in));
                        //TODO:
                        mTvDepart.setText(details.getDeptId());
                        mTvDepart.setVisibility(View.VISIBLE);
                        mTvDepart.startAnimation(AnimationUtils.loadAnimation(DiseaseDetailsActivity.this, android.R.anim.fade_in));
                        mTvReason.setText(Html.fromHtml(details.getDiseaseReason() + ""));
                        mTvSymptom.setText(Html.fromHtml(details.getSymptom() + ""));
                        mTvDiagnosis.setText(Html.fromHtml(details.getDiagnosis() + ""));
                        mTvTest.setText(Html.fromHtml(details.getTest() + ""));
                        mTvPrevent.setText(Html.fromHtml(details.getPrevent() + ""));
                        mTvSyndrome.setText(Html.fromHtml(details.getSymptom() + ""));
                        mTvTreatment.setText(Html.fromHtml(details.getTreatment() + ""));
                        mLlDescription.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(DiseaseDetailsActivity.this, R.anim.fast_slide_in_from_bottom)));
                        mLlDescription.setVisibility(View.VISIBLE);
                        mLlDescription.startLayoutAnimation();
                    }
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e("DiseaseDetailsActivity", "onCallFailed", exception);
                if (exception.getLocalizedMessage() != null) {
                    showToast(exception.getLocalizedMessage());
                } else {
                    showToast(R.string.msg_service_disconnected);
                }
            }
        });
    }


    public void onItemClick(View v) {
        CheckedTextView view = (CheckedTextView) v;
        view.setChecked(!view.isChecked());
        int id = v.getId();
        if (id == R.id.disease_details_reason) {
            if (view.isChecked()) {
                mLlReason.setVisibility(View.GONE);
            } else {
                mLlReason.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_symptom) {
            if (view.isChecked()) {
                mLlSymptom.setVisibility(View.GONE);
            } else {
                mLlSymptom.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_test) {
            if (view.isChecked()) {
                mLlTest.setVisibility(View.GONE);
            } else {
                mLlTest.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_diagnosis) {
            if (view.isChecked()) {
                mLlDiagnosis.setVisibility(View.GONE);
            } else {
                mLlDiagnosis.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_prevent) {
            if (view.isChecked()) {
                mLlPrevent.setVisibility(View.GONE);
            } else {
                mLlPrevent.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_syndrome) {
            if (view.isChecked()) {
                mLlSyndrome.setVisibility(View.GONE);
            } else {
                mLlSyndrome.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.disease_details_treatment) {
            if (view.isChecked()) {
                mLlTreatment.setVisibility(View.GONE);
            } else {
                mLlTreatment.setVisibility(View.VISIBLE);
            }
        }
    }

}
