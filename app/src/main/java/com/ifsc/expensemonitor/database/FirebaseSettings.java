package com.ifsc.expensemonitor.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseSettings {
    private static DatabaseReference userReference;

    public static FirebaseDatabase getFirebaseDatabase() { //TODO: substituir por FirebaseDatabase.getInstance()
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getUserReference() {
        if (userReference == null) {
            userReference = getFirebaseDatabase()
                    .getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        return userReference;
    }

    public static DatabaseReference getExpensesReference() {
        return getUserReference()
                .child("expenses");
    }

    public static DatabaseReference getOccurrencesReference() {
        return getUserReference()
                .child("occurrences");
    }

    public static DatabaseReference getOccurrenceControllersReference() {
        return getUserReference()
                .child("occurrence-controllers");
    }

}
