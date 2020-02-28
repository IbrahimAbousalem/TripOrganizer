package com.iti.mobile.triporganizer.data.room;

import com.iti.mobile.triporganizer.dagger.Scope.ApplicationScope;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.data.firebase.TripsFirebase;
import com.iti.mobile.triporganizer.data.room.dao.LocationDataDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

@ApplicationScope
public class TripsRoom {
    private TripDao tripDao;
    private LocationDataDao locationDataDao;
    private TripsFirebase tripsFirebase;

    @Inject
    public TripsRoom(TripDao tripDao, LocationDataDao locationDataDao, TripsFirebase tripsFirebase) {
        this.tripDao = tripDao;
        this.locationDataDao = locationDataDao;
        this.tripsFirebase = tripsFirebase;
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
