package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExpensesList extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        databaseTest();
    }

    private void databaseTest(){
        databaseReference.child("teeste").setValue("123");
    }
}