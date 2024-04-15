package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.LinearLayout;

import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyProfile extends AppCompatActivity {
    TextInputEditText reg_name,reg_mobile,reg_email;
    LinearLayout update;
    Context context = this;
    String name,mobile,email;
    String  UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        UserID = String.valueOf(SharedPrefManager.getInstance(this).getUser().getReg_id());
         name = (SharedPrefManager.getInstance(this).getUser().getReg_name());
         mobile = (SharedPrefManager.getInstance(this).getUser().getReg_mobile());
         email = (SharedPrefManager.getInstance(this).getUser().getReg_email());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        get_details(UserID);

        reg_name = findViewById(R.id.textViewUsername);
        reg_mobile = findViewById(R.id.textViewMobile);
        reg_email = findViewById(R.id.textViewEmail);

        update = findViewById(R.id.btn_update);
        update.setOnClickListener(view -> update_details(UserID, reg_name.getText().toString(),
                reg_mobile.getText().toString(),email));

    }

    private void update_details(String userID, String string, String string1, String string2) {
        final ProgressDialog progressDialog =  ProgressDialog.show(MyProfile.this,"Please Wait..","Loading..",false,false);
        {
            //Log.d("name is", "" + name);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_PROFILE_UPDATE, response -> {
                progressDialog.dismiss();
                try {
                    Log.d("Response for My Profile", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        // If update is successful, you might want to update the locally stored user data
                        SharedPrefManager.getInstance(MyProfile.this).saveUser(new User(Integer.parseInt(UserID), name, mobile, email));
                        // Finish the activity and start it again to reflect the changes
                        finish();
                        startActivity(new Intent(context, MyProfile.class));
                    } else {
                        Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }, error -> {
                progressDialog.dismiss();

                Toast.makeText(MyProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }) {

                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("reg_id", userID);
                    params.put("reg_name", string);
                    params.put("reg_mobile", string1);
                    params.put("reg_email", string2);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }


    }

    private void get_details(String userID) {
           StringRequest request = new StringRequest(Request.Method.POST,URLs.URL_GET_PROFILE, response -> {

               try {
                Log.d("Response", response);
                   JSONObject jsonObject = new JSONObject(response);
                   JSONObject jsonObject2 = (JSONObject) jsonObject.getJSONArray("records").get(0);
                   if (!jsonObject.getBoolean("error")) {

                       reg_name.setText(jsonObject2.getString("reg_name"));
                       reg_email.setText(jsonObject2.getString("reg_email"));
                       reg_mobile.setText(jsonObject2.getString("reg_mobile"));

                       return;
                   } else {
                       Toast.makeText(MyProfile.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
                   Toast.makeText(MyProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }

           }, error -> {
           }) {
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("reg_id", userID);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
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
        super.onBackPressed();
        finish();
    }



}