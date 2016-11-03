package com.gwi.selfplatform.module.net.response;

public class G1510 extends GRegistRecordBase{
    private String HospName;
    private String OrderNo;
    private String OrderDate;
    private int OrderState;
    private String RegSourceID;
    private String RegSourceName;
    private String TypeID;
    private String DeptID;
    
    private String DeptName;
    private String DoctID;
    private String DoctName;
    private String RankID;
    private String RankName;
    private int TimeRegion;
    private String StartTime;
    private String EndTime;
    
    private double PatientBookFee;
    private double TotalRegFee;
    private double RegFee;
    private double TreatFee;
    private double ServicesFee;
    private double MetaFee;
    private double OtherFee;
    private String Specialty;
    private int IsAvailable;
    private String Note;
    private String OrderIdentity;
    
    //中心医院添加
    private String PayMode;
    private String TranSerNo;

    //CCB Demo add
    private String TreatLocation;

    public String getTreatLocation() {
        return TreatLocation;
    }

    public void setTreatLocation(String treatLocation) {
        TreatLocation = treatLocation;
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
    public String getHospName() {
        return HospName;
    }
    public void setHospName(String hospName) {
        HospName = hospName;
    }
    public String getOrderNo() {
        return OrderNo;
    }
    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }
    public String getOrderDate() {
        return OrderDate;
    }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }
    public int getOrderState() {
        return OrderState;
    }
    public void setOrderState(int orderState) {
        OrderState = orderState;
    }
    public String getRegSourceID() {
        return RegSourceID;
    }
    public void setRegSourceID(String regSourceID) {
        RegSourceID = regSourceID;
    }
    public String getRegSourceName() {
        return RegSourceName;
    }
    public void setRegSourceName(String regSourceName) {
        RegSourceName = regSourceName;
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
    public String getDeptName() {
        return DeptName;
    }
    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
    public String getDoctID() {
        return DoctID;
    }
    public void setDoctID(String doctID) {
        DoctID = doctID;
    }
    public String getDoctName() {
        return DoctName;
    }
    public void setDoctName(String doctName) {
        DoctName = doctName;
    }
    public String getRankID() {
        return RankID;
    }
    public void setRankID(String rankID) {
        RankID = rankID;
    }
    public String getRankName() {
        return RankName;
    }
    public void setRankName(String rankName) {
        RankName = rankName;
    }
    public int getTimeRegion() {
        return TimeRegion;
    }
    public void setTimeRegion(int timeRegion) {
        TimeRegion = timeRegion;
    }
    public String getStartTime() {
        return StartTime;
    }
    public void setStartTime(String startTime) {
        StartTime = startTime;
    }
    public String getEndTime() {
        return EndTime;
    }
    public void setEndTime(String endTime) {
        EndTime = endTime;
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
    public String getSpecialty() {
        return Specialty;
    }
    public void setSpecialty(String specialty) {
        Specialty = specialty;
    }
    public int getIsAvailable() {
        return IsAvailable;
    }
    public void setIsAvailable(int isAvailable) {
        IsAvailable = isAvailable;
    }
    public String getNote() {
        return Note;
    }
    public void setNote(String note) {
        Note = note;
    }
    
}
