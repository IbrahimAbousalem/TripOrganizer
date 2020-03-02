package com.iti.mobile.triporganizer.data.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class MapperClass {

    public static TripAndLocation mapTripObject(Trip trip){
        TripAndLocation tripAndLocation = new TripAndLocation();
        tripAndLocation.setLocationDataList(trip.getLocationData());
        tripAndLocation.setTrip(trip);
        return tripAndLocation;
    }

    public static List<TripAndLocation> mapTripList(List<Trip> tripList){
        List<TripAndLocation> tripAndLocation = new ArrayList<>();
        for(Trip tripObject : tripList){
            TripAndLocation mapTrip = mapTripObject(tripObject);
            tripAndLocation.add(mapTrip);
        }
        return tripAndLocation;
    }

}
