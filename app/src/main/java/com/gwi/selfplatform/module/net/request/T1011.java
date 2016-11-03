package com.gwi.selfplatform.module.net.request;


public class T1011 extends TBody{
	private String SecurityNo;
	private String Passworid;
	//兼容处理Passworid ==> Password.
	private String Password;
	private String Note;
	
	public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getSecurityNo() {
		return SecurityNo;
	}
	public void setSecurityNo(String securityNo) {
		SecurityNo = securityNo;
	}
	public String getPassworid() {
		return Passworid;
	}
	public void setPassworid(String passworid) {
		Passworid = passworid;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
}
