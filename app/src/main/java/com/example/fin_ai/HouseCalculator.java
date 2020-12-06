package com.example.fin_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class HouseCalculator extends AppCompatActivity {

    EditText mSqftLiving, mBathrooms, mView, mSqftBasement, mBedrooms, mFloors;
    Button mEstimatePriceBtn;
    CheckBox mWaterfrontCheckbox;
    TextView mEstimatedPriceTextView;

    String sqftLiving = "";
    String bathrooms = "";
    String view = "";
    String sqftBasement = "";
    String bedrooms = "";
    String waterfront = "";
    String floors = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_calculator);

        // Finding actual buttons / spinners etc and assigning them to the variables.
        mSqftLiving = findViewById(R.id.sqft_living);
        mBathrooms = findViewById(R.id.bathrooms);
        mView = findViewById(R.id.view);
        mSqftBasement = findViewById(R.id.sqft_basement);
        mBedrooms = findViewById(R.id.bedrooms);
        mFloors = findViewById(R.id.Floors);
        mEstimatePriceBtn = findViewById(R.id.estimatePriceBtn);
        mEstimatedPriceTextView = findViewById(R.id.estimatedPriceTextView);

        mWaterfrontCheckbox = findViewById(R.id.waterfrontCheckbox);

        mEstimatePriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sqftLiving = mSqftLiving.getText().toString();
                String bathrooms = mBathrooms.getText().toString();
                String view = mView.getText().toString();
                String sqftBasement = mSqftBasement.getText().toString();
                String bedrooms = mBedrooms.getText().toString();
                String floors = mFloors.getText().toString();

                if (TextUtils.isEmpty(sqftLiving)){
                    mSqftLiving.setError("Sqft living field is empty.");
                }
                else if (TextUtils.isEmpty(bathrooms)) {
                    mBathrooms.setError("Bathrooms field is empty.");
                }
                else if (TextUtils.isEmpty(view)) {
                    mView.setError("View field is empty.");
                }
                else if (TextUtils.isEmpty(sqftBasement)) {
                    mSqftBasement.setError("Sqft basement field is empty.");
                }
                else if (TextUtils.isEmpty(bedrooms)) {
                    mBedrooms.setError("Bedrooms field is empty.");
                }
                else if (TextUtils.isEmpty(floors)) {
                    mFloors.setError("Floors field is empty.");
                }

                if (mWaterfrontCheckbox.isChecked()) {
                    waterfront = "1";
                } else {
                    waterfront = "0";
                }

                // Build up a JSON string to pass to the Fin-Ai Engine API.

                String data = "{\n" +
                        "\"sqft_living\":" + "\"" + sqftLiving + "\",\n" +
                        "\"bathrooms\":" + "\"" + bathrooms + "\",\n" +
                        "\"view\":" + "\"" + view + "\",\n" +
                        "\"sqft_basement\":" + "\"" + sqftBasement + "\",\n" +
                        "\"bedrooms\":" + "\"" + bedrooms + "\",\n" +
                        "\"waterfront\":" + "\"" + waterfront + "\",\n" +
                        "\"floors\":" + "\"" + floors + "\"\n" +
                        "}";
                Log.d("TEST", data);
                // POST the data to the API.
                SubmitData(data);
            }

            private void SubmitData(String obj) {
                // Volley post method
                // Creating a RequestQueue instance
                final String savedata = obj;
                String url = "http://192.168.0.129:5000/getEstimateHousePrice"; // this has to be changed to your computers local ipv4 address (cmd->ipconfig)
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mEstimatedPriceTextView.setText("Estimated House Price: $" + String.format("%.0f", Double.parseDouble(response)));
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