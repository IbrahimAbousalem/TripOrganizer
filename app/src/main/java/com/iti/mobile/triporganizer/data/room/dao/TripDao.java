package com.iti.mobile.triporganizer.data.room.dao;

import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.utils.Constants;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addTrip(Trip trip);

    @Update
    int updateTrip(Trip trip);

    @Delete
    int deleteTrip(Trip trip);

    @Query("UPDATE trips SET status =:status Where id = :tripId and userId = :userId")
    void updateStatus(long tripId, String userId, String status);

    @Transaction
    @Query("SELECT trips.*, locationData.* FROM trips, locationData WHERE trips.userId =:userId AND trips.status = '"+ Constants.UPCOMING +"' AND locationData.tripId = trips.id")
    LiveData<List<TripAndLocation>> getAllHomeTrips(String userId);

    @Transaction
    @Query("SELECT trips.*, locationData.* FROM trips, locationData WHERE trips.userId =:userId AND trips.status != '" + Constants.UPCOMING +"' AND locationData.tripId = trips.id")
    LiveData<List<TripAndLocation>> getAllHistoryTrips(String userId);

    @Transaction
    @Query("SELECT trips.*, locationData.* FROM trips, locationData WHERE trips.userId =:userId")
    LiveData<List<TripAndLocation>> getAllTripsForUser(String userId);
}
