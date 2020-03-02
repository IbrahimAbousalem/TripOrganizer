package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class NotesRoom {
    private NoteDao noteDao;
    private NotesFirebase notesFirebase;

    @Inject
    public NotesRoom(NoteDao noteDao, NotesFirebase notesFirebase) {
        this.noteDao = noteDao;
        this.notesFirebase = notesFirebase;
    }
    public LiveData<List<Note>> getAllNotes(int tripId){
        return noteDao.getAllNote(tripId);
    }

    public void addNote(Note note, String userId){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            long id = noteDao.addNote(note);
            note.setId(id);
            notesFirebase.addNote(note, userId);
        });
    }

    //send feedback to the user.
    public void updateNote(Note note, String userId){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            noteDao.updateNote(note);
            notesFirebase.updateNote(note, userId);
        });
    }

    public void deleteNote(Note note, String userId){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            noteDao.deleteNote(note);
            notesFirebase.deleteNote(note, userId);
        });
    }

}
