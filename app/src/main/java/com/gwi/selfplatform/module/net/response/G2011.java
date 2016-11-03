package com.gwi.selfplatform.module.net.response;

public class G2011 extends Body {
    private String ItemID;
    private String ItemName;
    private String Specs;
    private String UnitPrice;
    private String Unit;
    private String MedInsureType;
    private String SelfPercent;
    
    
    public String getMedInsureType() {
        return MedInsureType;
    }
    public void setMedInsureType(String medInsureType) {
        MedInsureType = medInsureType;
    }
    public String getItemID() {
        return ItemID;
    }
    public void setItemID(String itemID) {
        ItemID = itemID;
    }
    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public String getSpecs() {
        return Specs;
    }
    public void setSpecs(String specs) {
        Specs = specs;
    }
    public String getUnitPrice() {
        return UnitPrice;
    }
    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }
    public String getUnit() {
        return Unit;
    }
    public void setUnit(String unit) {
        Unit = unit;
    }
    public String getSelfPercent() {
        return SelfPercent;
    }
    public void setSelfPercent(String selfPercent) {
        SelfPercent = selfPercent;
    }
    
}
