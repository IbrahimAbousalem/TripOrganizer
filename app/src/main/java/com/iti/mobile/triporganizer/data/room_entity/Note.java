package com.iti.mobile.triporganizer.data.room_entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "notes", foreignKeys = @ForeignKey(entity = Trip.class,  parentColumns = "id", childColumns = "tripId", onDelete = CASCADE))
public class Note {
    @NonNull
    @PrimaryKey
    private int id;
    private String message;
    @ColumnInfo(name = "tripId", index = true)
    private int tripId;
    private boolean status;

    public Note() {
    }

    @Ignore
    public Note(String message, int tripId, boolean status, int id) {
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

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
