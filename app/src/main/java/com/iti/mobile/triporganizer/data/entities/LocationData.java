package com.iti.mobile.triporganizer.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "locationData", foreignKeys = @ForeignKey(entity = Trip.class, parentColumns = "id", childColumns = "tripId", onDelete = CASCADE))
public class LocationData implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "locationId")
    private long id;
    @ColumnInfo(name = "tripId", index = true)
    private long tripId;
    private String startTripAddressName;
    private double startTripStartPointLat;
    private double startTripStartPointLng;
    private String startTripEndAddressName;
    private double startTripEndPointLat;
    private double startTripEndPointLng;

    private String roundTripStartAddressName;
    private double roundTripStartPointLat;
    private double roundTripStartPointLng;
    private String roundTripEndAddressName;
    private double roundTripEndPointLat;
    private double roundTripEndPointLng;

    private Date startDate;
    private Date roundDate;

    public LocationData() {
    }

    @Ignore
    public LocationData(long id, long tripId, String startTripAddressName, double startTripStartPointLat, double startTripStartPointLng, String startTripEndAddressName, double startTripEndPointLat, double startTripEndPointLng, String roundTripStartAddressName, double roundTripStartPointLat, double roundTripStartPointLng, String roundTripEndAddressName, double roundTripEndPointLat, double roundTripEndPointLng, Date startDate, Date roundDate) {
        this.id = id;
        this.tripId = tripId;
        this.startTripAddressName = startTripAddressName;
        this.startTripStartPointLat = startTripStartPointLat;
        this.startTripStartPointLng = startTripStartPointLng;
        this.startTripEndAddressName = startTripEndAddressName;
        this.startTripEndPointLat = startTripEndPointLat;
        this.startTripEndPointLng = startTripEndPointLng;
        this.roundTripStartAddressName = roundTripStartAddressName;
        this.roundTripStartPointLat = roundTripStartPointLat;
        this.roundTripStartPointLng = roundTripStartPointLng;
        this.roundTripEndAddressName = roundTripEndAddressName;
        this.roundTripEndPointLat = roundTripEndPointLat;
        this.roundTripEndPointLng = roundTripEndPointLng;
        this.startDate = startDate;
        this.roundDate = roundDate;
    }

    protected LocationData(Parcel in) {
        id = in.readLong();
        tripId = in.readLong();
        startTripAddressName = in.readString();
        startTripStartPointLat = in.readDouble();
        startTripStartPointLng = in.readDouble();
        startTripEndAddressName = in.readString();
        startTripEndPointLat = in.readDouble();
        startTripEndPointLng = in.readDouble();
        roundTripStartAddressName = in.readString();
        roundTripStartPointLat = in.readDouble();
        roundTripStartPointLng = in.readDouble();
        roundTripEndAddressName = in.readString();
        roundTripEndPointLat = in.readDouble();
        roundTripEndPointLng = in.readDouble();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

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

    public double getStartTripStartPointLat() {
        return startTripStartPointLat;
    }

    public void setStartTripStartPointLat(double startTripStartPointLat) {
        this.startTripStartPointLat = startTripStartPointLat;
    }

    public double getStartTripStartPointLng() {
        return startTripStartPointLng;
    }

    public void setStartTripStartPointLng(double startTripStartPointLng) {
        this.startTripStartPointLng = startTripStartPointLng;
    }

    public String getStartTripEndAddressName() {
        return startTripEndAddressName;
    }

    public void setStartTripEndAddressName(String startTripEndAddressName) {
        this.startTripEndAddressName = startTripEndAddressName;
    }

    public double getStartTripEndPointLat() {
        return startTripEndPointLat;
    }

    public void setStartTripEndPointLat(double startTripEndPointLat) {
        this.startTripEndPointLat = startTripEndPointLat;
    }

    public double getStartTripEndPointLng() {
        return startTripEndPointLng;
    }

    public void setStartTripEndPointLng(double startTripEndPointLng) {
        this.startTripEndPointLng = startTripEndPointLng;
    }

    public String getRoundTripStartAddressName() {
        return roundTripStartAddressName;
    }

    public void setRoundTripStartAddressName(String roundTripStartAddressName) {
        this.roundTripStartAddressName = roundTripStartAddressName;
    }

    public double getRoundTripStartPointLat() {
        return roundTripStartPointLat;
    }

    public void setRoundTripStartPointLat(double roundTripStartPointLat) {
        this.roundTripStartPointLat = roundTripStartPointLat;
    }

    public double getRoundTripStartPointLng() {
        return roundTripStartPointLng;
    }

    public void setRoundTripStartPointLng(double roundTripStartPointLng) {
        this.roundTripStartPointLng = roundTripStartPointLng;
    }

    public String getRoundTripEndAddressName() {
        return roundTripEndAddressName;
    }

    public void setRoundTripEndAddressName(String roundTripEndAddressName) {
        this.roundTripEndAddressName = roundTripEndAddressName;
    }

    public double getRoundTripEndPointLat() {
        return roundTripEndPointLat;
    }

    public void setRoundTripEndPointLat(double roundTripEndPointLat) {
        this.roundTripEndPointLat = roundTripEndPointLat;
    }

    public double getRoundTripEndPointLng() {
        return roundTripEndPointLng;
    }

    public void setRoundTripEndPointLng(double roundTripEndPointLng) {
        this.roundTripEndPointLng = roundTripEndPointLng;
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
                || startTripStartPointLat != location.getStartTripStartPointLat() || startTripStartPointLng != location.getStartTripStartPointLng() ||startTripEndPointLat != location.getStartTripEndPointLat() || startTripEndPointLng != location.getStartTripEndPointLng() ||startDate !=location.getStartDate())) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tripId);
        dest.writeString(startTripAddressName);
        dest.writeDouble(startTripStartPointLat);
        dest.writeDouble(startTripStartPointLng);
        dest.writeString(startTripEndAddressName);
        dest.writeDouble(startTripEndPointLat);
        dest.writeDouble(startTripEndPointLng);
        dest.writeString(roundTripStartAddressName);
        dest.writeDouble(roundTripStartPointLat);
        dest.writeDouble(roundTripStartPointLng);
        dest.writeString(roundTripEndAddressName);
        dest.writeDouble(roundTripEndPointLat);
        dest.writeDouble(roundTripEndPointLng);
    }
}
