package com.gwi.selfplatform.module.pay.ccb;

/**
 * Created by Administrator on 2016-4-25.
 */
public class CcbConstants {
    /* 普通建行支付 */
    public static final String MERCHANTID = "105433180620030";        // 商户代码
    public static final String POSID = "890849808";                   // 商户柜台代码
    public static final String BRANCHID = "430000000";                // 分行代码
    public static final String CURCODE = "01";                        // 币种(缺省为01－人民币)
    public static final String TXCODE = "520100";                     // 交易码(由建行统一分配为520100)
    public static final String REMARK1 = "ccb";
    public static final String REMARK2 = "gwi";
    /* 安全建行支付 */
    public static final String CCBPAYINTERFACE = "2";                 // 安全支付方式:0.MD5接口1.密钥接口2.防钓鱼接口，这里固定使用2
}
