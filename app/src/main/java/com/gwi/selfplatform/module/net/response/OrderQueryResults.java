package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;
import java.util.List;

public class OrderQueryResults implements Serializable{

	private int TotalCount;

	private List<OrderQueryResult>  Items;

	public List<OrderQueryResult> getItems() {
		return Items;
	}

	public void setItems(List<OrderQueryResult> items) {
		Items = items;
	}

	public int getTotalCount() {
		return TotalCount;
	}

	public void setTotalCount(int totalCount) {
		TotalCount = totalCount;
	}

	public static class OrderQueryResult implements Serializable {
		private String OrderNo;
		private String HospCode;
		private String TranType;
		private String BusinessStatus;
		private String BusinessType;
		private String Amount;
		private String Account;
		private String UserName;
		private String CardNo;
		private String CardType;
		private String IDCardNo;
		private String PhoneNo;
		private String TerminalNo;
		private String TradeOrderNo;
		private String OrderStatus;
		private String CreateTime;
		private ResponseXml ResponseXml;

		private String HisStatus;
		private String TranTime;
		private String SellerNo;
		private String SellerAccount;
		private String BuyerNo;
		private String BuyerAccount;
		private String TradeFlowNo;
		private String PayStatus;
		private String RefundStatus;
		private String CancelOrderBatchNo;

		private String RefundType;

		public String getBusinessType() {
			return BusinessType;
		}

		public void setBusinessType(String businessType) {
			BusinessType = businessType;
		}

		public String getRefundType() {
			return RefundType;
		}
		public void setRefundType(String refundType) {
			RefundType = refundType;
		}
		public String getHisStatus() {
			return HisStatus;
		}
		public void setHisStatus(String hisStatus) {
			HisStatus = hisStatus;
		}
		public String getTranTime() {
			return TranTime;
		}
		public void setTranTime(String tranTime) {
			TranTime = tranTime;
		}
		public String getSellerNo() {
			return SellerNo;
		}
		public void setSellerNo(String sellerNo) {
			SellerNo = sellerNo;
		}
		public String getSellerAccount() {
			return SellerAccount;
		}
		public void setSellerAccount(String sellerAccount) {
			SellerAccount = sellerAccount;
		}
		public String getBuyerNo() {
			return BuyerNo;
		}
		public void setBuyerNo(String buyerNo) {
			BuyerNo = buyerNo;
		}
		public String getBuyerAccount() {
			return BuyerAccount;
		}
		public void setBuyerAccount(String buyerAccount) {
			BuyerAccount = buyerAccount;
		}
		public String getTradeFlowNo() {
			return TradeFlowNo;
		}
		public void setTradeFlowNo(String tradeFlowNo) {
			TradeFlowNo = tradeFlowNo;
		}
		public String getPayStatus() {
			return PayStatus;
		}
		public void setPayStatus(String payStatus) {
			PayStatus = payStatus;
		}
		public String getRefundStatus() {
			return RefundStatus;
		}
		public void setRefundStatus(String refundStatus) {
			RefundStatus = refundStatus;
		}
		public String getCancelOrderBatchNo() {
			return CancelOrderBatchNo;
		}
		public void setCancelOrderBatchNo(String cancelOrderBatchNo) {
			CancelOrderBatchNo = cancelOrderBatchNo;
		}
		public String getCreateTime() {
			return CreateTime;
		}
		public void setCreateTime(String createTime) {
			CreateTime = createTime;
		}
		public ResponseXml getResponseXml() {
			return ResponseXml;
		}
		public void setResponseXml(ResponseXml responseXml) {
			ResponseXml = responseXml;
		}
		public String getOrderNo() {
			return OrderNo;
		}
		public void setOrderNo(String orderNo) {
			OrderNo = orderNo;
		}
		public String getHospCode() {
			return HospCode;
		}
		public void setHospCode(String hospCode) {
			HospCode = hospCode;
		}
		public String getTranType() {
			return TranType;
		}
		public void setTranType(String tranType) {
			TranType = tranType;
		}
		public String getBusinessStatus() {
			return BusinessStatus;
		}
		public void setBusinessStatus(String businessStatus) {
			BusinessStatus = businessStatus;
		}
		public String getAmount() {
			return Amount;
		}
		public void setAmount(String amount) {
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
		public String getTradeOrderNo() {
			return TradeOrderNo;
		}
		public void setTradeOrderNo(String tradeOrderNo) {
			TradeOrderNo = tradeOrderNo;
		}
		public String getOrderStatus() {
			return OrderStatus;
		}
		public void setOrderStatus(String orderStatus) {
			OrderStatus = orderStatus;
		}
	}
	
}
