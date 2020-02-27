package com.iti.mobile.triporganizer.data.room_entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "trips",foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))
public class Trip{
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "userId", index = true)
    private String userId;
    private String tripName;
    private String status;
    @Ignore
    private TripData startTrip;
    @Ignore
    private TripData roundTrip;
    private boolean isRound;

    public Trip() {
    }

    public Trip(int id, String userId, String tripName, TripData startTrip, TripData roundTrip, String status, boolean isRound) {
        this.id = id;
        this.userId = userId;
        this.tripName = tripName;
        this.startTrip = startTrip;
        this.roundTrip = roundTrip;
        this.status = status;
        this.isRound = isRound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public TripData getStartTrip() {
        return startTrip;
    }

    public void setStartTrip(TripData startTrip){
        this.startTrip = startTrip;
    }

    public TripData getRoundTrip() {
        return roundTrip;
    }

    public void setRoundTrip(TripData roundTrip){
        this.roundTrip = roundTrip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRound() {
        return isRound;
    }

    public void setRound(boolean round) {
        isRound = round;
    }
}