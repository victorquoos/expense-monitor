package com.ifsc.expensemonitor.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSettings {
<<<<<<< HEAD
=======
    private static DatabaseReference userReference;
>>>>>>> 8e57fcd (iniciando menu de opções)

    public static FirebaseDatabase getFirebaseDatabase() { //TODO: substituir por FirebaseDatabase.getInstance()
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getUserReference() {
<<<<<<< HEAD
        DatabaseReference userReference = getFirebaseDatabase()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

=======
        if (userReference == null) {
            userReference = getFirebaseDatabase()
                    .getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
>>>>>>> 8e57fcd (iniciando menu de opções)
        return userReference;
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
