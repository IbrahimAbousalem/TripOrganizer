package com.iti.mobile.triporganizer.utils;


public class FirestoreConstatnts {

    public static final String TRIPS_COLLECTION = "Trips";
    public static final String NOTES_COLLECTION = "Notes";
    public static final String USERS_COLLECTION = "Users";

    //user
    public static final String userName = "userName";
    public static final String profilePicUrl = "profilePicUrl";
    public static final String email = "email";

    //trip
    public static final String id = "id";
    public static final String fireTripId = "fireTripId";
    public static final String userId = "userId";
    public static final String tripName = "tripName";
    public static final String status = "status";
    public static final String startTrip = "startTrip";
    public static final String roundTrip = "roundTrip";
    public static final String isRound = "isRound";

    //tripData
    public static final String tripId = "tripId";
    public static final String locationData = "locationData";
    public static final String date = "date";

    //locationData
    public static final String startTripStartPointLat = "startTripStartPointLat";
    public static final String startTripStartPointLng = "startTripStartPointLng";
    public static final String startTripStartAddressName = "startTripStartAddressName";
    public static final String startTripEndAddressName = "startTripEndAddressName";
    public static final String startTripEndPointLat = "startTripEndPointLat";
    public static final String startTripEndPointLng = "startTripEndPointLng";
    public static final String roundTripStartPointLat = "roundTripStartPointLat";
    public static final String roundTripStartPointLng = "roundTripStartPointLng";
    public static final String roundTripStartAddressName = "roundTripStartAddressName";
    public static final String roundTripEndAddressName = "roundTripEndAddressName";
    public static final String roundTripEndPointLat = "roundTripEndPointLat";
    public static final String roundTripEndPointLng = "roundTripEndPointLng";
    public static final String tripDetailsId = "tripDetailsId";
    public static final String startDate = "startDate";
    public static final String roundDate = "roundDate";


    //Note
    public static final String message = "message";
    public static final String fireNoteId = "fireNoteId";
}
