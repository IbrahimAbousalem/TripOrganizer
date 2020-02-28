package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.firebase.NotesFirebase;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

@ApplicationScope
public class TripsRoom {
    private TripDao tripDao;
    private LocationDataDao locationDataDao;
    private TripsFirebase tripsFirebase;
    private NoteDao noteDao;
    private NotesFirebase notesFirebase;
    @Inject
    public TripsRoom(TripDao tripDao, LocationDataDao locationDataDao, TripsFirebase tripsFirebase, NoteDao noteDao, NotesFirebase notesFirebase) {
        this.tripDao = tripDao;
        this.locationDataDao = locationDataDao;
        this.tripsFirebase = tripsFirebase;
        this.noteDao =noteDao;
        this.notesFirebase = notesFirebase;
    }

    public LiveData<List<TripAndLocation>> getAllTrips(String userId){
       LiveData<List<Trip>> data = tripsFirebase.getTripsForUser(userId);
       List<Trip> ans = data.getValue();
        return tripDao.getAllTrips(userId);
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
    public void addTripAndNotes(Trip trip, List<Note> notes){
        TripOrganizerDatabase.databaseWriteExecutor.execute(()->{
            long tripId = tripDao.addTrip(trip);
            LocationData locationData = trip.getLocationData();
            locationData.setTripId(tripId);
            long locationId = locationDataDao.addLocationData(locationData);
            trip.setId(tripId);
            locationData.setId(locationId);
            trip.setLocationData(locationData);
            tripsFirebase.addTrip(trip);
            if(notes!=null || !notes.isEmpty()){
                for(Note note : notes) {
                    note.setTripId(tripId);
                    long id = noteDao.addNote(note);
                    note.setId(id);
                    notesFirebase.addNote(note, trip.getUserId());
                }
            }
        });
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

}
