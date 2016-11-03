package com.gwi.selfplatform.module.net.request;

public class UserInfo {
    private String CorpCode;
    private String IDCardNo;
    private String UserName;
    private String Mobile;
    private String CusOrderNum;
    private String Sex;
    
    public String getSex() {
        return Sex;
    }
    public void setSex(String sex) {
        Sex = sex;
    }
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
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String orderMobile) {
        Mobile = orderMobile;
    }
    public String getCusOrderNum() {
        return CusOrderNum;
    }
    public void setCusOrderNum(String cusOrderNum) {
        CusOrderNum = cusOrderNum;
    }
}
