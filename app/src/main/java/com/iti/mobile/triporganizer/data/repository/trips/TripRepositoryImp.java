package com.iti.mobile.triporganizer.data.repository.trips;

import androidx.lifecycle.LiveData;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import java.util.List;

import javax.inject.Inject;

public class TripRepositoryImp implements TripsRepository {
    TripsFirebase tripsFirebase;

    @Inject
    public TripRepositoryImp(TripsFirebase tripsFirebase){
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

    @Override
    public LiveData<List<Trip>> getTripsForUser(String userId) {
       return tripsFirebase.getTripsForUser(userId);
    }
}
