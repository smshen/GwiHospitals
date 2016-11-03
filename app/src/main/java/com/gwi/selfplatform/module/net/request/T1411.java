package com.gwi.selfplatform.module.net.request;


public class T1411 extends TBody{
    private String TypeID;
    private String ParDeptID;
    private String Note;
    private String PinYinCode;
    private String Date;
    private String SubHospCode;
    
    public String getSubHospCode() {
        return SubHospCode;
    }
    public void setSubHospCode(String subHospCode) {
        SubHospCode = subHospCode;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getTypeID() {
        return TypeID;
    }
    public void setTypeID(String typeID) {
        TypeID = typeID;
    }
    public String getParDeptID() {
        return ParDeptID;
    }
    public void setParDeptID(String parDeptID) {
        ParDeptID = parDeptID;
    }
    public String getNote() {
        return Note;
    }
    public void setNote(String note) {
        Note = note;
    }
    public String getPinYinCode() {
        return PinYinCode;
    }
    public void setPinYinCode(String pinYinCode) {
        PinYinCode = pinYinCode;
    }
    
}
