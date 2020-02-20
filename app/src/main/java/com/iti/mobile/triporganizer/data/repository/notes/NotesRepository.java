package com.iti.mobile.triporganizer.data.repository.notes;

import androidx.lifecycle.LiveData;
import com.iti.mobile.triporganizer.data.entities.Note;
import java.util.List;

public interface NotesRepository {
    boolean addNote(Note note);
    boolean deleteNote(Note note);
    boolean  updateNote(Note note);
    LiveData<List<Note>> getNotesForTrip(String tripId);
}
