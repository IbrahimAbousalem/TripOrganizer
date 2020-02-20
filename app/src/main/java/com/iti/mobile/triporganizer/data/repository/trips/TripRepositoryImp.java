package com.iti.mobile.triporganizer.data.repository.trips;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.utils.FirestoreConstatnts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.tripsReference;

public class TripRepositoryImp implements TripsRepository {

    @Override
    public boolean addTrip(Trip trip) {
        DocumentReference reference = tripsReference.document();
        trip.setId(reference.getId());
        /*
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
         */
        return reference.set(trip).isSuccessful();

    }

    @Override
    public boolean deleteTrip(Trip trip) {
        DocumentReference reference = tripsReference.document(trip.getId());
        return reference.delete().isSuccessful();
    }

    @Override
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

    @Override
    public LiveData<List<Trip>> getTripsForUser(String userId) {
        MutableLiveData<List<Trip>> listLiveData = new MutableLiveData<>();
        tripsReference.whereEqualTo(FirestoreConstatnts.userId,userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Trip> trips = task.getResult().toObjects(Trip.class);
                listLiveData.postValue(trips);
            }
        });
        return listLiveData;
    }
}
