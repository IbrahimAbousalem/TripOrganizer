package com.iti.mobile.triporganizer.data.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDB {

    private FirebaseFirestore db;

    private static final FirestoreDB instance = new FirestoreDB();

    public static FirestoreDB getInstance() {
        return instance;
    }

    private FirestoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDB() {
        return db;
    }
}
