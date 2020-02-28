package com.iti.mobile.triporganizer.data.entities;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "locationData", foreignKeys = @ForeignKey(entity = Trip.class, parentColumns = "id", childColumns = "tripId", onDelete = CASCADE))
public class LocationData {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    private long id;
    @ColumnInfo(name = "tripId", index = true)
    private long tripId;
    private String startTripAddressName;
    private long startTripStartPoint;
    private String startTripEndAddressName;
    private long startTripEndPoint;

    private String roundTripStartAddressName;
    private long roundTripStartPoint;
    private String roundTripEndAddressName;
    private long roundTripEndPoint;

    private Date startDate;
    private Date roundDate;

    public LocationData() {
    }

    @Ignore
    public LocationData(long tripId, String startTripAddressName, long startTripStartPoint, String startTripEndAddressName, long startTripEndPoint, String roundTripStartAddressName, long roundTripStartPoint, String roundTripEndAddressName, long roundTripEndPoint, Date startDate, Date roundDate) {
        this.tripId = tripId;
        this.startTripAddressName = startTripAddressName;
        this.startTripStartPoint = startTripStartPoint;
        this.startTripEndAddressName = startTripEndAddressName;
        this.startTripEndPoint = startTripEndPoint;
        this.roundTripStartAddressName = roundTripStartAddressName;
        this.roundTripStartPoint = roundTripStartPoint;
        this.roundTripEndAddressName = roundTripEndAddressName;
        this.roundTripEndPoint = roundTripEndPoint;
        this.startDate = startDate;
        this.roundDate = roundDate;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getStartTripAddressName() {
        return startTripAddressName;
    }

    public void setStartTripAddressName(String startTripAddressName) {
        this.startTripAddressName = startTripAddressName;
    }

    public long getStartTripStartPoint() {
        return startTripStartPoint;
    }

    public void setStartTripStartPoint(long startTripStartPoint) {
        this.startTripStartPoint = startTripStartPoint;
    }

    public String getStartTripEndAddressName() {
        return startTripEndAddressName;
    }

    public void setStartTripEndAddressName(String startTripEndAddressName) {
        this.startTripEndAddressName = startTripEndAddressName;
    }

    public long getStartTripEndPoint() {
        return startTripEndPoint;
    }

    public void setStartTripEndPoint(long startTripEndPoint) {
        this.startTripEndPoint = startTripEndPoint;
    }

    public String getRoundTripStartAddressName() {
        return roundTripStartAddressName;
    }

    public void setRoundTripStartAddressName(String roundTripStartAddressName) {
        this.roundTripStartAddressName = roundTripStartAddressName;
    }

    public long getRoundTripStartPoint() {
        return roundTripStartPoint;
    }

    public void setRoundTripStartPoint(long roundTripStartPoint) {
        this.roundTripStartPoint = roundTripStartPoint;
    }

    public String getRoundTripEndAddressName() {
        return roundTripEndAddressName;
    }

    public void setRoundTripEndAddressName(String roundTripEndAddressName) {
        this.roundTripEndAddressName = roundTripEndAddressName;
    }

    public long getRoundTripEndPoint() {
        return roundTripEndPoint;
    }

    public void setRoundTripEndPoint(long roundTripEndPoint) {
        this.roundTripEndPoint = roundTripEndPoint;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getRoundDate() {
        return roundDate;
    }

    public void setRoundDate(Date roundDate) {
        this.roundDate = roundDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        LocationData location = (LocationData) obj;
        if (location != null && (tripId != location.getTripId() || !startTripAddressName.equals(location.getStartTripAddressName()) || !startTripEndAddressName.equals(location.getStartTripEndAddressName())
                || startTripStartPoint != location.getStartTripStartPoint() || startTripEndPoint != location.getStartTripEndPoint() || startDate !=location.getStartDate())) {
            return false;
        }
        return true;
    }
}
