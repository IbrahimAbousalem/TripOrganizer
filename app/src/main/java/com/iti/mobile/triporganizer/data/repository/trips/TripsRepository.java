package com.iti.mobile.triporganizer.data.repository.trips;

import androidx.lifecycle.LiveData;
import com.iti.mobile.triporganizer.data.entities.Trip;
import java.util.List;

public interface TripsRepository {
    boolean addTrip(Trip trip);
    boolean deleteTrip(Trip trip);
    boolean  updateTrip(Trip trip);
    LiveData<List<Trip>> getTripsForUser(String userId);
}
