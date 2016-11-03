package com.gwi.selfplatform.module.net.beans;

public class KBBodyPart {
    private String Code;
    private String Name;

    // For 智能导诊
    private int IsFront;
    private int IsBack;
    private String HexColor;
    private String NearBy;

    public int getIsFront() {
        return IsFront;
    }

    public int getIsBack() {
        return IsBack;
    }

    public String getHexColor() {
        return HexColor;
    }

    public String getNearBy() {
        return NearBy;
    }

    public void setIsFront(int isFront) {
        IsFront = isFront;
    }

    public void setIsBack(int isBack) {
        IsBack = isBack;
    }

    public void setHexColor(String hexColor) {
        HexColor = hexColor;
    }

    public void setNearBy(String nearBy) {
        NearBy = nearBy;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
