package com.iti.mobile.triporganizer.data.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.FirestoreConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.tripsReference;

public class TripsFirebase {
    public boolean addTrip(Trip trip) {
        DocumentReference reference = tripsReference.document();
        trip.setId(reference.getId());
        return reference.set(trip).isSuccessful();

    }

    public boolean deleteTrip(Trip trip) {
        DocumentReference reference = tripsReference.document(trip.getId());
        return reference.delete().isSuccessful();
    }

    public boolean updateTrip(Trip trip) {
        DocumentReference reference = tripsReference.document(trip.getId());
        Map<String, Object> mTrip = new HashMap<>();
        mTrip.put(FirestoreConstatnts.id, reference.getId());
        mTrip.put(FirestoreConstatnts.type, trip.getType());
        mTrip.put(FirestoreConstatnts.date, trip.getDate());
        mTrip.put(FirestoreConstatnts.endPoint, trip.getEndPoint());
        mTrip.put(FirestoreConstatnts.isRound, trip.isRound());
        mTrip.put(FirestoreConstatnts.userId, trip.getUserId());
        mTrip.put(FirestoreConstatnts.startPoint, trip.getStartPoint());
        mTrip.put(FirestoreConstatnts.roundTrip, trip.getRoundTrip());
        mTrip.put(FirestoreConstatnts.status, trip.getStatus());
        return reference.update(mTrip).isSuccessful();
    }

    public LiveData<List<Trip>> getTripsForUser(String userId) {
        MutableLiveData<List<Trip>> listLiveData = new MutableLiveData<>();
        tripsReference.whereEqualTo(FirestoreConstatnts.userId,userId).get().addOnCompleteListener(task -> {
            List<Trip> trips = task.getResult().toObjects(Trip.class);
            listLiveData.postValue(trips);
        });
        return listLiveData;
    }
}
