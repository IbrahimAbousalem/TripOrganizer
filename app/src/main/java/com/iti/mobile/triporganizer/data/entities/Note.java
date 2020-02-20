package com.iti.mobile.triporganizer.data.entities;

public class Note {
    private String message;
    private String tripId;
    private String status;
    private String id;

    public Note() {
    }

    public Note(String message, String tripId, String status, String id) {
        this.message = message;
        this.tripId = tripId;
        this.status = status;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
