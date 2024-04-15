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

import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;


import com.example.registration.model.URLs;

import com.example.registration.model.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

 public class MainActivity extends AppCompatActivity {

     //creating a globel variable
    TextInputEditText edtname , edtemail1, edtmobile, edtpassword2 , edtRepassword2;
    TextView txtAlready;
    Button btnReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("SIGN UP");

        is_connection();

        //fetching all the ids from xml activity file
        edtname = findViewById(R.id.edtName);
        edtemail1 = findViewById(R.id.edtemail1);
        edtmobile = findViewById(R.id.edtmobile);
        edtpassword2 = findViewById(R.id.edtpassword2);
        edtRepassword2 = findViewById(R.id.edtRepassword2);
        btnReg = findViewById(R.id.btnReg);
        txtAlready = findViewById(R.id.txtAlready);
        String fullText = "Already have an Account? SIGN IN";

        SpannableString spannableString = new SpannableString(fullText);
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);

        int startIndex = fullText.indexOf("SIGN IN");
        int endIndex = startIndex + "SIGN IN".length();
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableString.setSpan(italicSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan,startIndex,endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Set the text in your TextView
        txtAlready.setText(spannableString);

        txtAlready.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        });
      btnReg.setOnClickListener(view -> userReg());
    }

    //checking all the validations and necessary fields
     private void userReg(){
         final String reg_name = edtname.getText().toString().trim();
         final String reg_mobile = edtmobile.getText().toString().trim();
         final String reg_email = edtemail1.getText().toString().trim();
         final String reg_password = edtpassword2.getText().toString().trim();
         final String reg_re_password = edtRepassword2.getText().toString().trim();


         Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");


         Pattern mobilePattern = Pattern.compile("^[6-9]\\d{9}$"); // Mobile number should start with 6, 7, 8, or 9


         if(TextUtils.isEmpty(reg_name)){
             edtname.setError("Please Fill the required details!!");
             edtname.requestFocus();
             return;
         }

         if(TextUtils.isEmpty(reg_mobile)){
             edtmobile.setError("Please Fill the required details!!");
             edtmobile.requestFocus();
             return;
         } else if (!mobilePattern.matcher(reg_mobile).matches()) {
             edtmobile.setError("Please enter a valid mobile number!!");
             edtmobile.requestFocus();
             return;
         }


         if (TextUtils.isEmpty(reg_email)){
             edtemail1.setError("Please Fill the required details!!");
             edtemail1.requestFocus();
             return;
         } else if (!emailPattern.matcher(reg_email).matches()) {
             edtemail1.setError("Please enter a valid email address!!");
             edtemail1.requestFocus();
             return;
         }


         if (TextUtils.isEmpty(reg_password)) {
             edtpassword2.setError("Please Fill the required details!!");
             edtpassword2.requestFocus();
             return;
         }

         if(TextUtils.isEmpty(reg_re_password)){
             edtRepassword2.setError("Please Fill the required details!!");
             edtRepassword2.requestFocus();
             return;
         } else if (!reg_password.equals(reg_re_password)) {
             edtRepassword2.setError("Passwords do not match!!");
             edtRepassword2.requestFocus();
             return;
         }



         final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait..", "Loading..", false, false);

         //registration using string request
         StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SIGN_UP, response -> {
             progressDialog.dismiss();
             try {
                 JSONObject obj = new JSONObject(response);
                 if (!obj.getBoolean("error")) {
                    // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(this,Login.class));
                 } else {
                     Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                 }
             } catch (JSONException e) {
                 e.printStackTrace();

             }
         },error -> {
             progressDialog.dismiss();
             Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
         }){
             //mapping all the parameters with the api values
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> params = new HashMap<>();
                 params.put("reg_name", reg_name);
                 params.put("reg_mobile", reg_mobile);
                 params.put("reg_email", reg_email);
                 params.put("reg_password", reg_password);
                 return params;
             }
             };
         VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
     }

     public void onResume(){
        super.onResume();
        userReg();

     }

     @Override
     public void onBackPressed() {
         super.onBackPressed();
         finish();
     }

     //No internet connection
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
         if (!isConnected(MainActivity.this)) {
             Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
             new android.app.AlertDialog.Builder(MainActivity.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
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
