package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G2510 implements Serializable {
    private String RepNo;
    private String RepName;
    private String RepTypeName;
    private String RepDeptName;
    private String RepTime;
    private String DeptName;
    private String DocName;
    private String Note;
    public String getRepNo() {
        return RepNo;
    }
    public String getRepName() {
        return RepName;
    }
    public String getRepTypeName() {
        return RepTypeName;
    }
    public String getRepDeptName() {
        return RepDeptName;
    }
    public String getRepTime() {
        return RepTime;
    }
    public String getDeptName() {
        return DeptName;
    }
    public String getDocName() {
        return DocName;
    }
    public String getNote() {
        return Note;
    }
    public void setRepNo(String repNo) {
        RepNo = repNo;
    }
    public void setRepName(String repName) {
        RepName = repName;
    }
    public void setRepTypeName(String repTypeName) {
        RepTypeName = repTypeName;
    }
    public void setRepDeptName(String repDeptName) {
        RepDeptName = repDeptName;
    }
    public void setRepTime(String repTime) {
        RepTime = repTime;
    }
    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
    public void setDocName(String docName) {
        DocName = docName;
    }
    public void setNote(String note) {
        Note = note;
    }
    
}
