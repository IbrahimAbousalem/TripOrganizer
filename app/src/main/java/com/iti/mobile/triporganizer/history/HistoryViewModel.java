package com.iti.mobile.triporganizer.history;

import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryFirebaseImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {
    TripRepositoryFirebaseImp tripRepositoryFirebaseImp;
    TripRepositoryRoomImp tripRepositoryRoomImp;
    AuthenticationRepository auth;

    @Inject
    public HistoryViewModel(AuthenticationRepositoryImp auth, TripRepositoryFirebaseImp tripRepositoryFirebaseImp, TripRepositoryRoomImp tripRepositoryRoomImp) {
        this.tripRepositoryFirebaseImp = tripRepositoryFirebaseImp;
        this.tripRepositoryRoomImp = tripRepositoryRoomImp;
        this.auth = auth;
    }

    public LiveData<List<TripAndLocation>> getAllHistoryTrips(String userId, long date){
        return tripRepositoryRoomImp.getHistoryTrips(userId, date);
    }

    public String getCurrentUserId(){
        return auth.getCurrentUserId();
    }

}
