package com.gwi.selfplatform.module.net.request;

public class T1610 extends TBody {
    private String PatientID;
    
	private String StartDate;
	private String EndDate;
	
	public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
    public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	
}
