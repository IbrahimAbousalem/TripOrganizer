package com.iti.mobile.triporganizer.data.entities;

public class Note {
    private String message;
    private String tripId;
    private String status;

    public Note() {
    }

    public Note(String message, String tripId, String status) {
        this.message = message;
        this.tripId = tripId;
        this.status = status;
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
}
