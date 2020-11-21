package com.example.fin_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button mProfessionalRequestButton;
    String userID; // Currently logged in user's user ID.
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProfessionalRequestButton = findViewById(R.id.requestProfessioanlButton);
        mProfessionalRequestButton.setVisibility(View.GONE);
        checkForExistingApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForExistingApplication();
    }

    public void checkForExistingApplication() {
        userID = fAuth.getCurrentUser().getUid();
        fStore.collection("user_application_forms").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        mProfessionalRequestButton.setVisibility(View.VISIBLE);
                    } else {
                        mProfessionalRequestButton.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    // Allowing the user to logout.
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); // Logging the user out.
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }

    public void goToReview(View view) {
        startActivity(new Intent(getApplicationContext(), Review.class));
    }

    public void goToSupport(View view) {
        startActivity(new Intent(getApplicationContext(), Support.class));
    }

    public void goToLoanCalc(View view){
        startActivity(new Intent(getApplicationContext(), LoanCalculator.class));
    }

    public void goToProfessional(View view){
        startActivity(new Intent(getApplicationContext(), ProfessionalSupport.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(MainActivity.this, "Please click the Logout button to log out.", Toast.LENGTH_LONG).show();
    }
}