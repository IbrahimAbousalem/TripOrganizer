package com.iti.mobile.triporganizer.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.utils.FirestoreConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.notesReference;
import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.tripsReference;

public class NotesFirebase {

    public boolean addNote(Note note) {
        DocumentReference reference = notesReference.document();
        note.setId(reference.getId());
        return reference.set(note).isSuccessful();
    }

    public boolean deleteNote(Note note) {
        DocumentReference reference = notesReference.document(note.getId());
        return reference.delete().isSuccessful();
    }

    public boolean updateNote(Note note) {
        DocumentReference reference = tripsReference.document(note.getId());
        Map<String, Object> mTrip = new HashMap<>();
        mTrip.put(FirestoreConstatnts.id, reference.getId());
        mTrip.put(FirestoreConstatnts.message, note.getMessage());
        mTrip.put(FirestoreConstatnts.tripId, note.getTripId());
        mTrip.put(FirestoreConstatnts.status, note.getStatus());
        return reference.update(mTrip).isSuccessful();
    }

    public LiveData<List<Note>> getNotesForTrip(String tripId) {
        MutableLiveData<List<Note>> listLiveData = new MutableLiveData<>();
        tripsReference.whereEqualTo(FirestoreConstatnts.tripId,tripId).get().addOnCompleteListener(task -> {
            List<Note> notes = task.getResult().toObjects(Note.class);
            listLiveData.postValue(notes);
        });
        return listLiveData;
    }
}
