package com.example.fin_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class GoogleInfo extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText mAddress;
    Button mloginButton;
    String userID;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_info);

        mAddress = findViewById(R.id.address);

        mloginButton = findViewById(R.id.loginButton);
        final GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.
        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = signInAccount.getGivenName();
                String surName = signInAccount.getFamilyName();
                String address = mAddress.getText().toString();
                String email = signInAccount.getEmail();

                // Handling potential errors

                if (TextUtils.isEmpty(address)){
                    mAddress.setError("Address field cannot be empty.");
                }
                else if (!TextUtils.isEmpty(address)){

                    // Storing user data into firestore and sending them to the Fin-Ai dashboard.
                    Toast.makeText(GoogleInfo.this, "Account created!", Toast.LENGTH_LONG).show();
                    userID = fAuth.getCurrentUser().getUid();; // Getting current user's userID to use it as a document ID in firestore.
                    // Storing user date in firestore (database).
                    DocumentReference mDocumentReference = fStore.collection("users").document(userID); // creates a collection "users" if it is not already present in firestore.
                    // Creating a hashmap with key: String, value: Object.
                    Map<String,Object> user = new HashMap<>();
                    // Placing user information as a value into relevant key. (k,v)
                    user.put("firstName", firstName);
                    user.put("surName", surName);
                    user.put("address", address);
                    user.put("email", email);

                    // Inserting data into firestore collection users; document UserID;
                    mDocumentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User profile is created for: " + userID);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
};