package com.gwi.selfplatform.module.net.response;

import java.util.List;

public class RecipeDetailList {
	private List<RecipeDetail> RecipeDetail;
	
	public List<RecipeDetail> getRecipeDetail() {
		return RecipeDetail;
	}

	public void setRecipeDetail(List<RecipeDetail> recipeDetail) {
		RecipeDetail = recipeDetail;
	}

	class RecipeDetail{
		private String ItemNo;
		private String ItemID;
		private String ItemName;
		private String Specs;
		private String Unit;
		private String Quantity;
		private String UnitPrice;
		private String Fee;
		private String Note;
		public String getItemNo() {
			return ItemNo;
		}
		public void setItemNo(String itemNo) {
			ItemNo = itemNo;
		}
		public String getItemID() {
			return ItemID;
		}
		public void setItemID(String itemID) {
			ItemID = itemID;
		}
		public String getItemName() {
			return ItemName;
		}
		public void setItemName(String itemName) {
			ItemName = itemName;
		}
		public String getSpecs() {
			return Specs;
		}
		public void setSpecs(String specs) {
			Specs = specs;
		}
		public String getUnit() {
			return Unit;
		}
		public void setUnit(String unit) {
			Unit = unit;
		}
		public String getQuantity() {
			return Quantity;
		}
		public void setQuantity(String quantity) {
			Quantity = quantity;
		}
		public String getUnitPrice() {
			return UnitPrice;
		}
		public void setUnitPrice(String unitPrice) {
			UnitPrice = unitPrice;
		}
		public String getFee() {
			return Fee;
		}
		public void setFee(String fee) {
			Fee = fee;
		}
		public String getNote() {
			return Note;
		}
		public void setNote(String note) {
			Note = note;
		}
		
	}
}
