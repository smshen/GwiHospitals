package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.request.T1615;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1615;
import com.gwi.selfplatform.ui.adapter.GuideExpandAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class GuideQueryActivity extends HospBaseActivity {
    private static final String TAG = GuideQueryActivity.class.getSimpleName();
    private static final int REQUEST_CODE_BIND = 0x007;

    // private List<G1211> mDeptsList = null;

    @Bind(android.R.id.list)
    ExpandableListView mELVDeptsList;

    @Bind(R.id.text_empty)
    TextView mTvEmpty;

    private GuideExpandAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_expand_list);
        ButterKnife.bind(this);
        initViews();
        initEvents();

        loadRegistDeptsAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dimisssProgressDialog();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initViews() {
        setTitle(R.string.label_guide_query);
    }

    private void handleEmptyText() {
        if (mAdapter.getGroupCount() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
        } else {
            mTvEmpty.setVisibility(View.GONE);
        }
    }

    private MaterialDialog mMDDialog;

    private void showProgressDialog() {
        mMDDialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_title_prompt)
                .content(R.string.dialog_content_loading)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        mMDDialog.setCanceledOnTouchOutside(false);
    }

    private void dimisssProgressDialog() {
        if (mMDDialog != null) {
            mMDDialog.dismiss();
        }
    }

    private void loadRegistDeptsAsync() {
        loadCardBindingNewAsync(GlobalSettings.INSTANCE.getCurrentFamilyAccount());
    }

    private void expandListView() {
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mELVDeptsList.expandGroup(i);
        }
    }

    private void loadCardBindingNewAsync(final T_Phr_BaseInfo member) {
        if (null == member) {
            return;
        }
        ApiCodeTemplate.loadBindedCardAsync(this, null, TAG, member, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    for (ExT_Phr_CardBindRec cardInfo : result) {
                        if (cardInfo != null) {
                            loadPatientInfo(cardInfo);
                            break;
                        }
                    }
                } else {
                    CommonUtils.showNoCardDialog(GuideQueryActivity.this, new INoCardCallback() {

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
                CommonUtils.showError(GuideQueryActivity.this, (Exception) error.getException());
                // mHandler.sendEmptyMessageDelayed(Constants.MSG_SHOULD_FINISH, Constants.MILLIMS_DELAY_BACK);
            }
        });
    }

    private void loadPatientInfo(final ExT_Phr_CardBindRec cardInfo) {
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, null, cardInfo, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                getCheckNavListNewAsync(cardInfo, result);
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    private void getCheckNavListNewAsync(final ExT_Phr_CardBindRec cardInfo, G1011 patientInfo) {
        Request<T1615> request = new Request<>();
        Request.commonHeader(request, 1615, false);

        if (!ApiCodeTemplate.checkMedicalCardLogout(GuideQueryActivity.this, patientInfo,
                GlobalSettings.INSTANCE.getCurrentFamilyAccount(), null)) {
            return;
        }

        request.setBody(new T1615());
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(null)
                .mappingInto(new TypeToken<List<G1615>>() {
                })
                .execute(TAG, new RequestCallback<List<G1615>>() {
                    @Override
                    public void onRequestSuccess(List<G1615> result) {
                        CommonUtils.removeNull(result);
                        if (result != null && !result.isEmpty()) {
                            // for test
                            // creatFakeData(result);

                            // mDeptsList = DemoGenerator.getDemoData(request);
                            mAdapter = new GuideExpandAdapter(GuideQueryActivity.this, result);
                            mELVDeptsList.setAdapter(mAdapter);
                            expandListView();
                        } else {
                            showToast(R.string.msg_no_record);
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        CommonUtils.showError(GuideQueryActivity.this, (Exception) error.getException());
                    }
                });
    }

    private void creatFakeData(List<G1615> result) {
        for (int i = 0; i < result.size(); i++) {
            if (i % 3 == 0) {
                result.get(i).setExecDeptID("1");
            } else if (i % 3 == 1) {
                result.get(i).setExecDeptID("2");
            } else if (i % 3 == 2) {
                result.get(i).setExecDeptID("3");
            }
        }
    }
}
