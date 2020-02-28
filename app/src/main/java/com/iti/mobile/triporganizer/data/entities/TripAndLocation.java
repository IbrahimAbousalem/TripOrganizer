package com.iti.mobile.triporganizer.data.entities;

import androidx.room.Embedded;

public class TripAndLocation {
    @Embedded
    private Trip trip;

    @Embedded
    private LocationData locationDataList;

    public TripAndLocation() {
    }

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
}
