package com.gwi.selfplatform.module.net.beans;

public class KBDiseaseDetails extends KBDisease{
    private String Summary;
    private String DiseaseReason;
    private String Symptom;
    private String Test;
    private String Diagnosis;
    private String Prevent;
    private String Syndrome;
    private String Treatment;
    private String DeptName;
    
    public String getDeptName() {
        return DeptName;
    }
    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
    public String getSummary() {
        return Summary;
    }
    public void setSummary(String summary) {
        Summary = summary;
    }
    public String getDiseaseReason() {
        return DiseaseReason;
    }
    public void setDiseaseReason(String diseaseReason) {
        DiseaseReason = diseaseReason;
    }
    public String getSymptom() {
        return Symptom;
    }
    public void setSymptom(String symptom) {
        Symptom = symptom;
    }
    public String getTest() {
        return Test;
    }
    public void setTest(String test) {
        Test = test;
    }
    public String getDiagnosis() {
        return Diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }
    public String getPrevent() {
        return Prevent;
    }
    public void setPrevent(String prevent) {
        Prevent = prevent;
    }
    public String getSyndrome() {
        return Syndrome;
    }
    public void setSyndrome(String syndrome) {
        Syndrome = syndrome;
    }
    public String getTreatment() {
        return Treatment;
    }
    public void setTreatment(String treatment) {
        Treatment = treatment;
    }
    
}
