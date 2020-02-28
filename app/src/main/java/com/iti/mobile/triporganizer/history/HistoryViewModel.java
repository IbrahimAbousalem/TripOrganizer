package com.iti.mobile.triporganizer.history;

import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryFirebaseImp;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {
    TripRepositoryFirebaseImp tripRepositoryFirebaseImp;

    @Inject
    public HistoryViewModel(TripRepositoryFirebaseImp tripRepositoryFirebaseImp) {
        this.tripRepositoryFirebaseImp = tripRepositoryFirebaseImp;
    }
}
