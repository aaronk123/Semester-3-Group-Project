package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Support extends AppCompatActivity {

    Spinner mContactSpinner;

    TextView mContactDate, mContactTime;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    TimePickerDialog.OnTimeSetListener mTimeSetListener;

    EditText mEmail, mAddress, mPhoneNumber, mComment;
    ArrayList<String> mContactMethods = new ArrayList<>();
    String contactType = "";

    String userID; // Currently logged in user's user ID.
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        mContactTime = findViewById(R.id.contactTime);
        mContactDate = findViewById(R.id.contactDate);
        mEmail = findViewById(R.id.email);
        mAddress = findViewById(R.id.address);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mComment = findViewById(R.id.comment);

        fAuth = FirebaseAuth.getInstance(); // Getting the current instance of the authentication database from firebase.
        fStore = FirebaseFirestore.getInstance(); // Getting the current instance of the firestore database from firebase.

        mContactSpinner = findViewById(R.id.contactSpinner);
        mContactMethods.add("Financial Consultation");
        mContactMethods.add("Mortgage");
        mContactMethods.add("Personal Loan");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mContactMethods);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContactSpinner.setAdapter(arrayAdapter);

        contactType = mContactSpinner.getItemAtPosition(0).toString(); // Sets the contact type to the topmost index of the drowpdown spinner by default

        mContactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mContactDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(Support.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mContactTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(Support.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,
                        hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + month + "/" + year;
                mContactDate.setText(date);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                mContactTime.setText(time);
            }
        };
    }

    public void submitForm(View view) {
        String email = mEmail.getText().toString();
        String address = mAddress.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String date = mContactDate.getText().toString();
        String time = mContactTime.getText().toString();
        String contact = contactType;
        String comment = mComment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            comment = "No comment";
        }

        if (TextUtils.isEmpty(email)){
            mEmail.setError("Email field is empty.");
        } else if (TextUtils.isEmpty(address)){
            mAddress.setError("Address field is empty.");
        } else if (TextUtils.isEmpty(phoneNumber)){
            mPhoneNumber.setError("Phone Number field is empty.");
        } else if (date.equals("  Select Preferred Date")) {
            mContactDate.setError("Date field is empty.");
        } else if (time.equals("  Select Preferred Time")) {
            mContactTime.setError("Time field is empty.");
        } else {
            userID = fAuth.getCurrentUser().getUid();
            DocumentReference mDocumentReference = fStore.collection("user_support_form").document(userID);

            Map<String,Object> review = new HashMap<>();
            review.put("regarding", contact);
            review.put("preferredDate", date);
            review.put("preferredTime", time);
            review.put("email", email);
            review.put("address", address);
            review.put("phoneNumber", phoneNumber);
            review.put("comment", comment);

            mDocumentReference.set(review);
            Toast.makeText(Support.this, "Your Form has been submitted successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}