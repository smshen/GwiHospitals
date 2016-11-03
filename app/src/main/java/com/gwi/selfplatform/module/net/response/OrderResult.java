package com.gwi.selfplatform.module.net.response;

public class OrderResult {
    private String OrderNo;
    private String PrepayId;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPrepayId() {
        return PrepayId;
    }

    public void setPrepayId(String prepayId) {
        PrepayId = prepayId;
    }
}
