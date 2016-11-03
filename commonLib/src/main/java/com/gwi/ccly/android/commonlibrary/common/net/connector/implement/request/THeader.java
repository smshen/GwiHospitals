package com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request;

/**
 * @author 彭毅
 * @date 2015/4/23.
 */
public class THeader {
    private String Account;
    private String AccountPassword;
    private String AppTypeCode;
    private int FunCode;
    private String TerminalNo;
    private String AppCode;
    private String ReqTime;

    /** 兼容 */
    private String HospCode;

    public String getHospCode() {
        return HospCode;
    }

    public void setHospCode(String hospCode) {
        HospCode = hospCode;
    }

    public String getReqTime() {
        return ReqTime;
    }

    public void setReqTime(String reqTime) {
        ReqTime = reqTime;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getAccountPassword() {
        return AccountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        AccountPassword = accountPassword;
    }

    public String getAppTypeCode() {
        return AppTypeCode;
    }

    public void setAppTypeCode(String appTypeCode) {
        AppTypeCode = appTypeCode;
    }

    public int getFunCode() {
        return FunCode;
    }

    public void setFunCode(int funCode) {
        FunCode = funCode;
    }

    public String getTerminalNo() {
        return TerminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        TerminalNo = terminalNo;
    }

    public String getAppCode() {
        return AppCode;
    }

    public void setAppCode(String appCode) {
        AppCode = appCode;
    }
}
