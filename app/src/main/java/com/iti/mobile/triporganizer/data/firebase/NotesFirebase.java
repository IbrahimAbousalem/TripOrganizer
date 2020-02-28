package com.iti.mobile.triporganizer.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.utils.FirestoreConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.NOTES_COLLECTION;
import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.TRIPS_COLLECTION;

@ApplicationScope
public class NotesFirebase {

    private FirebaseFirestore db;

    @Inject
    public NotesFirebase(FirebaseFirestore db) {
        this.db =db;
    }

    public boolean addNote(Note note, String userId) {
        DocumentReference reference = db.collection(NOTES_COLLECTION).document(userId).collection(String.valueOf(note.getTripId())).document(String.valueOf(note.getId()));
        return reference.set(note).isSuccessful();
    }

    public boolean deleteNote(Note note, String userId) {
        DocumentReference reference = db.collection(NOTES_COLLECTION).document(userId).collection(String.valueOf(note.getTripId())).document(String.valueOf(note.getId()));
        return reference.delete().isSuccessful();
    }

    public boolean updateNote(Note note, String userId) {
        DocumentReference reference = db.collection(NOTES_COLLECTION).document(userId).collection(String.valueOf(note.getTripId())).document(String.valueOf(note.getId()));
        Map<String, Object> mTrip = new HashMap<>();
        mTrip.put(FirestoreConstatnts.id, reference.getId());
        mTrip.put(FirestoreConstatnts.message, note.getMessage());
        mTrip.put(FirestoreConstatnts.tripId, note.getTripId());
        mTrip.put(FirestoreConstatnts.status, note.getStatus());
        return reference.update(mTrip).isSuccessful();
    }

    //Need to look for the id..
    public LiveData<List<Note>> getNotesForTrip(int tripId, String userId) {
        MutableLiveData<List<Note>> listLiveData = new MutableLiveData<>();
        db.collection(NOTES_COLLECTION).document(userId).collection(String.valueOf(tripId)).get().addOnCompleteListener(task -> {
            List<Note> notes = task.getResult().toObjects(Note.class);
            listLiveData.postValue(notes);
        });
        return listLiveData;
    }
}
