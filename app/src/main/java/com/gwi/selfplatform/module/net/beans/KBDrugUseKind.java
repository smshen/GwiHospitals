package com.gwi.selfplatform.module.net.beans;

/**
 * 药物用途（常用药类型）
 * @author 彭毅
 *
 */
public class KBDrugUseKind extends KBBase{
    private String Code;
    private String Name;
    private String Memo;
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
