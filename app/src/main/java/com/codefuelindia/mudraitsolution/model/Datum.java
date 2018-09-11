package com.codefuelindia.mudraitsolution.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable {

@SerializedName("Customermaster")
@Expose
private Customermaster customermaster;

public Customermaster getCustomermaster() {
return customermaster;
}

public void setCustomermaster(Customermaster customermaster) {
this.customermaster = customermaster;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.customermaster, flags);
    }

    public Datum() {
    }

    protected Datum(Parcel in) {
        this.customermaster = in.readParcelable(Customermaster.class.getClassLoader());
    }

    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}