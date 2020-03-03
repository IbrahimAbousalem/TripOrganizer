package com.iti.mobile.triporganizer.data.repository.notes;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.room.NotesRoom;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class NoteRepositoryRoomImp implements NotesRepository {
    private NotesRoom notesRoom;
    @Inject
    public NoteRepositoryRoomImp(NotesRoom notesRoom) {
        this.notesRoom = notesRoom;
    }

    @Override
    public void addNote(Note note, String userId) {
        notesRoom.addNote(note, userId);
    }

    @Override
    public void deleteNote(Note note, String userId) {
        notesRoom.deleteNote(note, userId);
    }

    @Override
    public void updateNote(Note note, String userId) {
        notesRoom.updateNote(note, userId);
    }

    @Override
    public LiveData<List<Note>> getNotesForTrip(long tripId, String userId) {
        return notesRoom.getAllNotes(tripId);
    }
}
