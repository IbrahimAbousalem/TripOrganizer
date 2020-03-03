package com.iti.mobile.triporganizer.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.repository.notes.NoteRepositoryRoomImp;
import com.iti.mobile.triporganizer.data.repository.trips.TripRepositoryRoomImp;

import java.util.List;

import javax.inject.Inject;

public class DetailsViewModel extends ViewModel {
    private TripRepositoryRoomImp repo;
    private NoteRepositoryRoomImp noteRepo;

    @Inject
    public DetailsViewModel(TripRepositoryRoomImp repo, NoteRepositoryRoomImp noteRepo) {
        this.repo = repo;
        this.noteRepo = noteRepo;
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

    public LiveData<String> updateTripAndNotes(TripAndLocation trip, List<Note> notes){
         return repo.updateTripAndNotes(trip, notes);
    }

    public LiveData<List<Note>> getAllNotes(long tripId){
        return noteRepo.getNotesForTrip(tripId, "");
    }
}
