package com.ifsc.expensemonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.ifsc.expensemonitor.database.FirebaseSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
            navController.popBackStack();
            navController.navigate(R.id.expenseListFragment);
        }
    }
}