package com.iti.mobile.triporganizer.data.repository.notes;

import androidx.lifecycle.LiveData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import java.util.List;
public class NotesRepositoryImp implements NotesRepository{

    NotesFirebase notesFirebase;
    public NotesRepositoryImp(NotesFirebase notesFirebase){
        this.notesFirebase = notesFirebase;
    }
    @Override
    public boolean addNote(Note note) {

        return notesFirebase.addNote(note);
    }

    @Override
    public boolean deleteNote(Note note) {
        return notesFirebase.deleteNote(note);
    }

    @Override
    public boolean updateNote(Note note) {
        return notesFirebase.updateNote(note);
    }

    @Override
    public LiveData<List<Note>> getNotesForTrip(String tripId) {
       return notesFirebase.getNotesForTrip(tripId);
    }
}
