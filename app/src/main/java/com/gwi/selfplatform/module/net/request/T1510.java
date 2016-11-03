package com.gwi.selfplatform.module.net.request;

public class T1510 extends TBody {
    private String CorpCode;
    private String IDCardNo;
    private int OrderStatus;
    private String VisitStartDate;
    private String VisitEndDate;  
	private String PatientID;
    public String getCorpCode() {
        return CorpCode;
    }
    public void setCorpCode(String corpCode) {
        CorpCode = corpCode;
    }
    public String getIDCardNo() {
        return IDCardNo;
    }
    public void setIDCardNo(String iDCardNo) {
        IDCardNo = iDCardNo;
    }
    public int getOrderStatus() {
        return OrderStatus;
    }
    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }
    public String getVisitStartDate() {
        return VisitStartDate;
    }
    public void setVisitStartDate(String visitStartDate) {
        VisitStartDate = visitStartDate;
    }
    public String getVisitEndDate() {
        return VisitEndDate;
    }
    public void setVisitEndDate(String visitEndDate) {
        VisitEndDate = visitEndDate;
    }
    public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
    	PatientID = patientID;
    }	
}
