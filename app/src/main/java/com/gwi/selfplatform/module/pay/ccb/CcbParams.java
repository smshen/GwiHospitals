package com.gwi.selfplatform.module.pay.ccb;

import com.gwi.ccly.android.commonlibrary.common.security.MD5Util;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.phr.hospital.BuildConfig;

/**
 * Created by Administrator on 2016-4-25.
 * 中国建设银行支付实体 商户银行接口参数
 */
public class CcbParams {
    public static final String TAG = "CcbParams";
    /* -------------------------- 普通建行支付所需的参数 ------------------------------- */
    private String merchantid;              // 商户代码
    private String posid;                   // 商户柜台代码
    private String branchid;                // 分行代码
    private String orderid;                 // 定单号
    private double payment;                 // 付款金额
    private String curcode;                 // 币种(缺省为01－人民币)
    private String remark1;                 // 备注1
    private String remark2;                 // 备注2
    private String txcode;                  // 交易码(由建行统一分配为520100)
    private String mac;                     // MAC校验域
    /* -------------------------- 安全建行支付所需的参数 ------------------------------- */
    private String orderno;                 // 订单号，等同于普通支付的orderid
    private String ccbPayInterface;         // 安全支付方式:0.MD5接口1.密钥接口2.防钓鱼接口，这里固定使用2

    private String value;

    public CcbParams(Builder builder) {
        this.merchantid = builder.merchantid;
        this.posid = builder.posid;
        this.branchid = builder.branchid;
        this.orderid = builder.orderid;
        this.payment = builder.payment;
        this.curcode = builder.curcode;
        this.remark1 = builder.remark1;
        this.remark2 = builder.remark2;
        this.txcode = builder.txcode;
        this.ccbPayInterface = builder.ccbPayInterface;

        this.value = builder.toValue();
        this.mac = MD5Util.string2MD5(this.value);
    }

    /**
     * 获取中国银行的支付URL
     *
     * @return
     */
    public String getUrl() {
        String url;
//        if(BuildConfig.IS_CCB_PAY_SAFE){
        //http://pay.xxzrmyy.jsu.edu.cn/CCB/CCBPayFor.aspx
            url = BuildConfig.CCB_PAY_SAFE_URL + "?" + this.value;
//        } else {
//            url = BuildConfig.CCB_PAY_URL + "?" + this.value + "&MAC=" + mac;
//        }
        LogUtil.i(TAG, "getUrl:" + url);
        return url;
    }

    public static class Builder {
        /* 普通建行支付 */
        private String merchantid;              // 商户代码
        private String posid;                   // 商户柜台代码
        private String branchid;                // 分行代码
        private String orderid;                 // 定单号
        private double payment;                 // 付款金额
        private String curcode;                 // 币种(缺省为01－人民币)
        private String remark1;                 // 备注1
        private String remark2;                 // 备注2
        private String txcode;                  // 交易码(由建行统一分配为520100)
        /* 安全建行支付 */
        private String orderno;                 // 订单号，等同于普通支付的orderid
        private String ccbPayInterface;         // 安全支付方式:0.MD5接口1.密钥接口2.防钓鱼接口，这里固定使用2

        public Builder() {
            this.merchantid = CcbConstants.MERCHANTID;
            this.posid = CcbConstants.POSID;
            this.branchid = CcbConstants.BRANCHID;
            this.curcode = CcbConstants.CURCODE;
            this.txcode = CcbConstants.TXCODE;
            this.remark1 = CcbConstants.REMARK1;
            this.remark2 = CcbConstants.REMARK2;
            this.ccbPayInterface = CcbConstants.CCBPAYINTERFACE;
        }

        public Builder setOrderid(String orderid) {
            this.orderid = orderid;
            this.orderno = orderid;
            return this;
        }

        public Builder setPayment(double payment) {
            this.payment = payment;
            return this;
        }

        public Builder setRemark1(String remark1) {
            this.remark1 = remark1;
            return this;
        }

        public Builder setRemark2(String remark2) {
            this.remark2 = remark2;
            return this;
        }

        public String toValue() {
            String paramsValue;
//            if(BuildConfig.IS_CCB_PAY_SAFE){
                // 安全建行支付参数
                paramsValue = "orderno=" + orderno +
                        "&ccbPayInterface=" + ccbPayInterface;
//            } else {
//                // 普通建行支付参数
//                paramsValue = "MERCHANTID=" + merchantid +
//                        "&POSID=" + posid +
//                        "&BRANCHID=" + branchid +
//                        "&ORDERID=" + orderid +
//                        "&PAYMENT=" + payment +
//                        "&CURCODE=" + curcode +
//                        "&TXCODE=" + txcode +
//                        "&REMARK1=" + remark1 +
//                        "&REMARK2=" + remark2;
//            }
            return paramsValue;
        }

        public CcbParams builde() {
            return new CcbParams(this);
        }
    }
}
