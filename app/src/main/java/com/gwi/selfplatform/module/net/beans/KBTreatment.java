package com.gwi.selfplatform.module.net.beans;

public class KBTreatment extends KBBase {
    private String TreatmentId;
    private String TreatmentName;
    private String TreatmentKind;
    public String getTreatmentId() {
        return TreatmentId;
    }
    public String getTreatmentName() {
        return TreatmentName;
    }
    public String getTreatmentKind() {
        return TreatmentKind;
    }
    public void setTreatmentId(String treatmentId) {
        TreatmentId = treatmentId;
    }
    public void setTreatmentName(String treatmentName) {
        TreatmentName = treatmentName;
    }
    public void setTreatmentKind(String treatmentKind) {
        TreatmentKind = treatmentKind;
    }
    
}
