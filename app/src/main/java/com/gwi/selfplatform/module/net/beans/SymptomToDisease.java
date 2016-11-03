package com.gwi.selfplatform.module.net.beans;

/**
 * 查询症状对应的疾病列表[1703]
 * @author 彭毅
 *
 */
public class SymptomToDisease {
    private String SId;
    private String DiseaseId;
    private String DiseaseName;
    private String Confidence;
    private String DeptName;
    private String Memo;

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getSId() {
        return SId;
    }
    public String getDiseaseId() {
        return DiseaseId;
    }
    public String getDiseaseName() {
        return DiseaseName;
    }
    public String getConfidence() {
        return Confidence;
    }
    public String getMemo() {
        return Memo;
    }
    public void setSId(String sId) {
        SId = sId;
    }
    public void setDiseaseId(String diseaseId) {
        DiseaseId = diseaseId;
    }
    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }
    public void setConfidence(String confidence) {
        Confidence = confidence;
    }
    public void setMemo(String memo) {
        Memo = memo;
    }
    
}
