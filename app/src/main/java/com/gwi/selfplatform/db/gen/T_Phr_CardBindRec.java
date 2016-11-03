package com.gwi.selfplatform.db.gen;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.io.Serializable;

/**
 * Entity mapped to table T__PHR__CARD_BIND_REC.
 */
public class T_Phr_CardBindRec implements Serializable{

    private Long RecNo;
    /** Not-null value. */
    private String EhrId;
    private String CardNo;
    private Integer CardType;
    private Integer CardStatus;
    private String BindMan;
    private java.util.Date BindDate;
    private String HospitalCode;
    private String HospitalName;
    private String PatientID;

    public T_Phr_CardBindRec() {
    }

    public T_Phr_CardBindRec(Long RecNo) {
        this.RecNo = RecNo;
    }

    public T_Phr_CardBindRec(Long RecNo, String EhrId, String CardNo, Integer CardType, Integer CardStatus, String BindMan, java.util.Date BindDate, String HospitalCode, String HospitalName, String PatientID) {
        this.RecNo = RecNo;
        this.EhrId = EhrId;
        this.CardNo = CardNo;
        this.CardType = CardType;
        this.CardStatus = CardStatus;
        this.BindMan = BindMan;
        this.BindDate = BindDate;
        this.HospitalCode = HospitalCode;
        this.HospitalName = HospitalName;
        this.PatientID = PatientID;
    }

    public Long getRecNo() {
        return RecNo;
    }

    public void setRecNo(Long RecNo) {
        this.RecNo = RecNo;
    }

    /** Not-null value. */
    public String getEhrId() {
        return EhrId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEhrId(String EhrId) {
        this.EhrId = EhrId;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public Integer getCardType() {
        return CardType;
    }

    public void setCardType(Integer CardType) {
        this.CardType = CardType;
    }

    public Integer getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(Integer CardStatus) {
        this.CardStatus = CardStatus;
    }

    public String getBindMan() {
        return BindMan;
    }

    public void setBindMan(String BindMan) {
        this.BindMan = BindMan;
    }

    public java.util.Date getBindDate() {
        return BindDate;
    }

    public void setBindDate(java.util.Date BindDate) {
        this.BindDate = BindDate;
    }

    public String getHospitalCode() {
        return HospitalCode;
    }

    public void setHospitalCode(String HospitalCode) {
        this.HospitalCode = HospitalCode;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String HospitalName) {
        this.HospitalName = HospitalName;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String PatientID) {
        this.PatientID = PatientID;
    }

}
