package com.iti.mobile.triporganizer.data.room;

import com.google.firebase.firestore.FirebaseFirestore;
import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.MapperClass;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.TRIPS_COLLECTION;

@ApplicationScope
public class TripsRoom {
    private TripDao tripDao;
    private LocationDataDao locationDataDao;
    private TripsFirebase tripsFirebase;
    private NoteDao noteDao;
    private NotesFirebase notesFirebase;
    private FirebaseFirestore db;
    @Inject
    public TripsRoom(FirebaseFirestore db, TripDao tripDao, LocationDataDao locationDataDao, TripsFirebase tripsFirebase, NoteDao noteDao, NotesFirebase notesFirebase) {
        this.tripDao = tripDao;
        this.locationDataDao = locationDataDao;
        this.tripsFirebase = tripsFirebase;
        this.noteDao =noteDao;
        this.notesFirebase = notesFirebase;
        this.db = db;
    }

    public LiveData<List<TripAndLocation>> getAllUpComingTrips(String userId){
        return tripDao.getAllHomeTrips(userId);
    }

    public LiveData<List<TripAndLocation>> getAllHistoryTrips(String userId){
        return tripDao.getAllHistoryTrips(userId);
    }

    public void addTrip(Trip trip){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            long tripId = tripDao.addTrip(trip);
            LocationData locationData = trip.getLocationData();
            locationData.setTripId(tripId);
            long locationId = locationDataDao.addLocationData(locationData);
            trip.setId(tripId);
            locationData.setId(locationId);
            trip.setLocationData(locationData);
            tripsFirebase.addTrip(trip);
        });
    }

    public LiveData<String> updateTripAndNotes(TripAndLocation tripAndLocation, List<Note> notes){
        MutableLiveData<String>  tripMutableLiveData = new MutableLiveData<>();
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            Trip trip = MapperClass.mapTripAndLocationObject(tripAndLocation);
            int tripUpdated = tripDao.updateTrip(trip);
            if(tripUpdated > 0){
                int locationUpdated = locationDataDao.updateLocationData(tripAndLocation.getLocationDataList());
                if(locationUpdated > 0){
                    tripMutableLiveData.postValue("Updated Successfully!");
                    if(notes!=null && !notes.isEmpty()){
                        for(Note note : notes) {
                            note.setTripId(tripAndLocation.getTrip().getId());
                            long id = noteDao.addNote(note);
                            note.setId(id);
                            notesFirebase.addNote(note, trip.getUserId());
                        }
                    }
                }else{
                    tripMutableLiveData.postValue("Updated Failed!");
                }
            }else{
                tripMutableLiveData.postValue("Updated Failed!");
            }
        });
        return tripMutableLiveData;
    }

    public LiveData<Trip> addTripAndNotes(Trip trip, List<Note> notes){
        MutableLiveData<Trip>  tripMutableLiveData = new MutableLiveData<>();
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            long tripId = tripDao.addTrip(trip);
            LocationData locationData = trip.getLocationData();
            locationData.setTripId(tripId);
            long locationId = locationDataDao.addLocationData(locationData);
            trip.setId(tripId);
            locationData.setId(locationId);
            trip.setLocationData(locationData);
            tripMutableLiveData.postValue(trip);
            tripsFirebase.addTrip(trip);
            if(notes!=null && !notes.isEmpty()){
                for(Note note : notes) {
                    note.setTripId(tripId);
                    long id = noteDao.addNote(note);
                    note.setId(id);
                    notesFirebase.addNote(note, trip.getUserId());
                }
            }
        });
        return  tripMutableLiveData;
    }
    //send feedback to the user.
    public void updateTrip(Trip trip){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            int updated = tripDao.updateTrip(trip);
            locationDataDao.updateLocationData(trip.getLocationData());
            tripsFirebase.updateTrip(trip);
        });
    }
    public void deleteTrip(Trip trip){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            int deleted = tripDao.deleteTrip(trip);
            tripsFirebase.deleteTrip(trip);
        });
    }
    public void getTripsForUser(String userId) {
        db.collection(TRIPS_COLLECTION).document(userId).collection("UserTrips").get().addOnCompleteListener(task -> {
            List<Trip> trips = task.getResult().toObjects(Trip.class);
            if(!trips.isEmpty()){
                for(Trip tripObj: trips){
                    addTripInRoom(tripObj);
                }

            }
        });

    }
    public void addTripInRoom(Trip trip){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            long tripId = tripDao.addTrip(trip);
            LocationData locationData = trip.getLocationData();
            locationData.setTripId(tripId);
            long locationId = locationDataDao.addLocationData(locationData);
        });
    }
}
