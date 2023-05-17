package com.ifsc.expensemonitor.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ifsc.expensemonitor.R;
import com.ifsc.expensemonitor.calendar.CalendarActivity;
import com.ifsc.expensemonitor.database.FirebaseSettings;
import com.ifsc.expensemonitor.expenselist.ExpenseListActivity;

import java.util.Calendar;

public class WelcomeActivity extends AppCompatActivity {

    private Button withoutAccountBtn, signupBtn, signinBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        //auth.signOut();

        if (auth.getCurrentUser() != null) {
            currentMonthExpenseListActivity();
            finish();
        }
    }

    public void withoutAccount() {
        // TODO: implement anonymous user
    }

    public void currentMonthExpenseListActivity() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        Intent intent = new Intent(this, ExpenseListActivity.class);
        intent.putExtra("month", currentMonth);
        intent.putExtra("year", currentYear);
        startActivity(intent);
    }

    public void goToExpenses() { //todo: remove
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("isFromWelcome", true);
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