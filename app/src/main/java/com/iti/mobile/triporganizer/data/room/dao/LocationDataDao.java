package com.iti.mobile.triporganizer.data.room.dao;

import com.iti.mobile.triporganizer.data.room_entity.LocationData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDataDao {
    @Insert
    void addLocationData(LocationData locationData);

    @Update
    void updateLocationData(LocationData locationData);

    @Delete
    void deleteLocationData(LocationData locationData);

    @Query("SELECT * FROM locationData WHERE tripDetailsId =:tripDetailsId")
    LiveData<List<LocationData>> getAllLocationData(int tripDetailsId);
}
