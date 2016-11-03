package com.gwi.selfplatform.module.net.beans;

import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1417;

/**
 * 创建订单需要的参数
 * @author 彭毅
 * @date 2015/6/5.
 */
public class OrderParamater {

    private T_Phr_BaseInfo mBaseInfo;
    private T_UserInfo mUserInfo;
    private  String mRegIds;
    private String mPayType;
    private String mBussinessType;
    private G1417 mDct;
    private G1211 mDept;
    private G1011 mPatientInfo;
    private double mTransactionValue;
    private String mOrderDate;
    private T_Phr_CardBindRec mCardInfo;
    private String mTypeID;
    private G1017 mSubHosp;

    public G1017 getSubHosp() {
        return mSubHosp;
    }

    public void setSubHosp(G1017 subHosp) {
        this.mSubHosp = subHosp;
    }

    public String getTypeID() {
        return mTypeID;
    }

    public void setTypeID(String typeID) {
        mTypeID = typeID;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        mOrderDate = orderDate;
    }

    public T_Phr_BaseInfo getBaseInfo() {
        return mBaseInfo;
    }

    public void setBaseInfo(T_Phr_BaseInfo baseInfo) {
        mBaseInfo = baseInfo;
    }

    public String getBussinessType() {
        return mBussinessType;
    }

    public void setBussinessType(String bussinessType) {
        mBussinessType = bussinessType;
    }

    public T_Phr_CardBindRec getCardInfo() {
        return mCardInfo;
    }

    public void setCardInfo(T_Phr_CardBindRec cardInfo) {
        mCardInfo = cardInfo;
    }

    public G1417 getDct() {
        return mDct;
    }

    public void setDct(G1417 dct) {
        mDct = dct;
    }

    public G1211 getDept() {
        return mDept;
    }

    public void setDept(G1211 dept) {
        mDept = dept;
    }

    public G1011 getPatientInfo() {
        return mPatientInfo;
    }

    public void setPatientInfo(G1011 patientInfo) {
        mPatientInfo = patientInfo;
    }

    public String getRegIds() {
        return mRegIds;
    }

    public void setRegIds(String regIds) {
        mRegIds = regIds;
    }

    public double getTransactionValue() {
        return mTransactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        mTransactionValue = transactionValue;
    }

    public String getPayType() {
        return mPayType;
    }

    public void setPayType(String payType) {
        mPayType = payType;
    }

    public T_UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(T_UserInfo userInfo) {
        mUserInfo = userInfo;
    }
}
