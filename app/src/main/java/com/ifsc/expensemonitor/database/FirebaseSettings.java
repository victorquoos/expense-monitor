package com.ifsc.expensemonitor.database;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseSettings {
    private static FirebaseAuth auth;

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;

    }

}
