package com.example.registration;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Button;

import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {

    Button  btnFeedback;
    TextInputEditText edtFeedback;
    RatingBar rbFeedback;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");

        btnFeedback = findViewById(R.id.btnFeedback);
        edtFeedback = findViewById(R.id.edtFeedback);
        rbFeedback = findViewById(R.id.rbFeedback);
        btnFeedback.setOnClickListener(view -> userFeedback());
    }

    //checking all the validations and necessary fields
    private void userFeedback(){
        final String feedbackText = edtFeedback.getText().toString().trim();
       Log.d("Feedback Text", feedbackText);

        final float ratingValue= rbFeedback.getRating();
        Log.d("Rating Value", String.valueOf(ratingValue));


        User user = SharedPrefManager.getInstance(this).getUser();
        String regName = user.getReg_name();
        String regMobile = user.getReg_mobile();
    //    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

        if (TextUtils.isEmpty(feedbackText)) {
            edtFeedback.setError("Please send us your Review");
            edtFeedback.requestFocus();
            return;
        }

        if (ratingValue <= 0.0) {
            Toast.makeText(getApplicationContext(), "Please provide a us stars", Toast.LENGTH_SHORT).show();
            rbFeedback.requestFocus();
            return;

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_FEEDBACK, response -> {
            try {

                JSONObject obj = new JSONObject(response);
                Log.d("JSONObject Response", obj.toString());

                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,Category.class));

                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> Toast.makeText(getApplicationContext(),"ERROR", Toast.LENGTH_SHORT).show()

        ){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reg_name",regName);
                params.put("reg_mobile", regMobile);
                params.put("review",feedbackText);
                params.put("stars", String.valueOf(ratingValue));
                return params;
            }
        };
        //calling all the methods instance and all functionality
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        super.onResume();
        userFeedback();

    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
