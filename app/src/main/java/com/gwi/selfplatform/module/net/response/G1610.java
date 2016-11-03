package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1610 implements Serializable{
	private static final long serialVersionUID = 1610L;
	
	private String RegID;
	private String RegDate;
	private String RegSourceID;
	private String RegSourceName;
	private String RegisterArea;
	private String TypeID;
	private String RankName;
	private String DeptName;
	private String DoctName;
	private double TotalFee;
	private String FeeType;
	private double FavorFee;
	private double MedInsureFee;
	private double PersonalFee;
	private String Note;
	public String getRegID() {
		return RegID;
	}
	public void setRegID(String regID) {
		RegID = regID;
	}
	public String getRegDate() {
		return RegDate;
	}
	public void setRegDate(String regDate) {
		RegDate = regDate;
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
	public String getRegisterArea() {
		return RegisterArea;
	}
	public void setRegisterArea(String registerArea) {
		RegisterArea = registerArea;
	}
	public String getTypeID() {
		return TypeID;
	}
	public void setTypeID(String typeID) {
		TypeID = typeID;
	}
	public String getRankName() {
		return RankName;
	}
	public void setRankName(String rankName) {
		RankName = rankName;
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
	public double getTotalFee() {
		return TotalFee;
	}
	public void setTotalFee(double totalFee) {
		TotalFee = totalFee;
	}
	public String getFeeType() {
		return FeeType;
	}
	public void setFeeType(String feeType) {
		FeeType = feeType;
	}
	public double getFavorFee() {
		return FavorFee;
	}
	public void setFavorFee(double favorFee) {
		FavorFee = favorFee;
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
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	
}
