package com.iti.mobile.triporganizer.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.firebase.AuthenticationFirebase;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepository;
import com.iti.mobile.triporganizer.data.repository.auth.AuthenticationRepositoryImp;
import com.iti.mobile.triporganizer.data.repository.notes.NoteRepositoryRoomImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryFirebaseImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

public class TripsViewModel extends ViewModel {
    private TripRepositoryFirebaseImp tripRepositoryFirebaseImp;
    private TripRepositoryRoomImp tripRepositoryRoomImp;
    private NoteRepositoryRoomImp noteRepositoryRoomImp;
    private AuthenticationRepository auth;
    @Inject
    public TripsViewModel(AuthenticationRepositoryImp auth, TripRepositoryFirebaseImp tripRepositoryFirebaseImp, TripRepositoryRoomImp tripRepositoryRoomImp, NoteRepositoryRoomImp noteRepositoryRoomImp) {
        this.tripRepositoryFirebaseImp = tripRepositoryFirebaseImp;
        this.tripRepositoryRoomImp = tripRepositoryRoomImp;
        this.noteRepositoryRoomImp = noteRepositoryRoomImp;
        this.auth = auth;
    }

    public LiveData<List<TripAndLocation>> getUpComingTripsFromRoom(String uId){
        return tripRepositoryRoomImp.getUpComingTripsFromRoom(uId);
    }

    public void addTrip(Trip trip){
        tripRepositoryRoomImp.addTrip(trip);
    }

    public void updateTrip(Trip trip){
        tripRepositoryRoomImp.updateTrip(trip);
    }
    public void deleteTrip(Trip trip){
        tripRepositoryRoomImp.deleteTrip(trip);
    }

    public void addNote(Note note, String userId){
        noteRepositoryRoomImp.addNote(note,userId);
    }

    public void updateNote(Note note, String userId){
        noteRepositoryRoomImp.updateNote(note,userId);
    }
    public void deleteNote(Note note, String userId){
        noteRepositoryRoomImp.deleteNote(note, userId);
    }
    public String getCurrentUserId(){
        return auth.getCurrentUserId();
    }

    public LiveData<List<TripAndLocation>> getTripsFromFirebase(String userId){
        return tripRepositoryRoomImp.getTripsFromFirebase(userId);
    }

}
