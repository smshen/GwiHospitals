package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1212 implements Serializable{

	private static final long serialVersionUID = 0x12L;
	private String RegSourceID;
	private String RegSourceName;
	private String TypeID;
	private String DeptID;
	private String DeptName;
	private String Specify;
	
	private String DoctID;
	private String DoctName;
	private String RankID;
	private String RankName;
	private double TotalRegFee;
	private double RegFee;
	private double TreatFee;
	private double ServicesFee;
	private double MetaFee;
	private double OtherFee;
	private String StartTime;
	private String TimeRegion;
	private String EndTime;
	private int HaveCount;
	private int SchemaType;
	private String Note;

    //中心医院,免挂号费字段
	private double DiscountFee;

    public double getDiscountFee() {
        return DiscountFee;
    }

    public void setDiscountFee(double discountFee) {
        DiscountFee = discountFee;
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

	public String getSpecify() {
		return Specify;
	}

	public void setSpecify(String specify) {
		Specify = specify;
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

	public int getHaveCount() {
		return HaveCount;
	}

	public void setHaveCount(int haveCount) {
		HaveCount = haveCount;
	}

	public int getSchemaType() {
		return SchemaType;
	}

	public void setSchemaType(int schemaType) {
		SchemaType = schemaType;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
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

	public String getTimeRegion() {
		return TimeRegion;
	}

	public void setTimeRegion(String timeRegion) {
		TimeRegion = timeRegion;
	}
}
