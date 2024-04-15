package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.registration.adapter.CategoryAdapter;
import com.example.registration.model.CategoryModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Category extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
     String userId;
    List<CategoryModel> category;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getCategory();
        get_shared_pref();

        category = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category");
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void get_shared_pref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        userId = String.valueOf(user.getReg_id());

    }

    //fetching category using string request
    public void getCategory() {
        final ProgressDialog progressDialog = ProgressDialog.show(Category.this, "Please Wait..", "Loading..", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_CATEGORY, response -> {
            progressDialog.dismiss();
            try {
                // progressDialog = ProgressDialog.show(context, "Loading", "Please Wait...", false, false);
                Log.d("Response of Category ========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    //  Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    // Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<CategoryModel> categoryList = new ArrayList<>(); // Create list to hold category data
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject categoryJson = recordsArray.getJSONObject(i);
                        String catId = categoryJson.getString("cat_id");
                        String categoryName = categoryJson.getString("cat_name");
                        String categoryImage = categoryJson.getString("cat_image");
                        /*Log.d("catId==", String.valueOf(catId));
                        Log.d("catName==", categoryName);
                        Log.d("catImage==", categoryImage);*/
                        categoryList.add(new CategoryModel(catId, categoryName, categoryImage));
                    }

                    //Log.d("CategoryListSize", String.valueOf(categoryList.size())); // Log the size of categoryList
                    setupRecyclerView(categoryList);
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    //setting the recycler view with category
    private void setupRecyclerView(ArrayList<CategoryModel> categoryList) {
        // Set up RecyclerView after parsing all data
        recyclerView = findViewById(R.id.categoryRecycler);
        //Log.d("RecyclerView", "RecyclerView initialized");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CategoryAdapter(categoryList, Category.this));
        //Log.d("Adapter", "Adapter set successfully");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //navigation menus
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(Category.this, Category.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.wishlist) {
            Intent intent = new Intent(Category.this, wish_list.class);
            startActivity(intent);
            return true;

        }else if (id == R.id.cart) {
            Intent intent = new Intent(Category.this, cart.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.myorder) {
            Intent intent = new Intent(Category.this, Orders.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {
            Intent intent = new Intent(Category.this, MyProfile.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.chgpass) {
            Intent intent = new Intent(Category.this, ChangePass.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.feedback) {
            Intent intent = new Intent(Category.this, feedback.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(Category.this, About_us.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.contactus) {
            Intent intent = new Intent(Category.this, contact_us.class);
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

        // for back press
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Exit?")
                .setIcon(R.drawable.logo)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> super.onBackPressed())
                .create()
                .show();
    }

    protected void onResume() {
        super.onResume();
        get_shared_pref();
        getCategory();

    }
    }


