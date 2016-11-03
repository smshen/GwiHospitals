package com.gwi.selfplatform.module.net.request;

public class T1512 extends TBody {
    private String CorpCode;
    private String OrderNo;
    private String PatientID;
    private String IDCardNo;
    private String TerTranSerNo;
    //湘西时添加
    private String OrderIdentity;
    //中心医院添加
    private String PayMode;
    private String TranSerNo;
    private String RegSourceID;
    
    public String getRegSourceID() {
		return RegSourceID;
	}
	public void setRegSourceID(String regSourceID) {
		RegSourceID = regSourceID;
	}
	public String getPayMode() {
		return PayMode;
	}
	public void setPayMode(String payMode) {
		PayMode = payMode;
	}
	public String getTranSerNo() {
		return TranSerNo;
	}
	public void setTranSerNo(String tranSerNo) {
		TranSerNo = tranSerNo;
	}
	public String getOrderIdentity() {
        return OrderIdentity;
    }
    public void setOrderIdentity(String orderIdentity) {
        OrderIdentity = orderIdentity;
    }
    public String getCorpCode() {
        return CorpCode;
    }
    public void setCorpCode(String corpCode) {
        CorpCode = corpCode;
    }
    public String getOrderNo() {
        return OrderNo;
    }
    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
    public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
    public String getIDCardNo() {
        return IDCardNo;
    }
    public void setIDCardNo(String iDCardNo) {
        IDCardNo = iDCardNo;
    }
    public String getTerTranSerNo() {
        return TerTranSerNo;
    }
    public void setTerTranSerNo(String terTranSerNo) {
        TerTranSerNo = terTranSerNo;
    }
    
    
}
