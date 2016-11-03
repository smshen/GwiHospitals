package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

/*
 * 此Header为支付业务返回的response参数
 */
public class Header implements Serializable {
	private String FunCode;
	private String Status;
	private String ResultMsg;
	private String OpTime;
	public String getFunCode() {
		return FunCode;
	}
	public void setFunCode(String funCode) {
		FunCode = funCode;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getResultMsg() {
		return ResultMsg;
	}
	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}
	public String getOpTime() {
		return OpTime;
	}
	public void setOpTime(String opTime) {
		OpTime = opTime;
	}
	
}
