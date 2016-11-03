package com.gwi.selfplatform.module.net.request;

public class OrderQuery {
    private String OrderNo;
    private String HospCode;
    private String PayType;//TransType==>PayType
    private String BusinessType;
    private String CardNo;
    private String IDCardNo;
    private String BeginDate;
    private String EndDate;
    private String PatientID;
    private String OrderStatus;
    private int PageIndex;
    private int PageSize;
    //不知道为什么又添加了该字段...
    private String TransType;

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
    public String getOrderNo() {
        return OrderNo;
    }
    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
    public String getHospCode() {
        return HospCode;
    }
    public void setHospCode(String hospCode) {
        HospCode = hospCode;
    }
    public String getPayType() {
        return PayType;
    }
    public void setPayType(String payType) {
        PayType = payType;
    }
    public String getBusinessType() {
        return BusinessType;
    }
    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }
    public String getCardNo() {
        return CardNo;
    }
    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }
    public String getIDCardNo() {
        return IDCardNo;
    }
    public void setIDCardNo(String iDCardNo) {
        IDCardNo = iDCardNo;
    }
    public String getBeginDate() {
        return BeginDate;
    }
    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }
    public String getEndDate() {
        return EndDate;
    }
    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
    
}
