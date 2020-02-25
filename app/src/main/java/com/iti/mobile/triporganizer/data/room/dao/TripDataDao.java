package com.iti.mobile.triporganizer.data.room.dao;


import com.iti.mobile.triporganizer.data.room_entity.TripData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TripDataDao {
    @Insert
    void addTripData(TripData tripData);

    @Update
    void updateTripData(TripData tripData);

    @Delete
    void deleteTripData(TripData tripData);

    @Query("SELECT * FROM tripData WHERE tripId =:tripId")
    LiveData<List<TripData>> getTripData(int tripId);
}
