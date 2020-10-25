package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Allowing the user to logout.
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); // Logging the user out.
        startActivity(new Intent(getApplicationContext(), Login.class)); // Send the user back to the login screen.
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }

    public void goToReview(View view) {
        startActivity(new Intent(getApplicationContext(), Review.class));
    }

    public void goToSupport(View view) {
        startActivity(new Intent(getApplicationContext(), Support.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}