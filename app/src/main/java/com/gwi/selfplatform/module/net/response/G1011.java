package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1011 implements Serializable {

    public static final String STATUS_OK = "1";

    private String TranSerNo;
    private String PatientID;
    private String OutPatientNo;
    private String Money;
    private String CardStatus;
    private String Name;
    private String Sex;
//    private String Age;
    private String Birthday;
    private String BankCardNO;

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getTranSerNo() {
        return TranSerNo;
    }
    public void setTranSerNo(String tranSerNo) {
        TranSerNo = tranSerNo;
    }
    public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
    public String getOutPatientNo() {
        return OutPatientNo;
    }
    public void setOutPatientNo(String outPatientNo) {
        OutPatientNo = outPatientNo;
    }
    public String getMoney() {
        return Money==null?"0":Money;
    }
    public void setMoney(String money) {
        Money = money;
    }
    public String getCardStatus() {
        return CardStatus;
    }
    public void setCardStatus(String cardStatus) {
        CardStatus = cardStatus;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getSex() {
        return Sex;
    }
    public void setSex(String sex) {
        Sex = sex;
    }
//    public String getAge() {
//        return Age;
//    }
//    public void setAge(String age) {
//        Age = age;
//    }
    public String getBankCardNO() {
        return BankCardNO;
    }
    public void setBankCardNO(String bankCardNo) {
        BankCardNO = bankCardNo;
    }
    public String getIDCardNo() {
        return IDCardNo;
    }
    public void setIDCardNo(String iDCardNo) {
        IDCardNo = iDCardNo;
    }
    public String getIDCardType() {
        return IDCardType;
    }
    public void setIDCardType(String iDCardType) {
        IDCardType = iDCardType;
    }
    public String getFeeType() {
        return FeeType;
    }
    public void setFeeType(String feeType) {
        FeeType = feeType;
    }
    public String getPhoneNo() {
        return PhoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
    public String getAccBalance() {
        return AccBalance;
    }
    public void setAccBalance(String accBalance) {
        AccBalance = accBalance;
    }
    public String getNote() {
        return Note;
    }
    public void setNote(String note) {
        Note = note;
    }
    private String IDCardNo;
    private String IDCardType;
    private String FeeType;
    private String PhoneNo;
    private String AccBalance;
    private String Note;
}
