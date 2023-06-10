package com.ifsc.expensemonitor.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {
    @Exclude
    private String uid;
    private String email;
    @Exclude
    private String password;


    public User() {
    }

    public void save() {
        DatabaseReference reference = FirebaseSettings.getFirebaseDatabase().getReference();
        reference.child("users").child(getUid()).setValue(this);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
