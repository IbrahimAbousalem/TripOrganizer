package com.iti.mobile.triporganizer.data.entities;

import java.util.Date;

import androidx.annotation.Nullable;

public class Trip {

    private String id;
    private String userId;
    private String tripName;
    private LocationData locationData;
    private Date date;
    private String type;
    private String status;
    private Trip roundTrip;
    private boolean isRound;

    public Trip() {
    }

    public Trip(String id, String userId, String tripName, LocationData locationData, Date date, String type, String status, Trip roundTrip, boolean isRound) {
        this.id = id;
        this.userId = userId;
        this.tripName = tripName;
        this.locationData = locationData;
        this.date = date;
        this.type = type;
        this.status = status;
        this.roundTrip = roundTrip;
        this.isRound = isRound;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Trip getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(Trip roundTrip) {
        this.roundTrip = roundTrip;
    }

    public boolean isRound() {
        return isRound;
    }

    public void setRound(boolean round) {
        isRound = round;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Trip data = (Trip) obj;
        if(!tripName.equals(data.getTripName()) || roundTrip != data.getRoundTrip()
            || !locationData.equals(data.locationData) || !date.equals(data.getDate())
            || !type.equals(data.getType()) || !status.equals(data.getStatus())){
            return  false;
        }
        return true;
    }
}
