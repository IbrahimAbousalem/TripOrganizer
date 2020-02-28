package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;
import com.iti.mobile.triporganizer.data.room.dao.UserDao;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.utils.DateConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {User.class, Trip.class, LocationData.class, Note.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
abstract public class TripOrganizerDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public abstract UserDao getUserDao();
    public abstract TripDao getTripDao();
    public abstract LocationDataDao getLocationDataDao();
    public abstract NoteDao getNoteDao();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
}
