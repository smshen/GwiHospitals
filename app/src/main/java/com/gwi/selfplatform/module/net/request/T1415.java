package com.gwi.selfplatform.module.net.request;


public class T1415 extends TBody{
	private String regHospID;
	private String typeID;
	private String deptID;
	private String orderDate;
	private String doctID;
	private String rankID;
	private String regID;
	private String TimeRegion;
	public String getTimeRegion() {
		return TimeRegion;
	}

	public void setTimeRegion(String timeRegionId) {
		this.TimeRegion = timeRegionId;
	}

	private String patientBookFee;
	private String totalRegFee;
	private String regFee;
	private String treatFee;
	private String serviceFee;
	private String metaFee;
	private String otherFee;
	private String payMode;
	private String terTranSerNo;
	private String medInsureTranNo;
	private String medInsureStr;
	private String MedInsureFee;
	private String personalFee;
	private String note;
	private TPayInfo payInfo;
	
	public TPayInfo getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(TPayInfo payInfo) {
		this.payInfo = payInfo;
	}

	public String getRegHospID() {
		return regHospID;
	}

	public void setRegHospID(String regHospID) {
		this.regHospID = regHospID;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public String getDeptID() {
		return deptID;
	}

	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDoctID() {
		return doctID;
	}

	public void setDoctID(String doctID) {
		this.doctID = doctID;
	}

	public String getRankID() {
		return rankID;
	}

	public void setRankID(String rankID) {
		this.rankID = rankID;
	}

	public String getRegID() {
		return regID;
	}

	public void setRegID(String regID) {
		this.regID = regID;
	}

	public String getPatientBookFee() {
		return patientBookFee;
	}

	public void setPatientBookFee(String patientBookFee) {
		this.patientBookFee = patientBookFee;
	}

	public String getTotalRegFee() {
		return totalRegFee;
	}

	public void setTotalRegFee(String totalRegFee) {
		this.totalRegFee = totalRegFee;
	}

	public String getRegFee() {
		return regFee;
	}

	public void setRegFee(String regFee) {
		this.regFee = regFee;
	}

	public String getTreatFee() {
		return treatFee;
	}

	public void setTreatFee(String treatFee) {
		this.treatFee = treatFee;
	}

	public String getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getMetaFee() {
		return metaFee;
	}

	public void setMetaFee(String metaFee) {
		this.metaFee = metaFee;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getTerTranSerNo() {
		return terTranSerNo;
	}

	public void setTerTranSerNo(String terTranSerNo) {
		this.terTranSerNo = terTranSerNo;
	}

	public String getMedInsureTranNo() {
		return medInsureTranNo;
	}

	public void setMedInsureTranNo(String medInsureTranNo) {
		this.medInsureTranNo = medInsureTranNo;
	}

	public String getMedInsureStr() {
		return medInsureStr;
	}

	public void setMedInsureStr(String medInsureStr) {
		this.medInsureStr = medInsureStr;
	}

	public String getMedInsureFee() {
		return MedInsureFee;
	}

	public void setMedInsureFee(String medInsureFee) {
		MedInsureFee = medInsureFee;
	}

	public String getPersonalFee() {
		return personalFee;
	}

	public void setPersonalFee(String personalFee) {
		this.personalFee = personalFee;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static class TPayInfo
	{
		public String TranAccount;
		public String TranAmt;
		public String TranSerNo;
		public String TranTime;
		public String BankPNo;
		public String POSID;
		public String POSSerNo;
		public String ReceptNo;
		public String AuthorizeNo;
	}
}
