package com.ifsc.expensemonitor.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSettings {
    private static FirebaseAuth auth;
    private static FirebaseDatabase database;

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        if (database == null){
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    public static DatabaseReference getUserReference() {
        return getFirebaseDatabase().getReference()
                .child("users")
                .child(getFirebaseAuth().getCurrentUser().getUid());
    }

}
