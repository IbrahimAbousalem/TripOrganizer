package com.iti.mobile.triporganizer.utils;

import com.google.firebase.firestore.CollectionReference;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.firebase.FirestoreDB;

import java.util.Date;

public class FirestoreConstatnts {

    private static final String TRIPS_COLLECTION = "trips";
    private static final String NOTES_COLLECTION = "notes";
    private static final String USERS_COLLECTION = "users";

    public static final String id = "id";
    public static final String userId = "userId";
    public static final String startPoint = "startPoint";
    public static final String endPoint = "endPoint";
    public static final String date = "date";
    public static final String type = "type";
    public static final String status = "status";
    public static final String roundTrip = "roundTrip";
    public static final String isRound = "isRound";
    public static final String message = "message";
    public static final String tripId = "tripId";

    public static final CollectionReference usersReference = FirestoreDB.getInstance().getDB().collection(USERS_COLLECTION);
    public static final CollectionReference tripsReference = FirestoreDB.getInstance().getDB().collection(TRIPS_COLLECTION);
    public static final CollectionReference notesReference = FirestoreDB.getInstance().getDB().collection(NOTES_COLLECTION);
}
