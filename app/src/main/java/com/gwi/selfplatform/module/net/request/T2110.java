package com.gwi.selfplatform.module.net.request;

public class T2110 extends TBody {
	private String StartDate;
	private String PatientID;
	private String EndDate;
    private String Note;
    
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
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	
	
}
