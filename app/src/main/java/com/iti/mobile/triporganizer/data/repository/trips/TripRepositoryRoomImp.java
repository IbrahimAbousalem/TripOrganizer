package com.iti.mobile.triporganizer.data.repository.trips;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.room.NotesRoom;
import com.iti.mobile.triporganizer.data.room.TripsRoom;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class TripRepositoryRoomImp implements TripsRepository {

    private TripsRoom tripsRoom;

    @Inject
    public TripRepositoryRoomImp(TripsRoom tripsRoom) {
        this.tripsRoom = tripsRoom;
    }

    @Override
    public boolean addTrip(Trip trip) {
        tripsRoom.addTrip(trip);
        return true;
    }

    @Override
    public boolean deleteTrip(Trip trip) {
        tripsRoom.deleteTrip(trip);
        return false;
    }

    @Override
    public boolean updateTrip(Trip trip) {
        tripsRoom.updateTrip(trip);
        return false;
    }

    @Override
    public LiveData<List<TripAndLocation>> getUpComingTripsFromRoom(String userId, long date) {
        return tripsRoom.getAllUpComingTrips(userId, date);
    }

    @Override
    public LiveData<List<TripAndLocation>> getHistoryTrips(String userId, long date) {
        return tripsRoom.getAllHistoryTrips(userId, date);
    }

    @Override
    public void getTripsFromFirebase(String userId) {
         tripsRoom.getTripsForUser(userId);
    }

    @Override
    public LiveData<Trip> addTripAndNotes(Trip trip, List<Note> notes) {
        return tripsRoom.addTripAndNotes(trip, notes);
    }

}
