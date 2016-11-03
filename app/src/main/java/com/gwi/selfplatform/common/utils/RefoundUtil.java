package com.gwi.selfplatform.common.utils;

public class RefoundUtil {

    //type表示业务类型,挂号、充值、缴费、预约挂号
    public static String getOrderStatus(String businessType, String payResult, String hisResult, String businessStatus,String refoundResult) {
        String result = null;
        if (businessType.equalsIgnoreCase(BankUtil.BusinessType_Rgt_Order)) {//预约挂号
            if (refoundResult != null) {
                result = getRefoundStr(payResult, hisResult, businessStatus, refoundResult);
            } else {
                result = getRegistPhrStr2(payResult, hisResult, businessStatus, refoundResult);
            }
        } else if (BankUtil.BusinessType_Rgt.equals(businessType)) {//挂号
            result = getRegistrationStr(payResult, hisResult, businessStatus, refoundResult);
        } else if (BankUtil.BusinessType_CARD_CHARGE.equals(businessType)) {//充值
            result = getRechargeStr(payResult, hisResult);
        } else if (BankUtil.BusinessType_Recipe.equals(businessType)) {//缴费
            result = getPaymentStr(payResult, hisResult, businessStatus, refoundResult);
        } else if (BankUtil.BusinessType_Refound.equals(businessType)) {//取消预约
            result = getRefoundStr(payResult, hisResult, businessStatus, refoundResult);
        }
        return result;
    }

    /*预约挂号：
     * 自动冲退提示：10xx：表示预交金充值未执行，提示已经付款，后台业务正在办理中......
     * 110x表示支付成功，预交金充值成功，提示后台业务正在办理中......
     * 111x表示业务办理成功！
     * 112x表示支付成功，预交金充值成功，提示业务办理失败，钱款已经退到您的诊疗卡中，请您用预交金支付重试或者到我院人工窗口办理预交金退款.......
     * 12x0表示支付成功，预交金充值失败，提示系统正在将资金退回到您的支付宝账户中......
     * 12x1表示支付成功，预交金充值失败，提示系统已经将资金成功退入到您的支付宝账户中……
     * 12x2表示支付成功，预交金充值失败，提示系统无法将资金退入您的支付宝账户中，请于三个工作日后携带身份证及诊疗卡到我院财务科退费......
     */
    StringBuilder builder = new StringBuilder();

    public static String getRegistPhrStr(String payResult, String hisResult, String orderStatus, String refoundResult) {
        String phr = null;
        if (payResult.equals("1")) {
            if (hisResult.equals("0")) {
                return "10xx";
            } else if (hisResult.equals("1")) {
                if (orderStatus.equals("0")) {
                    return "110x";
                } else if (orderStatus.equals("1")) {
                    return "111x";
                } else if (orderStatus.equals("2")) {
                    return "112x";
                } else if (orderStatus.equals("3")) {
                    return "113x";
                }
            } else if (hisResult.equals("2")) {
                if (refoundResult.equals("0")) {
                    return "1200";
                } else if (refoundResult.equals("1")) {
                    return "1201";
                } else if (refoundResult.equals("2")) {
                    return "1202";
                } else if (refoundResult.equals("3")) {
                    return "1203";
                }
            } else if (hisResult.equals("3")) {
                return "13xx";
            }
        } else {
            //表示未支付
            phr = "0x";
        }
        return phr;
    }


    public static String getRegistPhrStr2(String payResult, String hisResult, String business, String refoundResult) {
        String phr = null;
        if (payResult.equals("1")) {
            if (hisResult.equals("0") && business.equals("0")) {
                return "100x";
            } else if (hisResult.equals("1") && business.equals("1")) {
                return "111x";
            } else if (hisResult.equals("2") && business.equals("2")) {
                if (refoundResult != null) {
                    if (refoundResult.equals("0")) {
                        return "1220";
                    } else if (refoundResult.equals("1")) {
                        return "1221";
                    } else if (refoundResult.equals("2")) {
                        return "1222";
                    } else if (refoundResult.equals("3")) {
                        return "1223";
                    }
                }
            } else if (hisResult.equals("3") && business.equals("3")) {
                return "133x";
            }
        } else {
            //表示未支付
            phr = "0x";
        }
        return phr;
    }

    /*
     * 取消预约提示：1110提示您已成功取消该业务，系统正在将资金退回到您的诊疗卡中......
     * 1111提示您已成功取消该业务，钱款已经退到您的诊疗卡中，如需退款请您到我院人工窗口办理预交金退款.......
     * 1112提示您已成功取消该业务，但系统无法将钱款已经退回到您的诊疗卡中，请于三个工作日后携带身份证及诊疗卡到我院财务科退费......
     * 1113同1112
     */
    public static String getRefoundStr(String payResult, String hisResult,String businessStatus, String refoundStatus) {
        StringBuilder builder = new StringBuilder();
        builder.append(payResult);
        builder.append(businessStatus);
        builder.append(hisResult);
        if (refoundStatus != null) {
            builder.append(refoundStatus);
        }
        return builder.toString();
    }


    /*
     * 自动冲退提示：10xx：表示预交金充值未执行，提示已经付款，后台业务正在办理中......
     * 110x表示支付成功，预交金充值成功，提示后台业务正在办理中......
     * 111x表示业务办理成功！
     * 112x表示支付成功，预交金充值成功，提示业务办理失败，钱款已经退到您的诊疗卡中，请您用预交金支付重试或者到我院人工窗口办理预交金退款.......
     * 12xx支付成功，预交金充值失败，提示请于三个工作日后携带身份证及诊疗卡到我院财务科退费......
     */
    @Deprecated
    public static String getPhrStr(String payResult, String hisResult, String orderStatus, String refoundResult) {
        StringBuilder builder = new StringBuilder();
        String phr = null;
        if (payResult.equals("1")) {
            if (hisResult.equals("0")) {
                return "10xx";
            } else if (hisResult.equals("1")) {
                if (orderStatus.equals("0")) {
                    return "110x";
                } else if (orderStatus.equals("1")) {
                    return "111x";
                } else if (orderStatus.equals("2") || orderStatus.equals("3")) {
                    return "112x";
                }
            } else if (hisResult.equals("2") || hisResult.equals("3")) {
                return "12xx";
            }
        } else {
            //表示未支付
            phr = "0x";
        }
        return phr;
    }

    /**
     * 充值业务,代码转换
     *
     * @param payResult
     * @param hisResult
     * @param orderStatus
     * @param refoundResult
     * @return
     */
    public static String getRechargeStr(String payResult, String hisResult) {
        String codeStr = null;
        if (payResult.equals("1")) {
            return payResult+hisResult;
        } else {
            //表示未支付
            codeStr = "0x";
        }
        return codeStr;
    }

    /**
     * 缴费业务，代码转换
     *
     * @param payResult
     * @param hisResult
     * @param bussinessStatus
     * @param refoundResult
     * @return
     */
    public static String getPaymentStr(String payResult, String hisResult, String bussinessStatus, String refoundResult) {
        String codeStr = null;
        if (payResult.equals("1")) {
            if (hisResult.equals("0")) {
                return "10";//未执行
            } else if (hisResult.equals("1")) {
//                return "11";
                if ("0".equals(bussinessStatus)) {
                    return "110x";
                } else if ("1".equals(bussinessStatus)) {
                    return "111x";
                }else if ("2".equals(bussinessStatus)) {
                    return "112x";
                } else {
                    return "113x";
                }
            } else if (hisResult.equals("2")) {
                return "12";
            } else if (hisResult.equals("3")) {
                return "13";
            }
        } else {
            //表示未支付
            codeStr = "0x";
        }
        return codeStr;
    }

    /**
     * 挂号业务,代码转换
     *
     * @param payResult
     * @param hisResult
     * @param orderStatus
     * @param refoundResult
     * @return
     */
    public static String getRegistrationStr(String payResult, String hisResult, String orderStatus, String refoundResult) {
        String codeStr = null;
        if (payResult.equals("1")) {
            if (hisResult.equals("0")) {
                return "10xx";
            } else if (hisResult.equals("1")) {
                if ("0".equals(orderStatus)) {
                    return "110x";
                } else if ("1".equals(orderStatus)) {
                    return "111x";
                } else if ("2".equals(orderStatus)) {
                    return "112x";
                } else if ("3".equals(orderStatus)) {
                    return "113x";
                }
            } else if (hisResult.equals("2")) {
                return "12xx";
            } else if (hisResult.equals("3")) {
                return "13xx";
            }
        } else {
            //表示未支付
            codeStr = "0x";
        }
        return codeStr;
    }

}
