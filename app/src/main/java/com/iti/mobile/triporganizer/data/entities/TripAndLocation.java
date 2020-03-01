package com.iti.mobile.triporganizer.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;

import java.io.Serializable;

public class TripAndLocation implements Serializable {
    @Embedded
    private Trip trip;

    @Embedded
    private LocationData locationDataList;

    public TripAndLocation() {
    }

    protected TripAndLocation(Parcel in) {
        trip = in.readParcelable(Trip.class.getClassLoader());
        locationDataList = in.readParcelable(LocationData.class.getClassLoader());
    }

//    public static final Creator<TripAndLocation> CREATOR = new Creator<TripAndLocation>() {
//        @Override
//        public TripAndLocation createFromParcel(Parcel in) {
//            return new TripAndLocation(in);
//        }
//
//        @Override
//        public TripAndLocation[] newArray(int size) {
//            return new TripAndLocation[size];
//        }
//    };

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public LocationData getLocationDataList() {
        return locationDataList;
    }

    public void setLocationDataList(LocationData locationDataList) {
        this.locationDataList = locationDataList;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(trip, flags);
//        dest.writeParcelable(locationDataList, flags);
//    }
}
