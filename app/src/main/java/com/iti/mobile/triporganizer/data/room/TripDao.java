package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.data.room_entity.Trip;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface TripDao {

    @Insert
    void addTrip(Trip trip);

    @Delete
    void deleteTrip(Trip trip);

}
