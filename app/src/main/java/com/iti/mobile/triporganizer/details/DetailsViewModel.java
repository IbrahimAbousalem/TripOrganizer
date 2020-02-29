package com.iti.mobile.triporganizer.details;

import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

public class DetailsViewModel extends ViewModel {
    private TripRepositoryRoomImp repo;

    @Inject
    public DetailsViewModel(TripRepositoryRoomImp repo) {
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

    public void addTripAndNotes(Trip trip, List<Note> notes){
        repo.addTripAndNotes(trip, notes);
    }
}
