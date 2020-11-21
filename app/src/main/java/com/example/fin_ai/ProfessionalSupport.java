package com.example.fin_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfessionalSupport extends AppCompatActivity {

    Spinner mContactTypeSpinner;
    TextView mContactTime, mTopic, mEmail;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    ArrayList<String> mContactTypes = new ArrayList<>();

    String userID, officerID;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_support);

        mContactTypes.add("Phone");
        mContactTypes.add("Email");
        mEmail = findViewById(R.id.officerEmail);
        mTopic = findViewById(R.id.topic);
        mContactTypeSpinner = findViewById(R.id.contactTypeSpinner);
        mContactTypeSpinner.setSelection(0);
        mContactTime = findViewById(R.id.preferredContactTime);
        mContactTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(ProfessionalSupport.this,
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
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                mContactTime.setText(date);
            }
        };

        ArrayAdapter<String> contactTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mContactTypes);
        contactTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContactTypeSpinner.setAdapter(contactTypeAdapter);

        userID = fAuth.getCurrentUser().getUid();
        DocumentReference userInfoDocument = fStore.collection("user_application_forms").document(userID);
        userInfoDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                officerID = snapshot.get("ID").toString();
                CollectionReference usersCollection = fStore.collection("users");
                usersCollection.whereEqualTo("ID", officerID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mEmail.setText(document.get("email").toString());
                            }
                        }
                    }
                });
            }
        });
    }

    public void onSubmitContactForm(View view) {
        String preferredContactTime = mContactTime.getText().toString();
        String contactType = mContactTypeSpinner.getSelectedItem().toString();
        String topic = mTopic.getText().toString();
        if (TextUtils.isEmpty(preferredContactTime)) {
            mContactTime.setError("Contact Time field is empty.");
        } else if (TextUtils.isEmpty(topic)) {
            mTopic.setError("Topic field is empty");
        } else {
            Map<String,Object> application = new HashMap<>();
            application.put("ID", officerID);
            application.put("contactType", contactType);
            application.put("contactTime", preferredContactTime);
            application.put("topic", topic);
            DocumentReference contactFormDocument = fStore.collection("user_professional_contact_forms").document(userID);
            contactFormDocument.set(application);
            Toast.makeText(ProfessionalSupport.this, "Your contact form was submitted successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}