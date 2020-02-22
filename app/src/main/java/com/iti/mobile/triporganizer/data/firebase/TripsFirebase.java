package com.iti.mobile.triporganizer.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.FirestoreConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.inject.Inject;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.NOTES_COLLECTION;
import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.TRIPS_COLLECTION;

@ApplicationScope
public class TripsFirebase {

    private FirebaseFirestore db;
    private NotesFirebase notesFirebase;

    @Inject
    public TripsFirebase(NotesFirebase notesFirebase, FirebaseFirestore db) {
        this.notesFirebase = notesFirebase;
        this.db = db;
    }

    public boolean addTrip(Trip trip) {
        DocumentReference reference = db.collection(TRIPS_COLLECTION).document();
        trip.setId(reference.getId());
        return reference.set(trip).isSuccessful();
    }

    public boolean deleteTrip(Trip trip) {
        DocumentReference reference = db.collection(TRIPS_COLLECTION).document(trip.getId());
        //delete related notes
        db.collection(NOTES_COLLECTION).whereEqualTo(FirestoreConstatnts.tripId, trip.getId()).get().addOnCompleteListener(task -> {
            List<Note> notes = task.getResult().toObjects(Note.class);
            for (Note note : notes){
                notesFirebase.deleteNote(note);
            }
        });
        return reference.delete().isSuccessful();
    }

    public boolean updateTrip(Trip trip) {
        DocumentReference reference = db.collection(TRIPS_COLLECTION).document(trip.getId());
        Map<String, Object> mTrip = new HashMap<>();
        Map<String, Object> mLocation = new HashMap<>();
        mLocation.put(FirestoreConstatnts.endPoint, trip.getLocationData().getEndPoint());
        mLocation.put(FirestoreConstatnts.endAddressName, trip.getLocationData().getEndAddressName());
        mLocation.put(FirestoreConstatnts.startPoint, trip.getLocationData().getStartPoint());
        mLocation.put(FirestoreConstatnts.startAddressName, trip.getLocationData().getStartAddressName());

        mTrip.put(FirestoreConstatnts.id, reference.getId());
        mTrip.put(FirestoreConstatnts.type, trip.getType());
        mTrip.put(FirestoreConstatnts.locationData, mLocation);
        mTrip.put(FirestoreConstatnts.date, trip.getDate());
        mTrip.put(FirestoreConstatnts.isRound, trip.isRound());
        mTrip.put(FirestoreConstatnts.userId, trip.getUserId());
        mTrip.put(FirestoreConstatnts.roundTrip, trip.getRoundTrip());
        mTrip.put(FirestoreConstatnts.status, trip.getStatus());
        return reference.update(mTrip).isSuccessful();
    }

    public LiveData<List<Trip>> getTripsForUser(String userId) {
        MutableLiveData<List<Trip>> listLiveData = new MutableLiveData<>();
        db.collection(TRIPS_COLLECTION).whereEqualTo(FirestoreConstatnts.userId,userId).get().addOnCompleteListener(task -> {
            List<Trip> trips = task.getResult().toObjects(Trip.class);
            listLiveData.postValue(trips);
        });
        return listLiveData;
    }
}
