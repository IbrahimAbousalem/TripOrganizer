package com.iti.mobile.triporganizer.data.room.dao;

import com.iti.mobile.triporganizer.data.entities.LocationData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addLocationData(LocationData locationData);

    @Update
    int updateLocationData(LocationData locationData);

    @Delete
    void deleteLocationData(LocationData locationData);

    @Query("SELECT * FROM locationData WHERE tripId =:tripId")
    LiveData<List<LocationData>> getAllLocationData(int tripId);

}
