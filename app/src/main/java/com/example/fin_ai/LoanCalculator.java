package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanCalculator extends AppCompatActivity {
    Button mContinueButton;
    EditText mEditTextBranch, mEditTextBrokerFirstname, mEditTextBrokerSurname, mEditTextBrokerEmail;

    Spinner mbrokerPremiumSpinner;
    String brokerPremium = "";
    ArrayList<String> mBrokerPremiumOptions = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID; // Currently logged in user's user ID.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);

        // Finding actual buttons / spinners etc and assigning them to the variables.
        mbrokerPremiumSpinner = findViewById(R.id.brokerPremiumSpinner);
        mContinueButton = findViewById(R.id.continueButton);
        mEditTextBranch = findViewById(R.id.editTextBranch);
        mEditTextBrokerFirstname = findViewById(R.id.editTextBrokerFirstname);
        mEditTextBrokerSurname = findViewById(R.id.editTextBrokerSurname);
        mEditTextBrokerEmail = findViewById(R.id.editTextBrokerEmail);

        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.
        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.

        // Create dropdown for broker premium
        mBrokerPremiumOptions.add("1%");
        mBrokerPremiumOptions.add("2%");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mBrokerPremiumOptions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mbrokerPremiumSpinner.setAdapter(arrayAdapter);

        mbrokerPremiumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brokerPremium = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // moving to the next stage of loan calculation

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fAuth.getCurrentUser().getUid();

                // adding new data acquired (branch name and or broker details) to the users profile
                String branchLocation = mEditTextBranch.getText().toString();

                String brokerFirstName = mEditTextBrokerFirstname.getText().toString();
                String brokerSurname = mEditTextBrokerSurname.getText().toString();
                String brokerEmail = mEditTextBrokerEmail.getText().toString();

                DocumentReference mDocumentReference = fStore.collection("loanCalculation_Forms").document(userID);

                Map<String, Object> loanCalculation = new HashMap<>();
                loanCalculation.put("branchLocation", branchLocation);

                if (!TextUtils.isEmpty(brokerFirstName) && !TextUtils.isEmpty(brokerSurname) && !TextUtils.isEmpty(brokerEmail) && !TextUtils.isEmpty(brokerPremium)){
                    loanCalculation.put("brokerFirstname", brokerFirstName);
                    loanCalculation.put("brokerSurname", brokerSurname);
                    loanCalculation.put("brokerEmail", brokerEmail);
                    loanCalculation.put("brokerPremium", brokerPremium);
                }

                mDocumentReference.set(loanCalculation);

                Intent intent = new Intent(getApplicationContext(), LoanCalculator2.class);
                startActivity(intent);
            }
        });
    }
}
