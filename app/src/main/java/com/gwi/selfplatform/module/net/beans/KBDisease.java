package com.gwi.selfplatform.module.net.beans;

public class KBDisease extends KBBase {
    private String DiseaseId;
    private String DiseaseName;
    private int BodyPart;
    private String DeptId;
    public String getDiseaseId() {
        return DiseaseId;
    }
    public void setDiseaseId(String diseaseId) {
        DiseaseId = diseaseId;
    }
    public String getDiseaseName() {
        return DiseaseName;
    }
    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }
    public int getBodyPart() {
        return BodyPart;
    }
    public void setBodyPart(int bodyPart) {
        BodyPart = bodyPart;
    }
    public String getDeptId() {
        return DeptId;
    }
    public void setDeptId(String deptId) {
        DeptId = deptId;
    }
    
}
