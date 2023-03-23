package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;
import com.ifsc.expensemonitor.start.SignupScreen;

public class WelcomeScreen extends AppCompatActivity {

    Button withoutAccountBtn, signupBtn, signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        withoutAccountBtn = (Button) findViewById(R.id.withoutAccountBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        signinBtn = (Button) findViewById(R.id.signinBtn);

        withoutAccountBtn.setOnClickListener(view -> expensesScreen());
        signupBtn.setOnClickListener(view -> signupScreen());
        signinBtn.setOnClickListener(view -> signinScreen());
    }

    public void expensesScreen() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void signupScreen() {
        Intent intent = new Intent(this, SignupScreen.class);
        startActivity(intent);
    }

    public void signinScreen() {
        //Intent intent = new Intent(this, SigninScreen.class); // TODO: create SigninScreen
        //startActivity(intent);
    }
}