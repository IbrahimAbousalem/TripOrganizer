package com.iti.mobile.triporganizer.data.entities;

public class Trip {

    private String id;
    private String userId;
    private String startPoint;
    private String endPoint;
    private String date;
    private String type;
    private String status;

    public Trip() {
    }

    public Trip(String id, String userId, String startPoint, String endPoint, String date, String type, String status) {
        this.id = id;
        this.userId = userId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.type = type;
        this.status = status;
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

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
