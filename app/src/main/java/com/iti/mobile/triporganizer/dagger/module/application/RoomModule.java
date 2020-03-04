package com.iti.mobile.triporganizer.dagger.module.application;

import android.app.Application;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.dagger.qualifier.Firebase;
import com.iti.mobile.triporganizer.dagger.qualifier.Room;
import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;
import com.iti.mobile.triporganizer.data.repository.notes.NoteRepositoryRoomImp;
import com.iti.mobile.triporganizer.data.repository.notes.NotesRepository;
import com.iti.mobile.triporganizer.data.repository.notes.NotesRepositoryFirebaseImp;
import com.iti.mobile.triporganizer.data.repository.profile.ProfileRepository;
import com.iti.mobile.triporganizer.data.repository.profile.ProfileRepositoryImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryFirebaseImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripsRepository;
import com.iti.mobile.triporganizer.data.room.NotesRoom;
import com.iti.mobile.triporganizer.data.room.TripOrganizerDatabase;
import com.iti.mobile.triporganizer.data.room.TripsRoom;
import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;
import com.iti.mobile.triporganizer.data.room.dao.UserDao;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private TripOrganizerDatabase database;

    public RoomModule(Application mApplication) {
        database = androidx.room.Room.databaseBuilder(mApplication,
                TripOrganizerDatabase.class,
                "TripOrganizerDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @ApplicationScope
    @Provides
    TripOrganizerDatabase providesRoomDatabase() {
        return database;
    }

    //Daos
    @ApplicationScope
    @Provides
    UserDao provideUserDao(TripOrganizerDatabase database){
        return database.getUserDao();
    }

    @ApplicationScope
    @Provides
    TripDao provideTripDao(TripOrganizerDatabase database){
        return database.getTripDao();
    }


    @ApplicationScope
    @Provides
    LocationDataDao provideLocationDataDao(TripOrganizerDatabase database){
        return database.getLocationDataDao();
    }

    @ApplicationScope
    @Provides
    NoteDao provideNoteDao(TripOrganizerDatabase database){
        return database.getNoteDao();
    }

    //Repos
    @ApplicationScope
    @Provides
    AuthenticationRepository provideAuthentication(AuthenticationFirebase firebase){
        return new AuthenticationRepositoryImp(firebase);
    }

    @ApplicationScope
    @Provides
    ProfileRepository profileRepository(AuthenticationFirebase firebase){
        return new ProfileRepositoryImp(firebase);
    }

    @ApplicationScope
    @Provides
    @Firebase
    TripsRepository provideTripFirebase(TripsFirebase firebase){
        return new TripRepositoryFirebaseImp(firebase);
    }

    @ApplicationScope
    @Provides
    @Room
    TripsRepository provideTripRoom(TripsRoom tripsRoom){
        return new TripRepositoryRoomImp(tripsRoom);
    }

    @ApplicationScope
    @Provides
    NotesRepository provideNotesFirebase(NotesFirebase firebase){
        return new NotesRepositoryFirebaseImp(firebase);
    }

    @ApplicationScope
    @Provides
    @Room
    NotesRepository provideNotesRoom(NotesRoom notesRoom){
        return new NoteRepositoryRoomImp(notesRoom);
    }

}
