package com.gwi.selfplatform.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.pay.common.PayMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by 毅 on 2016-2-19.
 */
public class GWISupportedPayTypeWidget extends FrameLayout {

    @Bind(R.id.pay_type_medical_card_layout)
    View mLayoutMedicalCard;

    @Bind(R.id.pay_type_alipay_layout)
    View mLayoutAlipay;

    @Bind(R.id.pay_type_boc_layout)
    View mLayoutBOC;

    @Bind(R.id.pay_type_ccb_layout)
    View mLayoutCCB;

    @Bind(R.id.pay_type_weixin_layout)
    View mLayoutWeixin;

    @Bind(R.id.pay_type_union_pay_layout)
    View mLayoutUnionPay;

    @Bind(R.id.pay_type_medical_card)
    RadioButton mRbMedicalCard;

    @Bind(R.id.pay_type_alipay)
    RadioButton mRbAliypay;

    @Bind(R.id.pay_type_union_pay)
    RadioButton mRbUnionPay;

    @Bind(R.id.pay_type_weixin)
    RadioButton mRbWeixin;

    @Bind(R.id.pay_type_china_bank)
    RadioButton mRbChinaBank;

    @Bind(R.id.pay_type_construction_bank)
    RadioButton mRbConstructionBank;

    @Bind(R.id.pay_type_medical_card_divider)
    View mDivMedicalCard;

    @Bind(R.id.pay_type_alipay_divider)
    View mDivAlipay;

    @Bind(R.id.pay_type_union_pay_divider)
    View mDivUnionPay;

    @Bind(R.id.pay_type_ccb_divider)
    View mDivCCB;

    @Bind(R.id.pay_type_boc_divider)
    View mDivBOC;

    @Bind(R.id.pay_type_weixin_divider)
    View mDivWeixin;

    @Bind(R.id.pay_type_root)
    View mPayTypeRootView;

    PayTypeSelectedListener mListener;

    public GWISupportedPayTypeWidget(Context context) {
        super(context);
        init();
    }

    public GWISupportedPayTypeWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GWISupportedPayTypeWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GWISupportedPayTypeWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.include_pay_type_v2, null);
        addView(contentView);
        ButterKnife.bind(this);
    }

    public void setSelectedListener(PayTypeSelectedListener listener) {
        mListener = listener;
        //默认
        mRbMedicalCard.setChecked(true);
    }

    public void handleHospitalParams(List<String> payList) {
        PayMode.getInstance().addPayMode(payList);

        if (payList.size() == 0) {
            mPayTypeRootView.setVisibility(View.GONE);
            return;
        }
        //如果只有一种支付方式：不付费，免费
        if (payList.size() == 1 &&
                (payList.contains(Constants.PAY_FOR_FREE) || payList.contains(Constants.PAY_MODE_NONE))) {
            mPayTypeRootView.setVisibility(View.GONE);
            return;
        }
        if (!payList.contains(Constants.PAY_MODE_ALIPAY)) {
            mLayoutAlipay.setVisibility(View.GONE);
            mDivAlipay.setVisibility(View.GONE);
        }
        if (!payList.contains(Constants.PAY_MODE_CARD_MEDICAL)) {
            mLayoutMedicalCard.setVisibility(View.GONE);
            mDivMedicalCard.setVisibility(View.GONE);
        }
        if (!payList.contains(Constants.PAY_MODE_BOC)) {
            mLayoutBOC.setVisibility(View.GONE);
            mDivBOC.setVisibility(View.GONE);
        }
        if (!payList.contains(Constants.PAY_MODE_CCB)) {
            mLayoutCCB.setVisibility(View.GONE);
            mDivCCB.setVisibility(View.GONE);
        }
        if (!payList.contains(Constants.PAY_MODE_WEIXIN)) {
            mLayoutWeixin.setVisibility(View.GONE);
            mDivWeixin.setVisibility(View.GONE);
        }
        if (!payList.contains(Constants.PAY_MODE_UNIONPAY)) {
            mLayoutUnionPay.setVisibility(View.GONE);
            mDivUnionPay.setVisibility(View.GONE);
        }
    }

    @OnCheckedChanged({R.id.pay_type_alipay, R.id.pay_type_medical_card, R.id.pay_type_union_pay,
            R.id.pay_type_china_bank, R.id.pay_type_weixin, R.id.pay_type_construction_bank})
    void onPayTypeChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.pay_type_alipay:
                    mRbMedicalCard.setChecked(false);
                    mRbChinaBank.setChecked(false);
                    mRbUnionPay.setChecked(false);
                    mRbWeixin.setChecked(false);
                    mRbConstructionBank.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_ALIPAY);
                    }
                    break;
                case R.id.pay_type_medical_card:
                    mRbAliypay.setChecked(false);
                    mRbChinaBank.setChecked(false);
                    mRbWeixin.setChecked(false);
                    mRbUnionPay.setChecked(false);
                    mRbConstructionBank.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_CARD_MEDICAL);
                    }
                    break;
                case R.id.pay_type_weixin:
                    mRbAliypay.setChecked(false);
                    mRbUnionPay.setChecked(false);
                    mRbMedicalCard.setChecked(false);
                    mRbChinaBank.setChecked(false);
                    mRbConstructionBank.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_WEIXIN);
                    }
                    break;
                case R.id.pay_type_china_bank:
                    mRbAliypay.setChecked(false);
                    mRbUnionPay.setChecked(false);
                    mRbMedicalCard.setChecked(false);
                    mRbWeixin.setChecked(false);
                    mRbConstructionBank.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_BOC);
                    }
                    break;
                case R.id.pay_type_union_pay:
                    mRbAliypay.setChecked(false);
                    mRbChinaBank.setChecked(false);
                    mRbMedicalCard.setChecked(false);
                    mRbWeixin.setChecked(false);
                    mRbConstructionBank.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_UNIONPAY);
                    }
                    break;
                case R.id.pay_type_construction_bank:
                    mRbAliypay.setChecked(false);
                    mRbChinaBank.setChecked(false);
                    mRbMedicalCard.setChecked(false);
                    mRbWeixin.setChecked(false);
                    mRbUnionPay.setChecked(false);
                    if (mListener != null) {
                        mListener.onPayTypeSelected(Constants.PAY_MODE_CCB);
                    }
                    break;
            }
        }
    }

    public interface PayTypeSelectedListener {
        void onPayTypeSelected(String payMode);
    }
}
