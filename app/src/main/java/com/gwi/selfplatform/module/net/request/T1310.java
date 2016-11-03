package com.gwi.selfplatform.module.net.request;

public class T1310 extends TBody {
    private String PatientID;
    private String VisitStartDate;
    private String VisitEndDate;
    public String getPatientID() {
        return PatientID;
    }
    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
    public String getVisitStartDate() {
        return VisitStartDate;
    }
    public void setVisitStartDate(String visitStartDate) {
        VisitStartDate = visitStartDate;
    }
    public String getVisitEndDate() {
        return VisitEndDate;
    }
    public void setVisitEndDate(String visitEndDate) {
        VisitEndDate = visitEndDate;
    }

}
