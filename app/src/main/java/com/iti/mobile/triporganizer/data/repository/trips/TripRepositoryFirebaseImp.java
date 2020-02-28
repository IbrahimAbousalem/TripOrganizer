package com.iti.mobile.triporganizer.data.repository.trips;

import androidx.lifecycle.LiveData;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import java.util.List;

import javax.inject.Inject;

public class TripRepositoryFirebaseImp implements TripsRepository {
    TripsFirebase tripsFirebase;

    @Inject
    public TripRepositoryFirebaseImp(TripsFirebase tripsFirebase){
        this.tripsFirebase = tripsFirebase;
    }

    @Override
    public boolean addTrip(Trip trip) {
      return tripsFirebase.addTrip(trip);
    }

    @Override
    public boolean deleteTrip(Trip trip) {
       return tripsFirebase.deleteTrip(trip);
    }

    @Override
    public boolean updateTrip(Trip trip) {
       return tripsFirebase.updateTrip(trip);
    }

    //think more about this..
    @Override
    public LiveData<List<TripAndLocation>> getTripsFromRoom(String userId) {
        return null;
    }

    @Override
    public LiveData<List<Trip>> getTripsFromFirebase(String userId) {
        return tripsFirebase.getTripsForUser(userId);
    }

    @Override
    public void addTripAndNotes(Trip trip, List<Note> notes) {

    }

}
