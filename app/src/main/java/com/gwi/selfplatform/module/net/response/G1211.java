package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1211 implements Serializable{
	
	private static final long serialVersionUID = 1211L;
	private String DeptID;
	private String DeptName;
	private int HasChildrenDept;
	private String Note;
	private String Location;
	private String Introduction;
	private String PinYinCode;
	
	private String ParDeptID;
	
	
	public String getParDeptID() {
        return ParDeptID;
    }
    public void setParDeptID(String parDeptID) {
        ParDeptID = parDeptID;
    }
    public String getPinYinCode() {
        return PinYinCode;
    }
    public void setPinYinCode(String pinYinCode) {
        PinYinCode = pinYinCode;
    }
    public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getIntroduction() {
		return Introduction;
	}
	public void setIntroduction(String introduction) {
		Introduction = introduction;
	}
	public String getDeptID() {
		return DeptID;
	}
	public void setDeptID(String deptID) {
		DeptID = deptID;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	public int getHasChildrenDept() {
		return HasChildrenDept;
	}
	public void setHasChildrenDept(int hasChildrenDept) {
		HasChildrenDept = hasChildrenDept;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	
}
