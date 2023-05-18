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

    public static Expense getExpense(int year, int month, String key) {
        // Crie uma variável para armazenar a despesa recuperada
        final Expense[] retrievedExpense = new Expense[1];

        // Crie uma referência para a localização da despesa
        DatabaseReference ref = getMonthReference(year, month).child(key);

        // Adicione um ouvinte para obter o valor da despesa
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Converta o snapshot em uma despesa e armazene-a na variável
                retrievedExpense[0] = snapshot.getValue(Expense.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Trate qualquer erro que possa ocorrer
                Log.e("Firebase", error.getMessage());
            }
        });

        // Retorne a despesa recuperada
        return retrievedExpense[0];
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
