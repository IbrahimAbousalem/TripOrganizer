package com.iti.mobile.triporganizer.data.entities;

import java.util.Date;

public class Trip {

    private String id;
    private String userId;
    private long startPoint;
    private long endPoint;
    private Date date;
    private String type;
    private String status;
    private Trip roundTrip;
    private boolean isRound;

    public Trip() {
    }

    public Trip(String id, String userId, long startPoint, long endPoint, Date date, String type, String status, Trip roundTrip, boolean isRound) {
        this.id = id;
        this.userId = userId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
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

    public long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(long startPoint) {
        this.startPoint = startPoint;
    }

    public long getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(long endPoint) {
        this.endPoint = endPoint;
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
}
