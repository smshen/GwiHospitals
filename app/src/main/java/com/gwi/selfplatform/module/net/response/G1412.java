package com.gwi.selfplatform.module.net.response;

import android.os.Parcel;
import android.os.Parcelable;

public class G1412 implements Parcelable{
    String OrderDate;
    String Note;

    public G1412() {
        super();
    }

    public G1412(String orderDate, String note) {
        OrderDate = orderDate;
        Note = note;
    }

    public G1412(Parcel in) {
        OrderDate = in.readString();
        Note = in.readString();
    }

    public String getOrderDate() {
        return OrderDate;
    }
    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }
    public String getNote() {
        return Note;
    }
    public void setNote(String note) {
        Note = note;
    }

    public static final Parcelable.Creator<G1412> CREATOR = new Parcelable.Creator<G1412>() {
        public G1412 createFromParcel(Parcel in) {
            return new G1412(in);
        }

        public G1412[] newArray(int size) {
            return new G1412[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderDate);
        dest.writeString(Note);
    }
	
}
