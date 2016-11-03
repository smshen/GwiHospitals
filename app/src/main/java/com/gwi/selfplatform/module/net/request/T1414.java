package com.gwi.selfplatform.module.net.request;


public class T1414 extends TBody{
    private String TerTranSerNo;
    private String RegSourceID;
    private String TypeID;
    private String DeptID;
    private String DoctID;
    private String RankID;
    //市中医院:TimeRegionId==>TimeRegion
    private String TimeRegion;
    private double PatientBookFee;
    private double TotalRegFee;
    private double RegFee;
    private double TreatFee;
    private double ServicesFee;
    private double MetaFee;
    private double OtherFee;
    private String PayMode;
    private String MedInsureTranNo;
    private String MedInsureStr;
    private double MedInsureFee;
    private double PersonalFee;
    private String OrderDate;
    //中心医院添加
    private String PatientID;
    private double DiscountFee;
    private String HospAreaID;
    //东莞添加
    private String SubHospCode;

    //2015-06-11
    private String DeptName;
    private String DeptLocation;
    private String StartTime;
    private String EndTime;
    private String DoctName;
    private String RankName;

    public String getHospAreaID() {
        return HospAreaID;
    }

    public void setHospAreaID(String hospAreaID) {
        HospAreaID = hospAreaID;
    }

    public double getDiscountFee() {
        return DiscountFee;
    }

    public void setDiscountFee(double discountFee) {
        DiscountFee = discountFee;
    }

    public String getDeptLocation() {
        return DeptLocation;
    }

    public void setDeptLocation(String deptLocation) {
        DeptLocation = deptLocation;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDoctName() {
        return DoctName;
    }

    public void setDoctName(String doctName) {
        DoctName = doctName;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getRankName() {
        return RankName;
    }

    public void setRankName(String rankName) {
        RankName = rankName;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
    public String getOrderDate() {
        return OrderDate;
    }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }
    private PayInfo PayInfo;
    private UserInfo UserInfo;
    
    public String getTerTranSerNo() {
        return TerTranSerNo;
    }
    public void setTerTranSerNo(String terTranSerNo) {
        TerTranSerNo = terTranSerNo;
    }
    public String getRegSourceID() {
        return RegSourceID;
    }
    public void setRegSourceID(String regSourceID) {
        RegSourceID = regSourceID;
    }
    public String getTypeID() {
        return TypeID;
    }
    public void setTypeID(String typeID) {
        TypeID = typeID;
    }
    public String getDeptID() {
        return DeptID;
    }
    public void setDeptID(String deptID) {
        DeptID = deptID;
    }
    public String getDoctID() {
        return DoctID;
    }
    public void setDoctID(String doctID) {
        DoctID = doctID;
    }
    public String getRankID() {
        return RankID;
    }
    public void setRankID(String rankID) {
        RankID = rankID;
    }
    public String getTimeRegion() {
        return TimeRegion;
    }
    public void setTimeRegion(String timeRegion) {
        TimeRegion = timeRegion;
    }
    public double getPatientBookFee() {
        return PatientBookFee;
    }
    public void setPatientBookFee(double patientBookFee) {
        PatientBookFee = patientBookFee;
    }
    public double getTotalRegFee() {
        return TotalRegFee;
    }
    public void setTotalRegFee(double totalRegFee) {
        TotalRegFee = totalRegFee;
    }
    public double getRegFee() {
        return RegFee;
    }
    public void setRegFee(double regFee) {
        RegFee = regFee;
    }
    public double getTreatFee() {
        return TreatFee;
    }
    public void setTreatFee(double treatFee) {
        TreatFee = treatFee;
    }
    public double getServicesFee() {
        return ServicesFee;
    }
    public void setServicesFee(double servicesFee) {
        ServicesFee = servicesFee;
    }
    public double getMetaFee() {
        return MetaFee;
    }
    public void setMetaFee(double metaFee) {
        MetaFee = metaFee;
    }
    public double getOtherFee() {
        return OtherFee;
    }
    public void setOtherFee(double otherFee) {
        OtherFee = otherFee;
    }
    public String getPayMode() {
        return PayMode;
    }
    public void setPayMode(String payMode) {
        PayMode = payMode;
    }
    public String getMedInsureTranNo() {
        return MedInsureTranNo;
    }
    public void setMedInsureTranNo(String medInsureTranNo) {
        MedInsureTranNo = medInsureTranNo;
    }
    public String getMedInsureStr() {
        return MedInsureStr;
    }
    public void setMedInsureStr(String medInsureStr) {
        MedInsureStr = medInsureStr;
    }
    public double getMedInsureFee() {
        return MedInsureFee;
    }
    public void setMedInsureFee(double medInsureFee) {
        MedInsureFee = medInsureFee;
    }
    public double getPersonalFee() {
        return PersonalFee;
    }
    public void setPersonalFee(double personalFee) {
        PersonalFee = personalFee;
    }
    public PayInfo getPayInfo() {
        return PayInfo;
    }
    public void setPayInfo(PayInfo payInfo) {
        PayInfo = payInfo;
    }
    public UserInfo getUserInfo() {
        return UserInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        UserInfo = userInfo;
    }
    public String getSubHospCode() {
        return SubHospCode;
    }
    public void setSubHospCode(String deptID) {
    	SubHospCode = deptID;
    }
}
