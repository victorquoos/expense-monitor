package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class WelcomeActivity extends AppCompatActivity {

    private Button withoutAccountBtn, signupBtn, signinBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: send user to calendar if already logged in

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signupBtn = (Button) findViewById(R.id.signupBtn);
        signinBtn = (Button) findViewById(R.id.signinBtn);
        withoutAccountBtn = (Button) findViewById(R.id.withoutAccountBtn);

        signupBtn.setOnClickListener(view -> signupActivity());
        signinBtn.setOnClickListener(view -> signinActivity());
        withoutAccountBtn.setOnClickListener(view -> withoutAccount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyIfUserIsLoggedIn();
    }

    private void verifyIfUserIsLoggedIn() {
        auth = FirebaseSettings.getFirebaseAuth();
        if (auth.getCurrentUser() != null) {
            calendarActivity();
            finish();
        }
    }

    public void withoutAccount() {
        // TODO: implement anonymous user
    }

    public void calendarActivity() {
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