package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1310;
import com.gwi.selfplatform.module.net.response.G1510;
import com.gwi.selfplatform.module.net.response.G1512;
import com.gwi.selfplatform.module.net.response.GRegistRecordBase;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.fragment.query.RegistRecordFragment;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 挂号查询详细页面
 *
 * @version 2.0
 * @date 2016-1-13
 */
public class RegisterQueryDetailActivity extends HospBaseActivity {

    private static final String TAG = "RegisterQueryDetailActivity";
    @Bind(R.id.register_query_detail_card_no)
    TextView mTvCardNo;

    @Bind(R.id.register_query_detail_dct_name_rank_name)
    TextView mTvDctNameAndRankName;

    @Bind(R.id.register_query_detail_dept_name)
    TextView mTvDeptName;

    @Bind(R.id.register_query_detail_fee)
    TextView mTvFee;

    @Bind(R.id.register_query_detail_identity_layout)
    TableRow mIdentityLayout;

    @Bind(R.id.register_query_detail_identity)
    TextView mTvIdentity;

    @Bind(R.id.register_query_detail_date)
    TextView mTvDate;

    @Bind(R.id.register_query_detail_patient_name)
    TextView mTvPatientName;

    @Bind(R.id.query_detail_content_layout)
    View mLayoutContent;

    @Bind(R.id.register_query_position)
    TextView mTvPostion;

    @Bind(R.id.register_query_detail_status)
    CheckedTextView mCtvStatus;
    @Bind(R.id.btn_order_cancel)
    Button mBtnCancel;
    @Bind(R.id.register_query_detail_hint)
    TextView mTvHint;

    @OnClick(R.id.btn_order_cancel)
    void onCancel() {
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_title_prompt)
                .content("是否取消本次预约？")
                .cancelable(false)
                .positiveText("是的")
                .negativeText("不了")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                        loadPatientInfoAsync();
                    }
                }).show();
    }


    T_Phr_BaseInfo mCurMember;
    ExT_Phr_CardBindRec mCardInfo;
    GRegistRecordBase mRecord;

    @Override
    protected void initViews() {
        int height = AbViewUtil.scaleValue(this, 619);
        mLayoutContent.getLayoutParams().height = height;

        mCurMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        mCardInfo = (ExT_Phr_CardBindRec) getIntent().getSerializableExtra(RegistRecordFragment.KEY_CARD_INFO);
        mRecord = (GRegistRecordBase) getIntent().getSerializableExtra(RegistRecordFragment.KEY_RECORD);
        mTvPatientName.setText(mCurMember.getName());
        mTvCardNo.setText(mCardInfo.getCardNo());

        if (mRecord instanceof G1310) {
            setTitle("挂号详细");
            G1310 registRecord = (G1310) mRecord;
            String docName = registRecord.getDoctName();
            if (TextUtil.isEmpty(docName)) {
                docName = "";
            }
            mTvDctNameAndRankName.setText(String.format("%s  %s", docName, registRecord.getRankName()));
            mTvFee.setText(CommonUtils.formatCash(registRecord.getTotalRegFee(), "元"));
            mTvDeptName.setText(registRecord.getDeptName());
            mTvDate.setText(registRecord.getRegDate());

            mCtvStatus.setVisibility(View.GONE);
            mBtnCancel.setVisibility(View.GONE);
            /* bug2135 LiuTao 20160803 add */
            mIdentityLayout.setVisibility(View.GONE);
            mTvHint.setText("温馨提示：请携带您的诊疗卡提前1小时到诊区就诊.");
            mTvPostion.setText((registRecord).getTreatLocation());
        } else {
            setTitle("预约详细");
            G1510 orderRecord = (G1510) mRecord;
            String docName = orderRecord.getDoctName();
            if (TextUtil.isEmpty(docName)) {
                docName = "";
            }
            mTvDctNameAndRankName.setText(String.format("%s  %s", docName, orderRecord.getRankName()));
            mTvFee.setText(CommonUtils.formatCash(orderRecord.getTotalRegFee(), "元"));
            mTvDeptName.setText(orderRecord.getDeptName());
            mTvDate.setText(orderRecord.getOrderDate());
            mTvIdentity.setText(orderRecord.getOrderIdentity());

            mCtvStatus.getLayoutParams().width = AbViewUtil.scaleValue(this, 170);
            mCtvStatus.getLayoutParams().height = AbViewUtil.scaleValue(this, 214);
            mTvHint.setText("温馨提示：请携带您的诊疗卡提前1小时到诊区就诊，针对未取号的预约记录单可以取消.");
            mBtnCancel.setEnabled(orderRecord.getOrderState() == 1);
            if (orderRecord.getOrderState() == 1) {
                mCtvStatus.setChecked(false);
            } else if (orderRecord.getOrderState() == 2) {
                mCtvStatus.setChecked(true);
            } else {
                mCtvStatus.setVisibility(View.INVISIBLE);
            }
            mTvPostion.setText(orderRecord.getTreatLocation());
        }

        mTvHint.setText(TextUtil.colorText(mTvHint.getText().toString(),
                getResources().getColor(R.color.colorPrimary), 0, 5));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_query_detail);
        addHomeButton();
        initViews();
        initEvents();

    }

    private void loadPatientInfoAsync() {
        ApiCodeTemplate.loadPatientInfoAsync(this, TAG, mCardInfo, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                doCancelAsync(result);
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegisterQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }

    private void doCancelAsync(G1011 patientInfo) {
        ApiCodeTemplate.doOrderRegistCancelAsync(this, TAG, null, patientInfo, mCardInfo, (G1510) mRecord, new RequestCallback<G1512>() {
            @Override
            public void onRequestSuccess(G1512 result) {
                showToast("取消成功!");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(RegisterQueryDetailActivity.this, (Exception) error.getException());
            }
        });
    }
}
