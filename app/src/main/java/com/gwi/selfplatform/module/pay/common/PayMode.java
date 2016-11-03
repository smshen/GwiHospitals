package com.gwi.selfplatform.module.pay.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseActivity;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.pay.ccb.CcbParams;
import com.gwi.selfplatform.module.pay.wechat.WXPayActivity;
import com.gwi.selfplatform.module.pay.wechat.WXUtil;
import com.gwi.selfplatform.module.pay.zhifubao.AliPayActivity;
import com.gwi.selfplatform.module.pay.zhifubao.Product;
import com.gwi.selfplatform.ui.base.WebActivity;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class PayMode {
    private static PayMode ourInstance = new PayMode();

    public static PayMode getInstance() {
        return ourInstance;
    }

    private PayMode() {
    }

    public void pay(BaseActivity activity, String mode, Bundle bundle) {
//        if (!isWXPaySupport()) {
//            activity.openActivity(ExternalPartner.class, bundle);
//            return;
//        }

        switch (mode) {
            case Constants.PAY_MODE_WEIXIN:
                activity.openActivity(WXPayActivity.class, bundle);
                break;
            case Constants.PAY_MODE_ALIPAY:
                activity.openActivity(AliPayActivity.class, bundle);
                break;
            case Constants.PAY_MODE_CCB:
                // 防钓鱼建行支付
                CcbParams params = new CcbParams.Builder()
                        .setOrderid(bundle.getString("OrderNo"))
                        .builde();
                Bundle ccb = new Bundle();
                ccb.putString(WebActivity.KEY_URL, params.getUrl());
                ccb.putString(WebActivity.KEY_TITLE, "中国建设银行");
                activity.openActivity(WebActivity.class, ccb);

                // 普通建行支付
//                Product product = (Product) bundle.getSerializable("Product");
//                DecimalFormat format = new DecimalFormat("#.##");
//                String fee = format.format(Double.valueOf(product.getPrice()));
//                double payment = Double.valueOf(fee);
//
//                CcbParams params = new CcbParams.Builder()
//                        .setOrderid(bundle.getString("OrderNo"))
//                        .setPayment(payment)
//                        .builde();
//
//                Bundle ccb = new Bundle();
//                ccb.putString(WebActivity.KEY_URL, params.getUrl());
//                ccb.putString(WebActivity.KEY_TITLE, "中国建设银行");
//                activity.openActivity(WebActivity.class, ccb);
                break;
            default:
                break;
        }
    }

    public boolean isWXPaySupport() {
        return BuildConfig.IS_WX_PAY;
    }

    public boolean isCCBPaySupport() {
        return BuildConfig.IS_CCB_PAY;
    }

    public boolean isPayModeSupport(String payMode) {
        if (Constants.PAY_MODE_ALIPAY.equalsIgnoreCase(payMode)
                || Constants.PAY_MODE_WEIXIN.equalsIgnoreCase(payMode)
                || Constants.PAY_MODE_CCB.equalsIgnoreCase(payMode)) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public void addWXPayMode(List<String> source) {
        if (isWXPaySupport()) {
            if (null != source) {
                source.add(Constants.PAY_MODE_WEIXIN);
            }
        }
    }

    public void addPayMode(List<String> payList) {
        //不应该这样做的，应该是在后台配置
//        if (null != payList) {
//            // 增加微信支付模式
//            if (isWXPaySupport()) {
//                payList.add(Constants.PAY_MODE_WEIXIN);
//            }
//            // 增加建设银行支付模式
//            if (isCCBPaySupport()) {
//                payList.add(Constants.PAY_MODE_CCB);
//            }
//        }
    }

    /**
     * 测试中国建设银行支付功能
     *
     * @param context
     */
    public void createFakeCCBPay(Context context) {
        CcbParams params = new CcbParams.Builder()
                //.setOrderid("59416")
                .setOrderid(WXUtil.getUUID())
                .setPayment(0.01)
                .builde();
        Bundle ccb = new Bundle();
        ccb.putString(WebActivity.KEY_URL, params.getUrl());
        // ccb.putString(WebActivity.KEY_URL, "https://www.baidu.com/");
        ccb.putString(WebActivity.KEY_TITLE, "中国建设银行");
        // PayMode.getInstance().pay((BaseActivity) context, Constants.PAY_MODE_CCB, ccb);
        // ((BaseActivity) context).openActivity(WebActivity.class, ccb);

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtras(ccb);
        context.startActivity(intent);
    }
}
