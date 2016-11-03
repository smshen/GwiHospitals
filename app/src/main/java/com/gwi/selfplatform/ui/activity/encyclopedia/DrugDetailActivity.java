package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.PubUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.module.net.beans.KBDrugDetails;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;

public class DrugDetailActivity extends HospBaseActivity {

    private static final String TAG = DrugDetailActivity.class.getSimpleName();

    private TextView drugName;
    private TextView drugType;
    private TextView drugElement;
    private TextView dosage;
    private TextView indications;
    private TextView contraindication;
    private TextView notice;
    private TextView ADR;

    private TextView indicationsContent;
    private TextView contraindicationContent;
    private TextView noticeContent;
    private TextView ADRContent;
    
    private View indications_layout;
    private View contraindication_layout;
    private View notice_layout;
    private View ADR_layout;
    
    private List<KBDrugDetails> mDrugList = null;
    private String drugId;
    private String drugDetailName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        drugId = intent.getStringExtra("DrugId");
        drugDetailName = intent.getStringExtra("DrugName");
        setTitle(drugDetailName);
        
        setContentView(R.layout.fragment_drug_detail);
        initViews();
        initEvents();
        addHomeButton();
       /* 
        getSupportFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, new DrugDetailFragment(drugId),"Body Part")
        .commit();
        addHomeButton();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        indications_layout = findViewById(R.id.indications_layout);
        contraindication_layout = findViewById(R.id.contraindication_layout);
        notice_layout = findViewById(R.id.notice_layout);
        ADR_layout = findViewById(R.id.ADR_layout);
        
        drugName = (TextView)findViewById(R.id.drugName);
        drugType = (TextView)findViewById(R.id.drugType);
        drugElement = (TextView)findViewById(R.id.drugElement);
        dosage = (TextView)findViewById(R.id.dosage);
        indications = (TextView)findViewById(R.id.indications);
        contraindication = (TextView)findViewById(R.id.contraindication);
        notice = (TextView)findViewById(R.id.notice);
        ADR = (TextView)findViewById(R.id.ADR);
        
        indicationsContent = (TextView)findViewById(R.id.indications_content);
        contraindicationContent = (TextView)findViewById(R.id.contraindication_content);
        noticeContent = (TextView)findViewById(R.id.notice_content);
        ADRContent = (TextView)findViewById(R.id.ADR_content);

        getDrugDetailNew(drugId);
    }
    
    public void onClick(View v) {
           CheckedTextView checkTV = (CheckedTextView)v;
           checkTV.setChecked(!checkTV.isChecked());
           int id = v.getId();
        if (id == R.id.indications) {
            if(checkTV.isChecked()){
                   indications_layout.setVisibility(View.VISIBLE);
               }else{
                   indications_layout.setVisibility(View.GONE);
               }
        } else if (id == R.id.contraindication) {
            if(checkTV.isChecked()){
                   contraindication_layout.setVisibility(View.VISIBLE);
               }else{
                   contraindication_layout.setVisibility(View.GONE);
               }
        } else if (id == R.id.notice) {
            if(checkTV.isChecked()){
                   notice_layout.setVisibility(View.VISIBLE);
               }else{
                   notice_layout.setVisibility(View.GONE);
               }
        } else if (id == R.id.ADR) {
            if(checkTV.isChecked()){
                   ADR_layout.setVisibility(View.VISIBLE);
               }else{
                   ADR_layout.setVisibility(View.GONE);
               }
        }
       }
    
    private void getDrugDetailNew(String drugId) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_DETAIL, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
        request.getBody().setDrugId(drugId);

        GWINet.connect().createRequest().postGWI(null,request).fromGWI()
                .setLoadingMessage(getString(R.string.dialog_content_loading))
                .showLoadingDlg(this, true)
                .mappingInto(new TypeToken<List<KBDrugDetails>>() {
                })
                .execute(TAG, new RequestCallback<List<KBDrugDetails>>() {
                    @Override
                    public void onRequestSuccess(List<KBDrugDetails> result) {
                        if (result != null && !result.isEmpty()) {
                            KBDrugDetails details = result.get(0);
                            if (!TextUtils.isEmpty(details.getIndications())) {
                                indicationsContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getIndications().replace("\\n", "")).toString()));
                            }
                            if (!TextUtils.isEmpty(details.getContraindication())) {
                                contraindicationContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getContraindication().replace("\\n", "")).toString()));
                            }
                            if (!TextUtils.isEmpty(details.getNotice())) {
                                noticeContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getNotice().replace("\\n", "")).toString()));
                            }
                            if (!TextUtils.isEmpty(details.getADR())) {
                                ADRContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getADR().replace("\\n", "")).toString()));
                            }

                            if (!TextUtils.isEmpty(details.getDrugName())) {
                                drugName.setText(Html.fromHtml(details.getDrugName()));
                            }
                            if (!TextUtils.isEmpty(details.getDrugKind())) {
                                drugType.setText(Html.fromHtml(details.getDrugKind()));
                            }
                            if (!TextUtils.isEmpty(details.getDrugElement())) {
                                drugElement.setText(Html.fromHtml(details.getDrugElement()));
                            }
                            if (!TextUtils.isEmpty(details.getDosage())) {
                                dosage.setText(PubUtil.replaceBlank(Html.fromHtml(details.getDosage().replace("\\n", "")).toString()));
                            }
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(DrugDetailActivity.this, (Exception) error.getException());
                    }
                });
    }
    
     private void getDrugDetail(final String drugId) {
            doForcableAsyncTask(this, getText(R.string.dialog_content_loading), new AsyncCallback<List<KBDrugDetails>>() {

                @Override
                public List<KBDrugDetails> callAsync() throws Exception {
                    return WebServiceController.getDrugDetail(drugId);
                }

                @Override
                public void onPostCall(List<KBDrugDetails> result) {
                    if(result!=null && !result.isEmpty()) {
                        KBDrugDetails details = result.get(0);
                        if(!TextUtils.isEmpty(details.getIndications())){
                            indicationsContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getIndications().replace("\\n", "")).toString()));
                        }
                        if(!TextUtils.isEmpty(details.getContraindication())){
                            contraindicationContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getContraindication().replace("\\n", "")).toString()));
                        }
                        if(!TextUtils.isEmpty(details.getNotice())){
                            noticeContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getNotice().replace("\\n", "")).toString()));
                        }
                        if(!TextUtils.isEmpty(details.getADR())){
                            ADRContent.setText(PubUtil.replaceBlank(Html.fromHtml(details.getADR().replace("\\n", "")).toString()));
                        }
                        
                        if(!TextUtils.isEmpty(details.getDrugName())){
                            drugName.setText(Html.fromHtml(details.getDrugName()));
                        }
                        if(!TextUtils.isEmpty(details.getDrugKind())){
                            drugType.setText(Html.fromHtml(details.getDrugKind()));
                        }
                        if(!TextUtils.isEmpty(details.getDrugElement())){
                            drugElement.setText(Html.fromHtml(details.getDrugElement()));
                        }
                        if(!TextUtils.isEmpty(details.getDosage())){
                            dosage.setText(PubUtil.replaceBlank(Html.fromHtml(details.getDosage().replace("\\n", "")).toString()));
                        }
                    }
                }

                @Override
                public void onCallFailed(Exception exception) {
                    Logger.e("DrugDetailActivity", "onCallFailed", exception);
                    if(exception.getLocalizedMessage()!=null) {
                        showToast(exception.getLocalizedMessage());
                    }else {
                        showToast(R.string.msg_service_disconnected);
                    }
                }
            });
        }

    @Override
    protected void initEvents() {

    }

}
