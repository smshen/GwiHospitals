package com.gwi.selfplatform.module.net.beans;

import java.io.Serializable;

public class KBDrugDetails extends KBBase implements Serializable{
	private static final long serialVersionUID = 1618L;
	/** 药物类型 */
    private String DrugKind;
    /** 用途分类 */
    private String UseKind;
    /** 属性分类 */
    private String PropertyKind;
    /** 药物成分 */
    private String DrugElement;
    /** 剂量 */
    private String Dosage;
    /** 适用症 */
    private String Indications;
    /** 禁忌 */
    private String Contraindication;
    /** 注意事项 */
    private String Notice;
    /** 不良反应 */
    private String ADR;
    
    private long DrugId;
    
    private String DrugName;
    
    public long getDurgId() {
        return DrugId;
    }
    public void setDurgId(long drugId) {
    	DrugId = drugId;
    }
    public String getDrugName() {
        return DrugName;
    }
    public void setDrugName(String drugName) {
        DrugName = drugName;
    }
    
	public String getDrugKind() {
        return DrugKind;
    }
    public void setDrugKind(String drugKind) {
        DrugKind = drugKind;
    }
    public String getUseKind() {
        return UseKind;
    }
    public void setUseKind(String useKind) {
        UseKind = useKind;
    }
    public String getPropertyKind() {
        return PropertyKind;
    }
    public void setPropertyKind(String propertyKind) {
        PropertyKind = propertyKind;
    }
    public String getDrugElement() {
        return DrugElement;
    }
    public void setDrugElement(String drugElement) {
        DrugElement = drugElement;
    }
    public String getDosage() {
        return Dosage;
    }
    public void setDosage(String dosage) {
        Dosage = dosage;
    }
    public String getIndications() {
        return Indications;
    }
    public void setIndications(String indications) {
        Indications = indications;
    }
    public String getContraindication() {
        return Contraindication;
    }
    public void setContraindication(String contraindication) {
        Contraindication = contraindication;
    }
    public String getNotice() {
        return Notice;
    }
    public void setNotice(String notice) {
        Notice = notice;
    }
    public String getADR() {
        return ADR;
    }
    public void setADR(String aDR) {
        ADR = aDR;
    }
    
}
