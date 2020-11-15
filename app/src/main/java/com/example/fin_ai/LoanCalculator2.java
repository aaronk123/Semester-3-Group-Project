package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanCalculator2 extends AppCompatActivity {

    Spinner mGenderSpinner, mEducationSpinner, mDependentsSpinner, mLoanTermSpinner;
    EditText mApplicantIncome, mCoapplicantIncome, mLoanAmt;
    Button mCalcButton;
    CheckBox mMarriedCheckBox, mSelfEmployedCheckBox;

    String gender = "";
    String education = "";
    String dependents = "";
    String loanTerm = "";

    ArrayList<String> mGenderOptions = new ArrayList<>();
    ArrayList<String> mEducationOptions = new ArrayList<>();
    ArrayList<String> mDependentsOptions = new ArrayList<>();
    ArrayList<String> mLoanTermOptions = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID; // Currently logged in user's user ID.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator2);

        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.
        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.

        // Finding actual buttons / spinners etc and assigning them to the variables.
        mGenderSpinner = findViewById(R.id.genderSpinner);
        mEducationSpinner = findViewById(R.id.educationSpinner);
        mDependentsSpinner = findViewById(R.id.dependentsSpinner);
        mLoanTermSpinner = findViewById(R.id.loanTermSpinner);

        mApplicantIncome = findViewById(R.id.editTextApplicantIncome);
        mCoapplicantIncome = findViewById(R.id.editTextCoApplicantIncome);
        mLoanAmt = findViewById(R.id.editTextLoanAmount);

        mCalcButton = findViewById(R.id.calculateButton);

        mMarriedCheckBox = findViewById(R.id.marriedCheckBox);
        mSelfEmployedCheckBox = findViewById(R.id.selfEmployedCheckBox);

        // Create dropdown for gender
        mGenderOptions.add("Male");
        mGenderOptions.add("Female");

        // Create dropdown for education
        mEducationOptions.add("Graduate");
        mEducationOptions.add("Non-Graduate");

        // Create dropdown for dependents
        mDependentsOptions.add("0");
        mDependentsOptions.add("1");
        mDependentsOptions.add("2");
        mDependentsOptions.add("3+");

        // Create dropdown for loan terms
        mLoanTermOptions.add("12");
        mLoanTermOptions.add("24");
        mLoanTermOptions.add("36");
        mLoanTermOptions.add("48");
        mLoanTermOptions.add("60");
        mLoanTermOptions.add("72");
        mLoanTermOptions.add("84");
        mLoanTermOptions.add("96");

        ArrayAdapter<String> arrayAdapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mGenderOptions);
        arrayAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(arrayAdapterGender);

        ArrayAdapter<String> arrayAdapterEducation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mEducationOptions);
        arrayAdapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEducationSpinner.setAdapter(arrayAdapterEducation);

        ArrayAdapter<String> arrayAdapterDependents = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDependentsOptions);
        arrayAdapterDependents.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDependentsSpinner.setAdapter(arrayAdapterDependents);

        ArrayAdapter<String> arrayAdapterLoanTerm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mLoanTermOptions);
        arrayAdapterLoanTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLoanTermSpinner.setAdapter(arrayAdapterLoanTerm);

        gender = mGenderSpinner.getItemAtPosition(0).toString(); // Sets the gender to the topmost index of the drowpdown spinner by default
        education = mEducationSpinner.getItemAtPosition(0).toString(); // Sets the education to the topmost index of the drowpdown spinner by default
        dependents = mDependentsSpinner.getItemAtPosition(0).toString(); // Sets the dependents to the topmost index of the drowpdown spinner by default
        loanTerm = mLoanTermSpinner.getItemAtPosition(0).toString(); // Sets the loan term to the topmost index of the drowpdown spinner by default


        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mEducationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                education = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mDependentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dependents = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mLoanTermSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loanTerm = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userID = fAuth.getCurrentUser().getUid();

                String applicantIncome = mApplicantIncome.getText().toString();
                String coApplicantIncome = mCoapplicantIncome.getText().toString();
                if (TextUtils.isEmpty(coApplicantIncome)){
                    coApplicantIncome = "0"; // make coapplicant income 0 by default if the field is left blank
                }
                String loanAmt = mLoanAmt.getText().toString();
                String creditHistory = "1";
                String propertyArea = "Urban";
                String marriedChoice = "";

                if (mMarriedCheckBox.isChecked()) {
                    marriedChoice = "Yes";
                } else {
                    marriedChoice = "No";
                }

                String selfEmployedChoice = "";

                if (mSelfEmployedCheckBox.isChecked()) {
                    selfEmployedChoice = "Yes";
                } else {
                    selfEmployedChoice = "No";
                }

                // Build up a JSON string to pass to the Fin-Ai Engine API god knows what happens
                // if the user types some weird stuff in monkas
                // Surely there is an easier way to do this.

                String data = "{\n" +
                        "\"gender\":" + "\"" + gender + "\",\n" +
                        "\"married\":" + "\"" + marriedChoice + "\",\n" +
                        "\"dependents\":" + "\"" + dependents + "\",\n" +
                        "\"education\":" + "\"" + education + "\",\n" +
                        "\"self-employed\":" + "\"" + selfEmployedChoice + "\",\n" +
                        "\"applicantIncome\":" + "\"" + applicantIncome + "\",\n" +
                        "\"coApplicantIncome\":" + "\"" + coApplicantIncome + "\",\n" +
                        "\"loanAmount\":" + "\"" + loanAmt + "\",\n" +
                        "\"loanAmountTerm\":" + "\"" + loanTerm + "\",\n" +
                        "\"creditHistory\":" + "\"" + creditHistory + "\",\n" +
                        "\"propertyArea\":" + "\"" + propertyArea + "\"\n" +
                        "}";
                Log.d("TEST", data);
                // POST the data to the API.
                SubmitData(data);
            }


            private void SubmitData(String obj) {
                // Volley post method
                // Creating a RequestQueue instance
                final String savedata = obj;
                String url = "http://192.168.1.10:5000/submitCalculation"; // this has to be changed to your computers local ipv4 address (cmd->ipconfig)
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response = "Y" / "N" - determines whether the client can apply for the loan or not.
                            if (response.equals("Y")){
                                Toast.makeText(getApplicationContext(), "You meet the requirements to apply for this loan, forwarding to the loan application page.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), Application.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Sorry, you do not meet the requirements to apply for this loan.", Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Log.i("VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        //Log.v("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return savedata == null ? null : savedata.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            //Log.v("Unsupported Encoding while trying to get the bytes", data);
                            return null;
                        }
                    }
                };
                requestQueue.add(stringRequest);
            }
            });
    }
}