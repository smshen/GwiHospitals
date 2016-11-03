package com.gwi.selfplatform.module.pay.zhifubao;

import java.io.Serializable;

public class Product implements Serializable{
	public String subject;
	public String body;
	public String price;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
