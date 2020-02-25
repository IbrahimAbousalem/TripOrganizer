package com.iti.mobile.triporganizer.data.room_entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "locationData", foreignKeys = @ForeignKey(entity = TripData.class,  parentColumns = "id", childColumns = "tripDetailsId", onDelete = CASCADE))
public class LocationData {
    @NonNull
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "tripDetailsId", index = true)
    private int tripDetailsId;
    private String startAddressName;
    private long startPoint;
    private String endAddressName;
    private long endPoint;

    public LocationData() {
    }

    @Ignore
    public LocationData(int id, int tripDetailsId, String startAddressName, long startPoint, String endAddressName, long endPoint) {
        this.id = id;
        this.tripDetailsId = tripDetailsId;
        this.startAddressName = startAddressName;
        this.startPoint = startPoint;
        this.endAddressName = endAddressName;
        this.endPoint = endPoint;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripDetailsId() {
        return tripDetailsId;
    }

    public void setTripDetailsId(int tripDetailsId) {
        this.tripDetailsId = tripDetailsId;
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
}
