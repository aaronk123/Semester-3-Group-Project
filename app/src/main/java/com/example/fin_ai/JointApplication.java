package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class JointApplication extends AppCompatActivity {

    EditText mFirstName, mSurname, mAddress, mContactNumber, mEmail, mPPSN, mGrossAnnualIncome;
    TextView mDOB;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_application);

        mFirstName = findViewById(R.id.firstName1);
        mSurname = findViewById(R.id.surName1);
        mAddress = findViewById(R.id.address1);
        mContactNumber = findViewById(R.id.contactNumber1);
        mEmail = findViewById(R.id.email1);
        mPPSN = findViewById(R.id.ppsn1);
        mGrossAnnualIncome = findViewById(R.id.grossAnnualIncome1);
        mDOB = findViewById(R.id.dob1);

        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(JointApplication.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                mDOB.setText(date);
            }
        };

    }

    public void onContinue(View view) {
        String firstName = mFirstName.getText().toString();
        String surname = mSurname.getText().toString();
        String address = mAddress.getText().toString();
        String contactNumber = mContactNumber.getText().toString();
        String email = mEmail.getText().toString();
        String ppsn = mPPSN.getText().toString();
        String grossAnnualIncome = mGrossAnnualIncome.getText().toString();
        String dob = mDOB.getText().toString();
        if (TextUtils.isEmpty(firstName)){
            mFirstName.setError("First name field is empty.");
        } else if (TextUtils.isEmpty(surname)) {
            mSurname.setError("Surname field is empty.");
        } else if (TextUtils.isEmpty(address)) {
            mAddress.setError("Address field is empty.");
        } else if  (TextUtils.isEmpty(contactNumber)) {
            mContactNumber.setError("Contact number field is empty.");
        } else if  (TextUtils.isEmpty(email)) {
            mEmail.setError("Email field is empty.");
        } else if  (TextUtils.isEmpty(ppsn)) {
            mPPSN.setError("PPSN field is empty.");
        } else if  (TextUtils.isEmpty(grossAnnualIncome)) {
            mGrossAnnualIncome.setError("Gross annual income field is empty.");
        } else {
            ArrayList<String> jointApplicantDetails = new ArrayList<>();
            jointApplicantDetails.add(firstName);
            jointApplicantDetails.add(surname);
            jointApplicantDetails.add(address);
            jointApplicantDetails.add(contactNumber);
            jointApplicantDetails.add(email);
            jointApplicantDetails.add(ppsn);
            jointApplicantDetails.add(dob);
            jointApplicantDetails.add(grossAnnualIncome);
            Application.retrieveData(jointApplicantDetails);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Application.setSingle();
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }
}