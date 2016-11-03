package com.gwi.selfplatform.module.net.response;

import android.os.Parcel;
import android.os.Parcelable;

public class G1017 implements Parcelable {
    private String SubHospCode;
    private String HospName;
    private String HospLevelCode;
    private String HospLevel;
    private String Rules;
    private String Introduction;
    private String Feature;
    private String Telephone;
    private String Address;
    private String HomeUrl;
    
    public G1017() {
        
    }

    public static final Parcelable.Creator<G1017> CREATOR = new Parcelable.Creator<G1017>() {
        public G1017 createFromParcel(Parcel in) {
            return new G1017(in);
        }

        public G1017[] newArray(int size) {
            return new G1017[size];
        }
    };

    private G1017(Parcel in) {
        SubHospCode = in.readString();
        HospName = in.readString();
        HospLevelCode = in.readString();
        HospLevel = in.readString();
        Rules = in.readString();
        Introduction = in.readString();
        Feature = in.readString();
        Telephone = in.readString();
        Address = in.readString();
        HomeUrl = in.readString();
    }

    public String getSubHospCode() {
        return SubHospCode;
    }

    public void setSubHospCode(String subHospCode) {
        SubHospCode = subHospCode;
    }

    public String getHospName() {
        return HospName;
    }

    public void setHospName(String hospName) {
        HospName = hospName;
    }

    public String getHospLevelCode() {
        return HospLevelCode;
    }

    public void setHospLevelCode(String hospLevelCode) {
        HospLevelCode = hospLevelCode;
    }

    public String getHospLevel() {
        return HospLevel;
    }

    public void setHospLevel(String hospLevel) {
        HospLevel = hospLevel;
    }

    public String getRules() {
        return Rules;
    }

    public void setRules(String rules) {
        Rules = rules;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getHomeUrl() {
        return HomeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        HomeUrl = homeUrl;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SubHospCode);
        dest.writeString(HospName);
        dest.writeString(HospLevelCode);
        dest.writeString(HospLevel);
        dest.writeString(Rules);
        dest.writeString(Introduction);
        dest.writeString(Feature);
        dest.writeString(Telephone);
        dest.writeString(Address);
        dest.writeString(HomeUrl);
    }

}
