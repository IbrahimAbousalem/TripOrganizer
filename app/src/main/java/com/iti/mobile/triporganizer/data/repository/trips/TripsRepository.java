package com.iti.mobile.triporganizer.data.repository.trips;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface TripsRepository {
    boolean addTrip(Trip trip);
    boolean deleteTrip(Trip trip);
    boolean  updateTrip(Trip trip);
    LiveData<List<TripAndLocation>> getUpComingTripsFromRoom(String userId, long date);
    void getTripsFromFirebase(String userId);
    LiveData<Trip> addTripAndNotes(Trip trip, List<Note> notes);
    LiveData<List<TripAndLocation>> getHistoryTrips(String userId, long date);
}
