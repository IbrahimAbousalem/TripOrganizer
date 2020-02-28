package com.iti.mobile.triporganizer.data.repository.notes;

import com.iti.mobile.triporganizer.data.entities.Note;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface NotesRepository {
    void addNote(Note note, String userId);
    void deleteNote(Note note, String userId);
    void  updateNote(Note note, String userId);
    LiveData<List<Note>> getNotesForTrip(int tripId, String userId);
}
