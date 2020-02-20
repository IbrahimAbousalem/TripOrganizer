package com.iti.mobile.triporganizer.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryImp;

import java.util.List;

public class TripsViewModel extends ViewModel {
    //TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();
    LiveData<List<Trip>> tripsListLiveData = new MutableLiveData<>();
}
