package com.iti.mobile.triporganizer.history;

import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryImp;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {
    TripRepositoryImp tripRepositoryImp;

    @Inject
    public HistoryViewModel(TripRepositoryImp tripRepositoryImp) {
        this.tripRepositoryImp = tripRepositoryImp;
    }
}
