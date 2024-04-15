package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.registration.model.URLs;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ForgetPassword extends AppCompatActivity {

    TextInputLayout username;
    TextView signUp;

    Button sendMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        username = findViewById(R.id.username);
        sendMail = findViewById(R.id.sentMail);
        signUp = findViewById(R.id.signUp);

        getSupportActionBar().setTitle("Forget Password");
        is_connection();

        String fullText = "Don't have an Account? SIGN UP";

        SpannableString spannableString = new SpannableString(fullText);
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);

        int startIndex = fullText.indexOf("SIGN UP");
        int endIndex = startIndex + "SIGN UP".length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(italicSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan,startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the text in your TextView
        signUp.setText(spannableString);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        sendMail.setOnClickListener(view -> {
            String Uname = username.getEditText().getText().toString();

            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

                    if (TextUtils.isEmpty(Uname)){
                        username.setError("Please Fill the required details!!");
                        username.requestFocus();
                        return;
                    } else if (!emailPattern.matcher(Uname).matches()) {
                        username.setError("Please enter a valid email address!!");
                        username.requestFocus();
                        return;
                    }

                forgetPassword(Uname);
        }
        );

    }
    private void forgetPassword(String Uname) {

        final ProgressDialog progressDialog = ProgressDialog.show(ForgetPassword.this, "Please Wait..", "Loading..", false, false);

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_FORGET_PASSWORD, response -> {
            progressDialog.dismiss();
            try {
                Log.d("Response for Forget Password",response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    Toast.makeText(ForgetPassword.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgetPassword.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ForgetPassword.this, "Error occured in sending Email", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(ForgetPassword.this, "Error Parsing Json", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reg_email", Uname);
                Log.d("name ==============",Uname);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


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

    private void is_connection() {
        if (!isConnected(ForgetPassword.this)) {
            Toast.makeText(ForgetPassword.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            new android.app.AlertDialog.Builder(ForgetPassword.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
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