package com.example.fin_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JointApplication extends AppCompatActivity {

    EditText mFirstName, mSurname, mAddress, mContactNumber, mEmail, mPPSN;
    TextView mDOB, mGrossAnnualIncome;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    Button mSubmitJointButton;
    String userID = ""; // Currently logged in user's user ID.
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_application);

        mSubmitJointButton = findViewById(R.id.submitJointButton);
        mFirstName = findViewById(R.id.firstName1);
        mSurname = findViewById(R.id.surName1);
        mAddress = findViewById(R.id.address1);
        mContactNumber = findViewById(R.id.contactNumber1);
        mEmail = findViewById(R.id.email1);
        mPPSN = findViewById(R.id.ppsn1);
        mGrossAnnualIncome = findViewById(R.id.grossAnnualIncomeFill1);
        mDOB = findViewById(R.id.dob1);

        mGrossAnnualIncome.setText(getIntent().getStringExtra("coApplicantIncome"));
        userID = getIntent().getStringExtra("userID");

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

    public void submitForm() {
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
                application.put("ID", getIntent().getStringExtra("ID"));
                application.put("applicationType", "Joint");
                application.put("loanAmount", getIntent().getStringExtra("loanAmount"));
                application.put("loanTerm", getIntent().getStringExtra("loanTerm"));
                application.put("title", getIntent().getStringExtra("title"));
                application.put("firstName", firstName);
                application.put("surName", surname);
                application.put("dateOfBirth", getIntent().getStringExtra("dateOfBirth"));
                application.put("address", address);
                application.put("phoneNumber", getIntent().getStringExtra("phoneNumber"));
                application.put("email", email);
                application.put("ppsNumber", getIntent().getStringExtra("ppsNumber"));
                application.put("grossAnnualIncome",  getIntent().getStringExtra("grossAnnualIncome"));
                application.put("coApplicantFirstName", mFirstName.getText().toString());
                application.put("coApplicantSurname", mSurname.getText().toString());
                application.put("coApplicantContactNumber", mContactNumber.getText().toString());
                application.put("coApplicantEmail", mEmail.getText().toString());
                application.put("coApplicantDateOfBirth", mDOB.getText().toString());
                application.put("coApplicantAddress", mAddress.getText().toString());
                application.put("coApplicantGrossAnnualIncome", mGrossAnnualIncome.getText().toString());
                application.put("coApplicantPPSNumber", mPPSN.getText().toString());
                DocumentReference mDocumentReference = fStore.collection("user_application_forms").document(userID);
                mDocumentReference.set(application);
                Toast.makeText(JointApplication.this, "Your Application is now being processed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onSubmitJoint(View view) {
        userID = fAuth.getCurrentUser().getUid();
        String firstName = mFirstName.getText().toString();
        String surname = mSurname.getText().toString();
        String address = mAddress.getText().toString();
        String contactNumber = mContactNumber.getText().toString();
        String email = mEmail.getText().toString();
        String ppsn = mPPSN.getText().toString();
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
        } else {
            submitForm();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Destroy the activity so it cannot be accessed after logging out.
    }
}