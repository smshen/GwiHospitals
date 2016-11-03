package com.gwi.selfplatform.module.net.response;

public class G1911 {
    private String ItemDate;
    private String ItemName;
    private String ItemSpec;
    private String ItemUnit;
    private double ItemPrice;
    private int ItemQuantity;
    private double TotalFee;
    private String ItemLocation;
    private String Note;

    public String getItemSpec() {
        return ItemSpec;
    }

    public void setItemSpec(String itemSpec) {
        ItemSpec = itemSpec;
    }

    public String getItemDate() {
        return ItemDate;
    }

    public void setItemDate(String itemDate) {
        ItemDate = itemDate;
    }

    public String getItemLocation() {
        return ItemLocation;
    }

    public void setItemLocation(String itemLocation) {
        ItemLocation = itemLocation;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(double itemPrice) {
        ItemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public String getItemUnit() {
        return ItemUnit;
    }

    public void setItemUnit(String itemUnit) {
        ItemUnit = itemUnit;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public double getTotalFee() {
        return TotalFee;
    }

    public void setTotalFee(double totalFee) {
        TotalFee = totalFee;
    }
}
