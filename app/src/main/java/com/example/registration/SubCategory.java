package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import com.example.registration.adapter.SubcategoryAdapter;

import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.SubCategoryModel;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    private String userId;
    List<SubCategoryModel> subCategoryList;
    RecyclerView recyclerView;
    private String cat_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        subCategoryList = new ArrayList<>();
        getCatIdByIntent();
        get_shared_pref();
        getSubCategory(cat_id);
        is_connection();

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sub Category");
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void get_shared_pref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        userId = String.valueOf(user.getReg_id());

    }
    private void getCatIdByIntent() {
        cat_id = getIntent().getStringExtra("cat_id");
    }

    private void getSubCategory(String cat_id) {
        final ProgressDialog progressDialog = ProgressDialog.show(SubCategory.this, "Please Wait..", "Loading..", false, false);

        String url = URLs.SUB_CATEGORY + cat_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            progressDialog.dismiss();
            try {
               // progressDialog = ProgressDialog.show(context, "Loading", "Please Wait...", false, false);
                Log.d("Response for Subcategory========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                   // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    //Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>(); // Create list to hold category data
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject subcategoryJson = recordsArray.getJSONObject(i);
                        String subcatId = subcategoryJson.getString("subcat_id");
                        String catId = subcategoryJson.getString("cat_id");
                        String subcategoryName = subcategoryJson.getString("subcat_name");
                        String subcategoryImage = subcategoryJson.getString("subcat_image");
                        /*Log.d("I", String.valueOf(i));
                        Log.d("subcatId==", subcatId);
                        Log.d("catId==", catId);
                        Log.d("subcatName==", subcategoryName);
                        Log.d("subcatImage==", subcategoryImage);*/
                        subCategoryList.add(new SubCategoryModel(subcatId,catId,subcategoryName,subcategoryImage));
                    }
                    //Log.d("CategoryListSize", String.valueOf(subCategoryList.size()));
                    setupRecyclerView(subCategoryList);
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void setupRecyclerView(ArrayList<SubCategoryModel> subCategoryList) {
        recyclerView = findViewById(R.id.subcategoryRecycler);
        //Log.d("RecyclerView", "RecyclerView initialized");
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new SubcategoryAdapter(subCategoryList, SubCategory.this));
        //Log.d("Adapter", "Adapter set successfully");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(SubCategory.this, Category.class);
            startActivity(intent);
            return true;
        }   else if (id == R.id.wishlist) {
                Intent intent = new Intent(SubCategory.this, wish_list.class);
                startActivity(intent);
                return true;
        }else if (id == R.id.cart) {
            Intent intent = new Intent(SubCategory.this, cart.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.myorder) {
            Intent intent = new Intent(SubCategory.this, Orders.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {
            Intent intent = new Intent(SubCategory.this, MyProfile.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.chgpass) {
            Intent intent = new Intent(SubCategory.this, ChangePass.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.feedback) {
            Intent intent = new Intent(SubCategory.this, feedback.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(SubCategory.this, About_us.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.contactus) {
            Intent intent = new Intent(SubCategory.this, contact_us.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.logout) {
            logout();
            return true;
        }
        return false;
    }
    private void logout() {
        SharedPrefManager.getInstance(getApplicationContext()).logout();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_shared_pref();
        return;
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
        if (!isConnected(SubCategory.this)) {
            Toast.makeText(SubCategory.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(SubCategory.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
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