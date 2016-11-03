package com.gwi.selfplatform.module.net.beans;

public class KBDrug extends KBBase{
    private long DurgId;
    private String DrugName;
    public long getDurgId() {
        return DurgId;
    }
    public void setDurgId(long durgId) {
        DurgId = durgId;
    }
    public String getDrugName() {
        return DrugName;
    }
    public void setDrugName(String drugName) {
        DrugName = drugName;
    }
}
