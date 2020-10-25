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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = "TAG"; // used in Logcat to filter log messages.
    EditText mfirstName, msurName, mAddress, mEmail, mPassword;
    Button mRegisterButton;

    String userID; // Currently logged in user's user ID.
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mfirstName = findViewById(R.id.firstName);
        msurName = findViewById(R.id.surName);
        mAddress = findViewById(R.id.address);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mRegisterButton = findViewById(R.id.registerButton);

        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.
        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Final has been appended due to use of hashmap for storing user information into firebase.
                final String firstName = mfirstName.getText().toString();
                final String surName = msurName.getText().toString();
                final String address = mAddress.getText().toString();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // handling potential errors.

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email field is empty.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password field is empty.");
                    return;
                }

                if (password.length() < 8){
                    mPassword.setError("Password must be at least 8 characters long.");
                    return;
                }

                // Registering the user in firebase.

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Account created!", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid(); // Getting current user's userID to use it as a document ID in firestore.
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

                        else{
                            Toast.makeText(Register.this, "Error occurred!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}