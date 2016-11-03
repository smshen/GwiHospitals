/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.gwi.selfplatform.module.pay.zhifubao;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	/*public static final List<String> mRecipeParams =  HospitalParams.getFields(GlobalSettings.INSTANCE.handleHospitalParams()
            .get(HospitalParams.CODE_ALIPAY_PARAMS));
	public static final String DEFAULT_PARTNER = mRecipeParams.get(0);
	public static final String DEFAULT_SELLER = mRecipeParams.get(1);
	public static final String PRIVATE = mRecipeParams.get(2);
	public static final String PUBLIC = mRecipeParams.get(3);*/

    //合作身份者id，以2088开头的16位纯数字
    //public static final String DEFAULT_PARTNER = "2088611734008240";

    //收款支付宝账号
    //public static final String DEFAULT_SELLER = "2088611734008240";

    //商户私钥，自助生成，在压缩包中有openssl，用此软件生成商户的公钥和私钥，写到此处要不然服务器返回错误。我忘记了不是私钥就是公钥要传到淘宝合作账户里详情请看淘宝的sdk文档
    //public static final String PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALWwesbAHToZgAK9pMDJMTc8RbCz40N+rZcBCyKL4nWJHx+iOCFIEai8IjcjR5UuflubMaed1685skejqRA9vgYdfWR/3w03eS7M1mceMkynWDvReCfdTgLglb154V3bbeeh2Rp5h87kVZ6V+VaFgrGSJbIjELceeqGUV0yz3RM9AgMBAAECgYEAiGPDtuFOz2eHFgeg3uvpWyqOsjhH2UoGK74nyrYxuMPHrmqLMki0oPMhyDxepxlw13moC8jMuYhZUbgE2OFQfGgujYYlS+SbZ+UVAGX12yH263erxikENwWv+XXVYo22PSzA0RWnIX+mDLrxRRKHWTBi3ziAGbtY4XQGsiRJR4ECQQDmnLx9zciOHhxifWGtDI4HO/HH+o6zgXl4W7j2iXadg4cynM1Lf657+tapcEo44a6oYKPLQKvbwgyRjEsl7QehAkEAybD3k9IDb3A/cyRuder0R8ZBoAH/sLcVDPYUPT80g6z4T6Zh3eP0ruWEPGj+dXmLNNi2QqCUveblFtDH+h92HQJAYspkKRR9xPv7Nun1bPeC5HB2jC+28AkYK0pHuz16FdIhmbn4bE5fRfdX/DABa8qqUrFLmaezt6myTqumCbI34QJBAMWZIkVpWMJB93fINoxCP0deXgb0TlwEUpRbNmL23G0w9imauYEZhyweYpqIxtuy04ZX1uCZ0H/oiYL70yAi2KUCQQCyqHM7C4pv6YEBrJH4payQ6OEV4bt/gvOhaeeSMSiPGFGteFlsFjHCHPAEw1Ir4A+lSc61qoqCbwaHaUmlqeT+";
    //公钥
    //public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1sHrGwB06GYACvaTAyTE3PEWws+NDfq2XAQsii+J1iR8fojghSBGovCI3I0eVLn5bmzGnndevObJHo6kQPb4GHX1kf98NN3kuzNZnHjJMp1g70Xgn3U4C4JW9eeFd223nodkaeYfO5FWelflWhYKxkiWyIxC3HnqhlFdMs90TPQIDAQAB";

    //中心医院
    public static String DEFAULT_PARTNER;
    //			"2088711166413031";
    public static String DEFAULT_SELLER;
    //	= "2088711166413031";
    public static String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMA53LjwRjLMZggNQmIr7AOIds2bDweOeDxm8nZj3TlqasolAiH/X8Z/D8zaJFjjXCX4Q16NBvhfsDloeEl0U+bzve9HdtqACrYWxTcsCqnv/4rYbR04oupo4jC18u8w6BLUZkzWMbjpt0EmXadqSp16pq4t5TOQUVzGIpSflKwpAgMBAAECgYANs1IPAFkQf8753fAQTS8aJVzKVLXihP3+2BuJ4p4+3F3WIIMhZzEtVZsLr7SNfU7xCdvLXmPTygYmYVLJcXgVSyrJUfC/BGH29Lxk5S4JMxDu/MpAeEtuc7bEIB4si4QlCNdT6DVT6sR/It7803zUyaO/yBlcz5pG0Jvv7OsWcQJBAPBfIj4ctPeqnWzY6Lpw5Cc4gcCx795foDqh6nmrhT9yNtySyFgTcOnCGEDrOBkriMcs/YA8Mb5z0q4wS1yPbd0CQQDMuV5z1kj8PJ4+NahLZ4ydoz59OB8EqTA/1Pj3CtEM/Xl7ZWzzjiJJtEZAlUDetGmuBSYi8Ap7KDer08jb0dC9AkAlhVQdCfIdrt4//WPmpnSWwrVOVPgyaA7k2B13vJRiIPv7VlY7mV1Iv+l1SMJK+4awQI+ZksvZBM9gLV22JxVpAkEAymGZuOOmJQx71r3OkDRectuVA86+AyqKEyAjunxL6w6M0GqUyTTCOqBnLqMdjMtHCYv3hmRLyDIaztc/YedJ9QJAK8yvMxBKp7jWpmJh55MM4CLUw33fd0r03QhnouXq/+thEHn5VKybwYBJdbGxPEs/CFdnnlpqTgKUaq41zIdJJQ==";
    public static String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAOdy48EYyzGYIDUJiK+wDiHbNmw8Hjng8ZvJ2Y905amrKJQIh/1/Gfw/M2iRY41wl+ENejQb4X7A5aHhJdFPm873vR3bagAq2FsU3LAqp7/+K2G0dOKLqaOIwtfLvMOgS1GZM1jG46bdBJl2nakqdeqauLeUzkFFcxiKUn5SsKQIDAQAB";

}
