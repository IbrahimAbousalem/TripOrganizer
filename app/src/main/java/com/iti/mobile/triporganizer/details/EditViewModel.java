package com.iti.mobile.triporganizer.details;

import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

public class EditViewModel extends ViewModel {
    private TripRepositoryRoomImp repo;

    @Inject
    public EditViewModel(TripRepositoryRoomImp repo) {
        this.repo = repo;
    }
    public void updateTrip(Trip trip){
        repo.updateTrip(trip);
    }

}
