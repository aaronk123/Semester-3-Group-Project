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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Application extends AppCompatActivity {

    private static ArrayList<String> jointApplicantForm = new ArrayList<>();
    private static RadioButton mJointButton, mSingleButton;
    Spinner mLoanAmountTermSpinner, mTitleSpinner;
    ArrayList<String> mLoanTerms = new ArrayList<>();
    ArrayList<String> mTitles = new ArrayList<>();
    EditText mLoanAmount, mContactNumber, mPPSN, mGrossAnnualIncome;
    TextView mDOB;
    Button mSubmitButton;
    DatePickerDialog.OnDateSetListener mDateSetListener;

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
        for (int i=12; i<108; i+=12) {
            mLoanTerms.add(Integer.toString(i));
        }

        mLoanAmountTermSpinner = findViewById(R.id.loanAmountTermSpinner);
        mTitleSpinner = findViewById(R.id.titleSpinner);

        mLoanAmount = findViewById(R.id.loanAmount);
        mContactNumber = findViewById(R.id.contactNumber);
        mPPSN = findViewById(R.id.ppsn);
        mGrossAnnualIncome = findViewById(R.id.grossAnnualIncome);
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

        mJointButton = findViewById(R.id.jointButton);
        mSingleButton = findViewById(R.id.singleButton);
        mSingleButton.setChecked(true);
        mSubmitButton = findViewById(R.id.submitButton);

        ArrayAdapter<String> loanAmountTermAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mLoanTerms);
        loanAmountTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLoanAmountTermSpinner.setAdapter(loanAmountTermAdapter);

        ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTitles);
        titleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(titleAdapter);

    }

    public void onSubmit(View view) {
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference userInfoDocument = fStore.collection("users").document(userID);
        userInfoDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                String firstName = snapshot.get("firstName").toString();
                String surname = snapshot.get("surName").toString();
                String address = snapshot.get("address").toString();
                String email = snapshot.get("email").toString();
                String loanAmount = mLoanAmount.getText().toString();
                String loanTerm = mLoanAmountTermSpinner.getSelectedItem().toString();
                String title = mTitleSpinner.getSelectedItem().toString();
                String contactNumber = mContactNumber.getText().toString();
                String ppsn = mPPSN.getText().toString();
                String grossAnnualIncome = mGrossAnnualIncome.getText().toString();
                String dob = mDOB.getText().toString();
                if (TextUtils.isEmpty(loanAmount)){
                    mLoanAmount.setError("Loan amount field is empty.");
                } else if (TextUtils.isEmpty(contactNumber)) {
                    mContactNumber.setError("Contact number field is empty.");
                } else if (TextUtils.isEmpty(ppsn)) {
                    mPPSN.setError("PPSN field is empty.");
                } else if (TextUtils.isEmpty(grossAnnualIncome)) {
                    mGrossAnnualIncome.setError("Gross annual income field is empty.");
                } else {
                    Map<String,Object> application = new HashMap<>();
                    application.put("loanAmount", loanAmount);
                    application.put("loanTerm", loanTerm);
                    application.put("title", title);
                    application.put("firstName", firstName);
                    application.put("surName", surname);
                    application.put("dateOfBirth", dob);
                    application.put("address", address);
                    application.put("phoneNumber", contactNumber);
                    application.put("email", email);
                    application.put("ppsNumber", ppsn);
                    application.put("grossAnnualIncome", grossAnnualIncome);
                    if (mSingleButton.isChecked()) {
                        application.put("applicationType", "Single");
                    } else {
                        application.put("applicationType", "Joint");
                        application.put("coApplicantFirstName", jointApplicantForm.get(0));
                        application.put("coApplicantSurname", jointApplicantForm.get(1));
                        application.put("coApplicantAddress", jointApplicantForm.get(2));
                        application.put("coApplicantPhoneNumber", jointApplicantForm.get(3));
                        application.put("coApplicantEmail", jointApplicantForm.get(4));
                        application.put("coApplicantDateOfBirth", jointApplicantForm.get(5));
                        application.put("coApplicantPPSNumber", jointApplicantForm.get(5));
                        application.put("coApplicantGrossAnnualIncome", jointApplicantForm.get(6));
                    }
                    DocumentReference mDocumentReference = fStore.collection("user_application_forms").document(userID);
                    mDocumentReference.set(application);
                    Toast.makeText(Application.this, "Your Application is now being processed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void retrieveData(ArrayList<String> jointApplicantInfo) {
        jointApplicantForm.clear();
        jointApplicantForm = jointApplicantInfo;
    }

    public void onJointClick(View view) {
        mSingleButton.setChecked(false);
        startActivity(new Intent(getApplicationContext(), JointApplication.class));
    }

    public void onSingleClick(View view) {
        mJointButton.setChecked(false);
        mSingleButton.setChecked(true);
    }

    public static void setSingle() {
        mJointButton.setChecked(false);
        mSingleButton.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }
}