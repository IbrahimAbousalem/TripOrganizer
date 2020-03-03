package com.iti.mobile.triporganizer.data.repository.notes;

import androidx.lifecycle.LiveData;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import java.util.List;

import javax.inject.Inject;

public class NotesRepositoryFirebaseImp implements NotesRepository{

    NotesFirebase notesFirebase;
    @Inject
    public NotesRepositoryFirebaseImp(NotesFirebase notesFirebase){
        this.notesFirebase = notesFirebase;
    }

    @Override
    public void addNote(Note note, String userId) {
         notesFirebase.addNote(note, userId);
    }

    @Override
    public void deleteNote(Note note, String userId) {
        notesFirebase.deleteNote(note, userId);
    }

    @Override
    public void updateNote(Note note, String userId) {
        notesFirebase.updateNote(note, userId);
    }

    @Override
    public LiveData<List<Note>> getNotesForTrip(long tripId, String userId) {
       return notesFirebase.getNotesForTrip(tripId, userId);
    }
}
