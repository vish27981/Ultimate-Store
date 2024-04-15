package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePass extends AppCompatActivity {

    TextInputLayout oldPassword, newPassword,confirmPassword;
    Button btnChangePass;
    String UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        oldPassword = (TextInputLayout) findViewById(R.id.oldPassword);
        newPassword= (TextInputLayout) findViewById(R.id.newPassword);
        confirmPassword = (TextInputLayout) findViewById(R.id.confirmPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        is_connection();

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(view -> {
            if(!validatePassword()){
                return;
            }
            User user = SharedPrefManager.getInstance(this).getUser();
            UserEmail = user.getReg_email();
            final String old = oldPassword.getEditText().getText().toString();
            final String newPass = newPassword.getEditText().getText().toString();
            changePassword(old,newPass,UserEmail);
        });
    }

    private void changePassword( String old, String newPass,String shEmail) {
        final ProgressDialog progressDialog =  ProgressDialog.show(ChangePass.this,"Please Wait..","Loading..",false,false);

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_PASSWORD, response -> {
            progressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    Toast.makeText(ChangePass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePass.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChangePass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(ChangePass.this,error.getMessage(),Toast.LENGTH_SHORT).show();
        }){
            public Map<String, String>getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("reg_password",old);
                params.put("newpwd",newPass);
                params.put("reg_email",shEmail);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private boolean validatePassword(){
        String passwordOld = oldPassword.getEditText().getText().toString();
        String passwordNew = newPassword.getEditText().getText().toString().trim();
        String passwordConfirm= confirmPassword.getEditText().getText().toString().trim();

        if(passwordOld.isEmpty()){
            oldPassword.setError("Field Can't be empty");
            return false;
        }else if(passwordNew.isEmpty()){
            oldPassword.setError(null);
            newPassword.setError("Field Can't be empty");
            return false;
        } else if(passwordConfirm.isEmpty()){
            newPassword.setError(null);
            confirmPassword.setError("Field Can't be empty");
            return false;
        }else if(!passwordNew.equals(passwordConfirm)){
            confirmPassword.setError("Please Confirm Password");
            return false;
        }else{
            oldPassword.setError(null);
            newPassword.setError(null);
            confirmPassword.setError(null);
            return true;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        super.onResume();
    }

    public void onBackPressed() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Exit?")
                .setIcon(R.drawable.logo)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> super.onBackPressed())
                .create()
                .show();
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
        if (!isConnected(ChangePass.this)) {

            Toast.makeText(ChangePass.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(ChangePass.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
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