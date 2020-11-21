package com.example.fin_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Application extends AppCompatActivity {

    TextView mLoanAmount, mLoanTerm, mGrossAnnualIncome, mGrossAnnualIncomeFill;
    Spinner mTitleSpinner;
    ArrayList<String> mTitles = new ArrayList<>();
    EditText mContactNumber, mPPSN;
    TextView mDOB;
    Button mSubmitButton;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    String firstName, surname, address, email, coApplicantIncome;
    String userID; // Currently logged in user's user ID.
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        mTitles.add("Mr");
        mTitles.add("Mrs");
        mTitles.add("Miss");
        mTitles.add("Ms");
        mTitles.add("Dr");

        mTitleSpinner = findViewById(R.id.titleSpinner);

        mLoanAmount = findViewById(R.id.loanAmountFill);
        mLoanTerm = findViewById(R.id.loanTermFill);
        mGrossAnnualIncome = findViewById(R.id.grossAnnualIncome);
        mGrossAnnualIncomeFill = findViewById(R.id.grossAnnualIncomeFill);


        mLoanAmount.setText(getIntent().getStringExtra("loanAmount"));
        mLoanTerm.setText(getIntent().getStringExtra("loanTerm"));
        mGrossAnnualIncomeFill.setText(getIntent().getStringExtra("applicantIncome"));
        coApplicantIncome = getIntent().getStringExtra("coApplicantIncome");

        mSubmitButton = findViewById(R.id.registerButton2);

        if (!coApplicantIncome.equals("0")) {
            mSubmitButton.setText("Continue to Co-Applicant Form");
        }
        mContactNumber = findViewById(R.id.contactNumber);
        mPPSN = findViewById(R.id.ppsn);
        mDOB = findViewById(R.id.dob);

        mDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(Application.this,
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

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTitles);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(titleAdapter);
    }

    public void launchCoApplicant(String ID) {
        Intent intent = new Intent(getApplicationContext(), JointApplication.class);
        intent.putExtra("userID", ID);
        intent.putExtra("applicationType", "Joint");
        intent.putExtra("firstName", firstName);
        intent.putExtra("surname", surname);
        intent.putExtra("address", address);
        intent.putExtra("email", email);
        intent.putExtra("loanAmount", mLoanAmount.getText().toString());
        intent.putExtra("loanTerm", mLoanTerm.getText().toString());
        intent.putExtra("title", mTitleSpinner.getSelectedItem().toString());
        intent.putExtra("dateOfBirth", mDOB.getText().toString());
        intent.putExtra("phoneNumber", mContactNumber.getText().toString());
        intent.putExtra("ppsNumber", mPPSN.getText().toString());
        intent.putExtra("grossAnnualIncome", mGrossAnnualIncomeFill.getText().toString());
        intent.putExtra("coApplicantIncome", coApplicantIncome);
        startActivity(intent);
    }

    public void onSubmit(View view) {
        userID = fAuth.getCurrentUser().getUid();
        String loanAmount = mLoanAmount.getText().toString();
        String contactNumber = mContactNumber.getText().toString();
        String ppsn = mPPSN.getText().toString();
        String grossAnnualIncome = mGrossAnnualIncome.getText().toString();
        if (TextUtils.isEmpty(loanAmount)) {
            mLoanAmount.setError("Loan amount field is empty.");
        } else if (TextUtils.isEmpty(contactNumber)) {
            mContactNumber.setError("Contact number field is empty.");
        } else if (TextUtils.isEmpty(ppsn)) {
            mPPSN.setError("PPSN field is empty.");
        } else if (TextUtils.isEmpty(grossAnnualIncome)) {
            mGrossAnnualIncome.setError("Gross annual income field is empty.");
        } else if (!coApplicantIncome.equals("0")) {
            launchCoApplicant(userID);
        } else {
            DocumentReference userInfoDocument = fStore.collection("users").document(userID);
            userInfoDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot snapshot = task.getResult();
                    Map<String,Object> application = new HashMap<>();
                    String firstName = snapshot.get("firstName").toString();
                    String surname = snapshot.get("surName").toString();
                    String address = snapshot.get("address").toString();
                    String email = snapshot.get("email").toString();
                    application.put("applicationType", "Single");
                    application.put("loanAmount", mLoanAmount.getText().toString());
                    application.put("loanTerm", mLoanTerm.getText().toString());
                    application.put("title", mTitleSpinner.getSelectedItem().toString());
                    application.put("firstName", firstName);
                    application.put("surName", surname);
                    application.put("dateOfBirth", mDOB.getText().toString());
                    application.put("address", address);
                    application.put("phoneNumber", mContactNumber.getText().toString());
                    application.put("email", email);
                    application.put("ppsNumber", mPPSN.getText().toString());
                    application.put("grossAnnualIncome", mGrossAnnualIncomeFill.getText().toString());
                    DocumentReference mDocumentReference = fStore.collection("user_application_forms").document(userID);
                    mDocumentReference.set(application);
                    Toast.makeText(Application.this, "Your Application is now being processed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }
}