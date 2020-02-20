package com.iti.mobile.triporganizer.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryImp;

import java.util.List;

import javax.inject.Inject;

public class TripsViewModel extends ViewModel {
    TripRepositoryImp tripRepositoryImp;

    @Inject
    public TripsViewModel(TripRepositoryImp tripRepositoryImp) {
        this.tripRepositoryImp = tripRepositoryImp;
    }

    public LiveData<List<Trip>> getTripsList(String uId){
        return tripRepositoryImp.getTripsForUser(uId);
    }

}
