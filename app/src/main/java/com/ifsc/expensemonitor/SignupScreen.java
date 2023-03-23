package com.ifsc.expensemonitor;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ifsc.expensemonitor.database.DatabaseHandler;
import com.ifsc.expensemonitor.database.User;

public class SignupScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        test();
    }

    public void test() {
        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);
        Button signupBtn = (Button) findViewById(R.id.signupBtn);

        Log.d(TAG, "User name: LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO(N)G");
        Log.d(TAG, DatabaseHandler.childList.toString());
        Log.d(TAG, "User name: LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO(N)G");

        signupBtn.setOnClickListener(view -> {
            for(User user : DatabaseHandler.childList) {
                if (user.email == email.getText().toString() && user.password == password.getText().toString()) {
                    Log.d(TAG, "User name: LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO(N)G");
                }
            }
        });
    }


}