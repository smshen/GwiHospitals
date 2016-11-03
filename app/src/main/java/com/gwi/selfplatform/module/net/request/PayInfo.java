package com.gwi.selfplatform.module.net.request;

public class PayInfo {
    private String TranAccount;
    private Double TranAmt;
    private String TranSerNo;
    private String TranDate;
    private String TranTime;
    private String BankPNo;
    private String POSID;
    private String POSSerNo;
    private String ReceiptNo;
    private String AuthorizeNo;


    public String getTranAccount() {
        return TranAccount;
    }

    public void setTranAccount(String tranAccount) {
        TranAccount = tranAccount;
    }

    public Double getTranAmt() {
        return TranAmt;
    }

    public void setTranAmt(Double tranAmt) {
        TranAmt = tranAmt;
    }

    public String getTranSerNo() {
        return TranSerNo;
    }

    public void setTranSerNo(String tranSerNo) {
        TranSerNo = tranSerNo;
    }

    public String getTranDate() {
        return TranDate;
    }

    public void setTranDate(String tranDate) {
        TranDate = tranDate;
    }

    public String getTranTime() {
        return TranTime;
    }

    public void setTranTime(String tranTime) {
        TranTime = tranTime;
    }

    public String getBankPNo() {
        return BankPNo;
    }

    public void setBankPNo(String bankPNo) {
        BankPNo = bankPNo;
    }

    public String getPOSID() {
        return POSID;
    }

    public void setPOSID(String pOSID) {
        POSID = pOSID;
    }

    public String getPOSSerNo() {
        return POSSerNo;
    }

    public void setPOSSerNo(String pOSSerNo) {
        POSSerNo = pOSSerNo;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getAuthorizeNo() {
        return AuthorizeNo;
    }

    public void setAuthorizeNo(String authorizeNo) {
        AuthorizeNo = authorizeNo;
    }

}
