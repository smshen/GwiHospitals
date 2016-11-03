package com.gwi.selfplatform.module.net.request;

public class T1413 extends TBody {
    private String regHospID;
    private String typeID;
    private String deptID;
    private String orderDate;
    private String note;
    private String BeginDate;
    private String EndDate;
    private String Date;
    private String SubHospCode;
    
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getRegHospID() {
        return regHospID;
    }

    public void setRegHospID(String regHospID) {
        this.regHospID = regHospID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getSubHospCode() {
        return SubHospCode;
    }
    public void setSubHospCode(String deptID) {
    	SubHospCode = deptID;
    }	
}
