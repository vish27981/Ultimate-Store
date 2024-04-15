package com.example.registration;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import android.widget.Button;
import android.widget.TextView;
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

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    TextInputEditText username,password;
    TextView ForgetPass,txtNeed;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        ForgetPass = findViewById(R.id.Forgetpass);
        txtNeed = findViewById(R.id.txtNeed);
        btnLogin = findViewById(R.id.btnLogin);

        // Set action bar title
        getSupportActionBar().setTitle("Login");

        // Check internet connection
        is_connection();


        String fullText = "Need an Account?  SIGN UP";

        SpannableString spannableString = new SpannableString(fullText);
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
        int startIndex = fullText.indexOf("SIGN UP");
        int endIndex = startIndex + "SIGN UP".length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(italicSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan,startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text in your TextView
        txtNeed.setText(spannableString);
        txtNeed.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, MainActivity.class);
            Login.this.startActivity(intent);

        });

        // Click listener for "Forget Password?" text
        ForgetPass.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, ForgetPassword.class);
            Login.this.startActivity(intent);

        });

        // If user is already logged in, redirect to Category activity
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(this, Category.class));
            finish();
        }
        // Click listener for login button
        btnLogin.setOnClickListener(view -> userLogin());



    }
    // Method to handle user login
    private void userLogin(){
        final String email = username.getText().toString();
        final String pass = password.getText().toString();

        // Pattern for email validation
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        // Validate email
        if (TextUtils.isEmpty(email)){
            username.setError("Please Fill the required details!!");
            username.requestFocus();
            return;
        } else if (!emailPattern.matcher(email).matches()) {
            username.setError("Please enter a valid email address!!");
            username.requestFocus();
            return;
        }

        // Validate password
        if(TextUtils.isEmpty(pass)){
            password.setError("Please Fill the required details!!");
            password.requestFocus();
            return;
        }

        // Make a login request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN, response -> {
            try {
                Log.d("Login Response=====",response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    // If login is successful, save user data and redirect to Category activity
                  // Toast.makeText(getApplicationContext(), "Login SuccessFull", Toast.LENGTH_SHORT).show();
                    JSONObject userJson = (JSONObject) obj.getJSONArray("records").get(0);
                    User user = new User(
                            userJson.getInt("reg_id"),
                            userJson.getString("reg_name"),
                            userJson.getString("reg_mobile"),
                            userJson.getString("reg_email"),
                            userJson.getString("reg_password"));
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    finish();
                    startActivity(new Intent(getApplicationContext(), Category.class));
                } else {
                    // Show error message if login fails
                    Toast.makeText(getApplicationContext(), "Error occured while Login ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(),"ERROR", Toast.LENGTH_SHORT).show()
         ){
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parameters for login request
                Map<String, String> params = new HashMap<>();
                params.put("reg_email", email);
                params.put("reg_password", pass);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onResume(){
        super.onResume();
        userLogin();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    // Method to check internet connection
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // Show dialog if no internet connection
    private void is_connection() {
        if (!isConnected(Login.this)) {
            Toast.makeText(Login.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            new android.app.AlertDialog.Builder(Login.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
                    .setMessage("Check your internet connection and try again.")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();

        }
    }
}

