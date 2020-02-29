package com.iti.mobile.triporganizer.add_trip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

public class AddTripViewModel extends ViewModel {
    private TripRepositoryRoomImp repo;

    @Inject
    public AddTripViewModel(TripRepositoryRoomImp repo) {
        this.repo = repo;
    }

    public void addTrip(Trip trip){
        repo.addTrip(trip);
    }

    public void updateTrip(Trip trip){
        repo.updateTrip(trip);
    }
    public void deleteTrip(Trip trip){
        repo.deleteTrip(trip);
    }

    public LiveData<Trip> addTripAndNotes(Trip trip, List<Note> notes){
        return repo.addTripAndNotes(trip, notes);
    }

}
