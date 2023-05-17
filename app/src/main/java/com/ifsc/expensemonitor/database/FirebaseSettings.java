package com.ifsc.expensemonitor.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseSettings {
    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static DatabaseReference userReference;

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    public static DatabaseReference getUserReference() {
        if (userReference == null) {
            userReference = getFirebaseDatabase()
                    .getReference()
                    .child("users")
                    .child(getFirebaseAuth().getCurrentUser().getUid());
        }
        return userReference;
    }

    public static DatabaseReference getMonthReference(int year, int month) { //todo: rever
        return getUserReference()
                .child("expenses")
                .child("year" + year)
                .child("month" + month);
    }

    public static void saveExpense(Expense expense) {
        getUserReference()
                .child("expenses")
                .child("year" + expense.getDate().getYear())
                .child("month" + expense.getDate().getMonth())
                .push()
                .setValue(expense);
    }

    public static void deleteExpense(Expense expense) {
        getUserReference()
                .child("expenses")
                .child("year" + expense.getDate().getYear())
                .child("month" + expense.getDate().getMonth())
                .child(expense.getKey())
                .removeValue();
    }

    public static void updateExpense(Expense oldExpense, Expense newExpense) {
        deleteExpense(oldExpense);
        saveExpense(newExpense);
    }

    public static void saveUserLocally() { //todo: rever
        getUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Object childData = childSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
