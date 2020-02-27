package com.iti.mobile.triporganizer.data.room.dao;

import com.iti.mobile.triporganizer.data.room_entity.Trip;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TripDao {

    @Insert
    void addTrip(Trip trip);

    @Update
    void updateTrip(Trip trip);

    @Delete
    void deleteTrip(Trip trip);

    @Query("SELECT * FROM trips WHERE userId =:userId")
    LiveData<List<Trip>> getAllTrips(String userId);
}
