package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class G2110 implements Serializable{
	private static final long serialVersionUID = 2110L;
	
	private String RepNo;
	private String RepName;
	private String RepType;
	private String RepTypeName;
	private String RepDeptName;
	private String SendTime;
	private String RepTime;
	private String SendDeptName;
	private String RepDoctName;
	private String CheckDoctName;
	private String Status;
	private String Note;
	private String SampName ;
	public String getRepNo() {
		return RepNo;
	}
	public void setRepNo(String repNo) {
		RepNo = repNo;
	}
	public String getRepName() {
		return RepName;
	}
	public void setRepName(String repName) {
		RepName = repName;
	}
	public String getRepType() {
		return RepType;
	}
	public void setRepType(String repType) {
		RepType = repType;
	}
	public String getRepTypeName() {
		return RepTypeName;
	}
	public void setRepTypeName(String repTypeName) {
		RepTypeName = repTypeName;
	}
	public String getRepDeptName() {
		return RepDeptName;
	}
	public void setRepDeptName(String repDeptName) {
		RepDeptName = repDeptName;
	}
	
	public String getSendTime() {
		return SendTime;
	}
	public void setSendTime(String sendTime) {
		SendTime = sendTime;
	}
	public String getRepTime() {
		return RepTime;
	}
	public void setRepTime(String repTime) {
		RepTime = repTime;
	}
	public String getSendDeptName() {
		return SendDeptName;
	}
	public void setSendDeptName(String sendDeptName) {
		SendDeptName = sendDeptName;
	}
	public String getRepDoctName() {
		return RepDoctName;
	}
	public void setRepDoctName(String repDoctName) {
		RepDoctName = repDoctName;
	}
	public String getCheckDoctName() {
		return CheckDoctName;
	}
	public void setCheckDoctName(String checkDoctName) {
		CheckDoctName = checkDoctName;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}

	public String getSampName() {
		return SampName;
	}

	public void setSampName(String sampName) {
		SampName = sampName;
	}
}
