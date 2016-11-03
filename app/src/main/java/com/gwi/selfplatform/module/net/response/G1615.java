package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

/**
 * <b>Note:</b>重载{@link G1615#equals(Object)}
 * @author 彭毅
 *
 */
public class G1615 extends Body implements Serializable{
    
    public static class PatientInfo implements Serializable{
        private String PatientName;
        private String CardNo;
        private String Money;
        public String getPatientName() {
            return PatientName;
        }
        public void setPatientName(String patientName) {
            PatientName = patientName;
        }
        public String getCardNo() {
            return CardNo;
        }
        public void setCardNo(String cardNo) {
            CardNo = cardNo;
        }
        public String getMoney() {
            return Money;
        }
        public void setMoney(String money) {
            Money = money;
        }
    }
    
    private PatientInfo PatientInfo;
    
    /** 挂号流水号 */
    private String RegNo;
    private String ExecDeptID;
    private String ExecDeptName;
    private String ExecLocation;
    /** 医嘱(处方)ID */
    private String RecipeNo;
    private String RecipeName;
    private String TotalFee;
    private int Status;
    private String PaymentTime;
    
    public PatientInfo getPatientInfo() {
        return PatientInfo;
    }
    public void setPatientInfo(PatientInfo patientInfo) {
        PatientInfo = patientInfo;
    }
    public String getRegNo() {
        return RegNo;
    }
    public void setRegNo(String regNo) {
        RegNo = regNo;
    }
    public String getExecDeptID() {
        return ExecDeptID;
    }
    public void setExecDeptID(String execDeptID) {
        ExecDeptID = execDeptID;
    }
    public String getExecDeptName() {
        return ExecDeptName;
    }
    public void setExecDeptName(String execDeptName) {
        ExecDeptName = execDeptName;
    }
    public String getExecLocation() {
        return ExecLocation;
    }
    public void setExecLocation(String execLocation) {
        ExecLocation = execLocation;
    }
    public String getRecipeNo() {
        return RecipeNo;
    }
    public void setRecipeNo(String recipeNo) {
        RecipeNo = recipeNo;
    }
    public String getRecipeName() {
        return RecipeName;
    }
    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }
    public String getTotalFee() {
        return TotalFee;
    }
    public void setTotalFee(String totalFee) {
        TotalFee = totalFee;
    }
    public int getStatus() {
        return Status;
    }
    public void setStatus(int status) {
        Status = status;
    }
    public String getPaymentTime() {
        return PaymentTime;
    }
    public void setPaymentTime(String paymentTime) {
        PaymentTime = paymentTime;
    }
    
}
