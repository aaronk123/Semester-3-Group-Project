package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Review extends AppCompatActivity {

    RatingBar mRating;
    EditText mComment;
    String userID; // Currently logged in user's user ID.
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mRating = findViewById(R.id.ratingBar);
        mComment = findViewById(R.id.comment);

        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.
        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.
    }

    public void home(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void submitReview(View view) {
        float userRating = mRating.getRating();
        String userComment = mComment.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dayMonthYear = formatter.format(date);

        if (userRating < 0.5) {
            Toast.makeText(Review.this, "Rating field is Empty", Toast.LENGTH_LONG).show();
        } else {
            userID = fAuth.getCurrentUser().getUid();
            CollectionReference mCollectionReference = fStore.collection("user_reviews").document(userID).collection("reviews");

            Map<String,Object> review = new HashMap<>();
            review.put("date", dayMonthYear);
            review.put("rating", userRating);
            review.put("comment", userComment);

            mCollectionReference.add(review);
            Toast.makeText(Review.this, "We appreciate your feedback!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}