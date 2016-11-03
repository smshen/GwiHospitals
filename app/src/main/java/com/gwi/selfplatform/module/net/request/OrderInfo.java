package com.gwi.selfplatform.module.net.request;

public class OrderInfo {
	private String HospCode;
	private String PayType;//PayType==>PayType
	private String BusinessType;
	private double Amount;
	private String Account;
	private String UserName;
	private String CardNo;
	private String CardType;
	private String IDCardNo;
	private String PhoneNo;
	private String TerminalNo;
	/**Deprecated*/private String Source;
	private String BusinessRequestData;//BusinessRequestData==>BusinessRequestData

	//2015-06-10 新字段
	private String OrderDescription;
	/** 1：扫码支付 0：其它 */
	private String PayActionType;
	private String HisOrderNo;
	private String OutTradeNo;

	private String PatientID;

	private double DiscountFee;
    private String HospAreaID;

    public double getDiscountFee() {
        return DiscountFee;
    }

    public void setDiscountFee(double discountFee) {
        DiscountFee = discountFee;
    }

    public String getHospAreaID() {
        return HospAreaID;
    }

    public void setHospAreaID(String hospAreaID) {
        HospAreaID = hospAreaID;
    }

    public String getPatientID() {
		return PatientID;
	}

	public void setPatientID(String patientID) {
		PatientID = patientID;
	}

	public String getHisOrderNo() {
		return HisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		HisOrderNo = hisOrderNo;
	}

	public String getOrderDescription() {
		return OrderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		OrderDescription = orderDescription;
	}

	public String getOutTradeNo() {
		return OutTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		OutTradeNo = outTradeNo;
	}

	public String getPayActionType() {
		return PayActionType;
	}

	public void setPayActionType(String payActionType) {
		PayActionType = payActionType;
	}

	public String getSource() {
        return Source;
    }
    public void setSource(String source) {
        Source = source;
    }
    public String getBusinessRequestData() {
		return BusinessRequestData;
	}
	public void setBusinessRequestData(String businessRequestData) {
		BusinessRequestData = businessRequestData;
	}
	
	public String getHospCode() {
		return HospCode;
	}
	public void setHospCode(String hospCode) {
		HospCode = hospCode;
	}
	public String getPayType() {
		return PayType;
	}
	public void setPayType(String payType) {
		PayType = payType;
	}
	public String getBusinessType() {
		return BusinessType;
	}
	public void setBusinessType(String businessType) {
		BusinessType = businessType;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public String getIDCardNo() {
		return IDCardNo;
	}
	public void setIDCardNo(String iDCardNo) {
		IDCardNo = iDCardNo;
	}
	public String getPhoneNo() {
		return PhoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		PhoneNo = phoneNo;
	}
	public String getTerminalNo() {
		return TerminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		TerminalNo = terminalNo;
	}
	
}
