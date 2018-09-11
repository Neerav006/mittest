package com.codefuelindia.mudraitsolution.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchHistory implements Parcelable {

@SerializedName("data")
@Expose
private List<Datum> data = null;

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
    }

    public SearchHistory() {
    }

    protected SearchHistory(Parcel in) {
        this.data = in.createTypedArrayList(Datum.CREATOR);
    }

    public static final Parcelable.Creator<SearchHistory> CREATOR = new Parcelable.Creator<SearchHistory>() {
        @Override
        public SearchHistory createFromParcel(Parcel source) {
            return new SearchHistory(source);
        }

        @Override
        public SearchHistory[] newArray(int size) {
            return new SearchHistory[size];
        }
    };
}