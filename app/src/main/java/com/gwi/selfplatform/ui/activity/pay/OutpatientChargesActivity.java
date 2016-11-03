package com.gwi.selfplatform.ui.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1611;
import com.gwi.selfplatform.module.net.response.G1612;
import com.gwi.selfplatform.module.net.response.RecipeList;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.pay.OutpatientChargeListFragment;
import com.gwi.selfplatform.ui.fragment.pay.OutpatientPayModeSelectFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * 门诊缴费页面
 * @version 2.0
 * @date 2015-12-18
 */
public class OutpatientChargesActivity extends HospBaseActivity {

    public static final String TAG_RECIEP_INFO =  "tag_reciep_info";
    private static final String TAG = OutpatientChargesActivity.class.getSimpleName();
    private T_Phr_BaseInfo mCurMember;
    private ExT_Phr_CardBindRec mCardInfo;
    private G1011 mPatientInfo;
    private List<RecipeList> mRecipeList;

    @Bind(R.id.outpatient_charges_total_fee)
    TextView mTvTotalFee;

    String fee;


    @Override
    protected void initViews() {
        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        mRecipeList = new ArrayList<>();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outpatient_charges);
        EventBus.getDefault().register(this);
        initViews();
        initEvents();
        loadBindedCardAsync();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(G1612 successResult) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadReciepListInfo();
    }

    /**
     * 页面切换事件
     * @param fragment
     */
    public void onEvent(OutpatientChargeListFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.outpatient_charge_content,
                        OutpatientPayModeSelectFragment.newInstance(fee, mRecipeList,
                                mCurMember, mCardInfo, mPatientInfo))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 72) {
            finish();
        }
    }

    private void loadBindedCardAsync() {
        ApiCodeTemplate.loadBindedCardAsync(this, TAG, mCurMember, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (result != null && !result.isEmpty()) {
                    mCardInfo = result.get(0);
                    loadPatientInfo(mCardInfo);
                } else {
                    CommonUtils.showNoCardDialog(OutpatientChargesActivity.this, new INoCardCallback() {

                        @Override
                        public void isBindNow(boolean isBind) {
                            if (isBind) {
                                //TODO:
                                openActivityForResult(MyMedicalCardActivity.class, 1);
                            } else {
                                finish(R.anim.push_right_in, R.anim.push_right_out);
                            }
                        }
                    });
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(OutpatientChargesActivity.this, (Exception) error.getException());
            }
        });
    }

    private void loadPatientInfo(T_Phr_CardBindRec cardBindRec) {
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                mPatientInfo = result;
                loadReciepListInfo();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(OutpatientChargesActivity.this, (Exception) error.getException());
            }
        });
    }

    private void loadReciepListInfo() {
        doCancellableAsyncTask(this, getString(R.string.dialog_content_loading), new AsyncCallback<G1611>() {

            @Override
            public G1611 callAsync() throws Exception {
                return ApiCodeTemplate.getRecipeListAsync(mCardInfo, mPatientInfo);
            }

            @Override
            public void onPostCall(G1611 g1611) {
                if (g1611 != null && !g1611.getRecipeList().isEmpty()) {
                    DecimalFormat format = new DecimalFormat("#.00");
                    fee = format.format(Double.valueOf(g1611.getTotalFee()));
                    mTvTotalFee.setText(CommonUtils.formatCash(Double.valueOf(g1611.getTotalFee()), "元"));
                    mRecipeList.addAll(g1611.getRecipeList());
                }
                initContent();
            }

            @Override
            public void onCallFailed(Exception exception) {
                CommonUtils.showError(OutpatientChargesActivity.this, exception);
                initContent();
            }
        });
    }


    private void initContent() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.outpatient_charge_content, OutpatientChargeListFragment.newInstance(mCurMember, mCardInfo, mPatientInfo, mRecipeList), TAG_RECIEP_INFO)
                .commit();
    }

}
