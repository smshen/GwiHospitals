package com.gwi.selfplatform.module.net.request;


public class T1412 extends TBody{
	private String BeginDate;
	private String EndDate;
	private String TypeID;
	private String DeptID;
	private String SubHospCode;
	private String DoctID;

    public String getDoctID() {
        return DoctID;
    }

    public void setDoctID(String doctID) {
        DoctID = doctID;
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
    public String getTypeID() {
        return TypeID;
    }
    public void setTypeID(String typeID) {
        TypeID = typeID;
    }
    public String getDeptID() {
        return DeptID;
    }
    public void setDeptID(String deptID) {
        DeptID = deptID;
    }
    public String getSubHospCode() {
        return SubHospCode;
    }
    public void setSubHospCode(String deptID) {
    	SubHospCode = deptID;
    }	
}
