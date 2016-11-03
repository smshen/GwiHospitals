package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G1612 implements Serializable{
	private static final long serialVersionUID = 1612L;
	
	private String TranSerNo;
	private String ReceiptNo;
	private String Note;
	
	public String getTranSerNo() {
		return TranSerNo;
	}
	public void setTranSerNo(String tranSerNo) {
		TranSerNo = tranSerNo;
	}
	public String getReceiptNo() {
		return ReceiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		ReceiptNo = receiptNo;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	
}
