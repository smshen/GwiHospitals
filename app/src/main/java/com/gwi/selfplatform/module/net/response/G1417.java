package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

/**G1413*/
public class G1417 implements Serializable{
	private static final long serialVersionUID = 1417L;
	
	private String RegSourceID;
	private String RegSourceName;
	private String HospCode;
	private String TypeID;
	private String DeptID;
	private String DeptName;
	private String DoctID;
	private String DoctName;
	
	private String RankID;
	private String RankName;
	private String OrderDate;
	private String StartTime;
	private String EndTime;
	private String TimeRegion;
	private int AllCount;
	private int OutCount;
	private String Specify;

	private int HaveCount;
	private int RegType;
	private int SchemaType;
	private double RegFee;
	private double TreatFee;
	private String ServicesFee;
    //材料费
	private double MetaFee;
	private String OtherFee;
	private String Note;
	private double TotalRegFee;
	private String Date;

	//中心医院,免挂号费字段
	private double DiscountFee;

	public double getDiscountFee() {
		return DiscountFee;
	}

	public void setDiscountFee(double discountFee) {
		DiscountFee = discountFee;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public double getTotalRegFee() {
        return TotalRegFee;
    }
    public void setTotalRegFee(double totalRegFee) {
        TotalRegFee = totalRegFee;
    }
    public String getSpecify() {
		return Specify;
	}
	public void setSpecify(String specify) {
		Specify = specify;
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
	public String getHospCode() {
		return HospCode;
	}
	public void setHospCode(String hospCode) {
		HospCode = hospCode;
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
	public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
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
	public String getTimeRegion() {
		return TimeRegion;
	}
	public void setTimeRegion(String timeRegion) {
		TimeRegion = timeRegion;
	}
	public int getAllCount() {
		return AllCount;
	}
	public void setAllCount(int allCount) {
		AllCount = allCount;
	}
	public int getOutCount() {
		return OutCount;
	}
	public void setOutCount(int outCount) {
		OutCount = outCount;
	}
	public int getHaveCount() {
		return HaveCount;
	}
	public void setHaveCount(int haveCount) {
		HaveCount = haveCount;
	}
	public int getRegType() {
		return RegType;
	}
	public void setRegType(int regType) {
		RegType = regType;
	}
	public int getSchemaType() {
		return SchemaType;
	}
	public void setSchemaType(int schemaType) {
		SchemaType = schemaType;
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
	public String getServicesFee() {
		return ServicesFee;
	}
	public void setServicesFee(String servicesFee) {
		ServicesFee = servicesFee;
	}
	public double getMetaFee() {
		return MetaFee;
	}
	public void setMetaFee(double metaFee) {
		MetaFee = metaFee;
	}
	public String getOtherFee() {
		return OtherFee;
	}
	public void setOtherFee(String otherFee) {
		OtherFee = otherFee;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}

}
