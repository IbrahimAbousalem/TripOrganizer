package com.iti.mobile.triporganizer.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "trips",foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Trip implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "userId", index = true)
    private String userId;
    @Ignore
    private String fireTripId;
    private String tripName;
    private String status;
    @Ignore
    private LocationData locationData;

    private boolean isRound;

    public Trip() {
    }

    public Trip(long id, String userId, String tripName, String status, LocationData locationData, boolean isRound) {
        this.id = id;
        this.userId = userId;
        this.tripName = tripName;
        this.status = status;
        this.locationData = locationData;
        this.isRound = isRound;
    }

    protected Trip(Parcel in) {
        id = in.readLong();
        userId = in.readString();
        fireTripId = in.readString();
        tripName = in.readString();
        status = in.readString();
        locationData = in.readParcelable(LocationData.class.getClassLoader());
        isRound = in.readByte() != 0;
    }

//    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
//        @Override
//        public Trip createFromParcel(Parcel in) {
//            return new Trip(in);
//        }
//
//        @Override
//        public Trip[] newArray(int size) {
//            return new Trip[size];
//        }
//    };

    public String getFireTripId() {
        return fireTripId;
    }

    public void setFireTripId(String fireTripId) {
        this.fireTripId = fireTripId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
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
        return data == null || (tripName.equals(data.getTripName()) && locationData == data.getLocationData()
                && isRound == data.isRound()
                && status.equals(data.getStatus()));
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(id);
//        dest.writeString(userId);
//        dest.writeString(fireTripId);
//        dest.writeString(tripName);
//        dest.writeString(status);
//        dest.writeParcelable(locationData, flags);
//        dest.writeByte((byte) (isRound ? 1 : 0));
//    }
}