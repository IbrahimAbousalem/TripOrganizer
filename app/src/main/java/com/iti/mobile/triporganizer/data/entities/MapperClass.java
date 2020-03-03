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

    public static Trip mapTripAndLocationObject(TripAndLocation tripAndLocation){
        Trip trip = new Trip();
        trip.setLocationData(tripAndLocation.getLocationDataList());
        trip.setId(tripAndLocation.getTrip().getId());
        trip.setStatus(tripAndLocation.getTrip().getStatus());
        trip.setUserId(tripAndLocation.getTrip().getUserId());
        trip.setTripName(tripAndLocation.getTrip().getTripName());
        return trip;
    }

    public static List<Trip> mapTripAndLocationList(List<TripAndLocation> tripAndLocationList){
        List<Trip> tripList = new ArrayList<>();
        for(TripAndLocation tripAndLocationObject : tripAndLocationList){
            Trip mapTrip = mapTripAndLocationObject(tripAndLocationObject);
            tripList.add(mapTrip);
        }
        return tripList;
    }

}
