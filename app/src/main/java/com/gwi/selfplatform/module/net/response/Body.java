package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

/*
 * 此Body为支付业务返回的response参数
 */
public class Body implements Serializable {
	private String Note;

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}
}
