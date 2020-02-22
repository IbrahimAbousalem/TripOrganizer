package com.iti.mobile.triporganizer.data.entities;

import androidx.annotation.Nullable;

public class LocationData {
    private String startAddressName;
    private long startPoint;
    private String endAddressName;
    private long endPoint;

    public LocationData(String startAddressName, long startPoint, String endAddressName, long endPoint) {
        this.startAddressName = startAddressName;
        this.startPoint = startPoint;
        this.endAddressName = endAddressName;
        this.endPoint = endPoint;
    }

    public String getStartAddressName() {
        return startAddressName;
    }

    public void setStartAddressName(String startAddressName) {
        this.startAddressName = startAddressName;
    }

    public long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(long startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndAddressName() {
        return endAddressName;
    }

    public void setEndAddressName(String endAddressName) {
        this.endAddressName = endAddressName;
    }

    public long getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(long endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        LocationData location = (LocationData) obj;
        if(!startAddressName.equals(location.getStartAddressName()) || !endAddressName.equals(location.getEndAddressName())
        || startPoint != location.getStartPoint() || endPoint != location.endPoint){
            return false;
        }
        return true;
    }
}
