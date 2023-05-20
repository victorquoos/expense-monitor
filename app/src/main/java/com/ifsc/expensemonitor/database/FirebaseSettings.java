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

    public static FirebaseAuth getFirebaseAuth() { //TODO: substituir por FirebaseAuth.getInstance()
        return FirebaseAuth.getInstance();
    }

    public static FirebaseDatabase getFirebaseDatabase() { //TODO: substituir por FirebaseDatabase.getInstance()
        return FirebaseDatabase.getInstance();
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

    public static DatabaseReference getMonthReference(int year, int month) {
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

    public static void saveUserLocally() {
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
