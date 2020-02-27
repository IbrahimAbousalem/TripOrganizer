package com.iti.mobile.triporganizer.data.room;

import android.content.Context;

import com.iti.mobile.triporganizer.data.room_entity.LocationData;
import com.iti.mobile.triporganizer.data.room_entity.Trip;
import com.iti.mobile.triporganizer.data.room_entity.TripData;
import com.iti.mobile.triporganizer.data.room_entity.User;
import com.iti.mobile.triporganizer.utils.DateConverter;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {User.class, Trip.class, TripData.class, LocationData.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
abstract public class TripOrganizerDatabase extends RoomDatabase {
    private static TripOrganizerDatabase mInstance;

    public static TripOrganizerDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TripOrganizerDatabase.class) {
                if(mInstance!=null){
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),
                            TripOrganizerDatabase.class,
                            "TripOrganizerDB")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return mInstance;
    }
}
