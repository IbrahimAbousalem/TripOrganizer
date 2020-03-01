package com.iti.mobile.triporganizer.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.MapperClass;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.room.TripOrganizerDatabase;
import com.iti.mobile.triporganizer.data.room.TripsRoom;
import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;
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
        DocumentReference reference = db.collection(TRIPS_COLLECTION).document(trip.getUserId()).collection("UserTrips").document(String.valueOf(trip.getId()));
        return reference.set(trip).isSuccessful();
    }

    public boolean deleteTrip(Trip trip) {
        DocumentReference reference = db.collection(TRIPS_COLLECTION).document(trip.getUserId()).collection("UserTrips").document(String.valueOf(trip.getId()));

        //delete related notes
        db.collection(NOTES_COLLECTION).document(String.valueOf(trip.getId())).collection(String.valueOf(trip.getId())).get().addOnCompleteListener(task -> {
            List<Note> notes = task.getResult().toObjects(Note.class);
            for (Note note : notes){
                notesFirebase.deleteNote(note, trip.getUserId());
            }
        });
        return reference.delete().isSuccessful();
    }

    public boolean updateTrip(Trip trip) {

        DocumentReference reference = db.collection(TRIPS_COLLECTION).document(trip.getUserId()).collection("UserTrips").document(String.valueOf(trip.getId()));

        Map<String, Object> mTrip = new HashMap<>();
        Map<String, Object> mLocationOne = new HashMap<>();

        mLocationOne.put(FirestoreConstatnts.tripId,trip.getLocationData().getTripId());
        mLocationOne.put(FirestoreConstatnts.startTripEndPointLat, trip.getLocationData().getStartTripEndPointLat());
        mLocationOne.put(FirestoreConstatnts.startTripEndPointLng, trip.getLocationData().getStartTripEndPointLng());
        mLocationOne.put(FirestoreConstatnts.startTripEndAddressName, trip.getLocationData().getStartTripEndAddressName());
        mLocationOne.put(FirestoreConstatnts.startTripStartPointLat, trip.getLocationData().getStartTripStartPointLat());
        mLocationOne.put(FirestoreConstatnts.startTripStartPointLng, trip.getLocationData().getStartTripStartPointLng());
        mLocationOne.put(FirestoreConstatnts.startTripStartAddressName, trip.getLocationData().getStartTripAddressName());
        mLocationOne.put(FirestoreConstatnts.startDate, trip.getLocationData().getStartDate());
        mLocationOne.put(FirestoreConstatnts.id, trip.getLocationData().getId());

        mLocationOne.put(FirestoreConstatnts.roundTripEndPointLat, trip.getLocationData().getRoundTripEndPointLat());
        mLocationOne.put(FirestoreConstatnts.roundTripEndPointLng, trip.getLocationData().getRoundTripEndPointLng());
        mLocationOne.put(FirestoreConstatnts.roundTripEndAddressName, trip.getLocationData().getRoundTripEndAddressName());
        mLocationOne.put(FirestoreConstatnts.roundTripStartPointLat, trip.getLocationData().getRoundTripStartPointLat());
        mLocationOne.put(FirestoreConstatnts.roundTripStartPointLng, trip.getLocationData().getRoundTripStartPointLng());
        mLocationOne.put(FirestoreConstatnts.roundTripStartAddressName, trip.getLocationData().getRoundTripStartAddressName());
        mLocationOne.put(FirestoreConstatnts.roundDate, trip.getLocationData().getRoundDate());
        mLocationOne.put(FirestoreConstatnts.isRound, trip.getLocationData().isRound());

        mTrip.put(FirestoreConstatnts.id, trip.getId());
        mTrip.put(FirestoreConstatnts.userId, trip.getUserId());
        mTrip.put(FirestoreConstatnts.locationData, mLocationOne);
        mTrip.put(FirestoreConstatnts.tripName, trip.getTripName());
        mTrip.put(FirestoreConstatnts.status, trip.getStatus());

        return reference.update(mTrip).isSuccessful();
    }

}
