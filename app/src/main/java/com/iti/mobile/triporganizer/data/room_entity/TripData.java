package com.iti.mobile.triporganizer.data.room_entity;

import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.User;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "tripData", foreignKeys = @ForeignKey(entity = Trip.class, parentColumns = "id", childColumns = "tripId", onDelete = CASCADE))
public class TripData {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "tripId", index = true)
    private int tripId;
    @Ignore
    private LocationData locationData;
    private Date date;

    public TripData() {
    }

    public TripData(int id, int tripId, LocationData locationData, Date date) {
        this.id = id;
        this.tripId = tripId;
        this.locationData = locationData;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
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
}
