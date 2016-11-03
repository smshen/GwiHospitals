package com.gwi.selfplatform.module.net.request;

public class T1612 extends TBody {
	private String RegID;
	private String RecipeID;
	private String TotalFee;
	private String PersonalFee;
	private String MedInsureFee;
	private String PreChargeFee;
	private String PayMode;
	private String TerTranSerNo;
	private String MedInsureStr;

	private PayInfo PayInfo;
	
	private String PatientID;
	
	public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getRegID() {
		return RegID;
	}

	public void setRegID(String regID) {
		RegID = regID;
	}

	public String getRecipeID() {
		return RecipeID;
	}

	public void setRecipeID(String recipeID) {
		RecipeID = recipeID;
	}

	public String getTotalFee() {
		return TotalFee;
	}

	public void setTotalFee(String totalFee) {
		TotalFee = totalFee;
	}

	public String getPersonalFee() {
		return PersonalFee;
	}

	public void setPersonalFee(String personalFee) {
		PersonalFee = personalFee;
	}

	public String getMedInsureFee() {
		return MedInsureFee;
	}

	public void setMedInsureFee(String medInsureFee) {
		MedInsureFee = medInsureFee;
	}

	public String getPreChargeFee() {
		return PreChargeFee;
	}

	public void setPreChargeFee(String preChargeFee) {
		PreChargeFee = preChargeFee;
	}

	public String getPayMode() {
		return PayMode;
	}

	public void setPayMode(String payMode) {
		PayMode = payMode;
	}

	public String getTerTranSerNo() {
		return TerTranSerNo;
	}

	public void setTerTranSerNo(String terTranSerNo) {
		TerTranSerNo = terTranSerNo;
	}

	public String getMedInsureStr() {
		return MedInsureStr;
	}

	public void setMedInsureStr(String medInsureStr) {
		MedInsureStr = medInsureStr;
	}

	public PayInfo getPayInfo() {
		return PayInfo;
	}

	public void setPayInfo(PayInfo payInfo) {
		PayInfo = payInfo;
	}
	
	
}
