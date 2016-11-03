package com.gwi.selfplatform.module.net.beans;

public class KBTestCheckDetails extends KBTestCheck {
    /** 项目介绍 */
    private String Summary;
    /** 正常值参考 */
    private String Reference;
    /** 临床意义 */
    private String ClinicalSignificance;
    public String getSummary() {
        return Summary;
    }
    public void setSummary(String summary) {
        Summary = summary;
    }
    public String getReference() {
        return Reference;
    }
    public void setReference(String reference) {
        Reference = reference;
    }
    public String getClinicalSignificance() {
        return ClinicalSignificance;
    }
    public void setClinicalSignificance(String clinicalSignificance) {
        ClinicalSignificance = clinicalSignificance;
    }
    
}
