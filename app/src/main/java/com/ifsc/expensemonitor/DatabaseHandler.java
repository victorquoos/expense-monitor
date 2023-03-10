package com.ifsc.expensemonitor;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    public static void databaseTest() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child("0");

        String id = myRef.toString().split("/")[4];

        User userTest = new User();

        userTest.email ="email@test.com";
        userTest.password ="passwordtest";
        userTest.id = id;

        List<Expense> expenses = userTest.expenses;

        Expense expense = new Expense();

        expense.load(id,"expense test","description",2000f,expenseType.Parcel);

        expenses.add(expense);
        expenses.add(expense);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Value is: " + value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        userTest.expenses = expenses;

        myRef.setValue(userTest);

        //myRef.child("email").setValue("email");
        //myRef.child("senha").setValue("senha");
    }
}
