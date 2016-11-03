package com.gwi.selfplatform.module.net.beans;

public class KBDrugProperty extends KBBase {

    private String Code;
    private String Name;
    private String Memo;
    private String ParentCode;
    private int Status;
    public String getCode() {
        return Code;
    }
    public void setCode(String code) {
        Code = code;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getParentCode() {
        return ParentCode;
    }
    public void setParentCode(String parentCode) {
        ParentCode = parentCode;
    }
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
}
