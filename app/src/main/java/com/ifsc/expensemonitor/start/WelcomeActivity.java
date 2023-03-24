package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Button withoutAccountBtn, signupBtn, signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: send user to calendar if already logged in

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        withoutAccountBtn = (Button) findViewById(R.id.withoutAccountBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        signinBtn = (Button) findViewById(R.id.signinBtn);

        withoutAccountBtn.setOnClickListener(view -> expensesActivity());
        signupBtn.setOnClickListener(view -> signupActivity());
        signinBtn.setOnClickListener(view -> signinActivity());
    }

    public void expensesActivity() {
        // TODO: implement anonymous user
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void signupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void signinActivity() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }
}