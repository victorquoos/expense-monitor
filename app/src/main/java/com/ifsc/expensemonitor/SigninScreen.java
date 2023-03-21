package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ifsc.expensemonitor.database.DatabaseHandler;

import java.util.Arrays;

public class SigninScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        // DatabaseHandler.databaseTest();

    }
}