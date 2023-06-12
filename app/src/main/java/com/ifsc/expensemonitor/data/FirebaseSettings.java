package com.ifsc.expensemonitor.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSettings {
<<<<<<< HEAD
<<<<<<< HEAD
=======
    private static DatabaseReference userReference;
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
>>>>>>> fb51114 (muita coisa)

    public static FirebaseDatabase getFirebaseDatabase() { //TODO: substituir por FirebaseDatabase.getInstance()
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getUserReference() {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> fb51114 (muita coisa)
        DatabaseReference userReference = getFirebaseDatabase()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

<<<<<<< HEAD
=======
        if (userReference == null) {
            userReference = getFirebaseDatabase()
                    .getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
>>>>>>> 8e57fcd (iniciando menu de opções)
=======
>>>>>>> fb51114 (muita coisa)
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
