package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SigninScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        DatabaseHandler.databaseTest();
    }
}