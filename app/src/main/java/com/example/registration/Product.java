package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import com.example.registration.adapter.ProductAdapter;

import com.example.registration.model.ProductModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    public static String abc;
    NotificationBadge count;
    private  String UserId;
    List<ProductModel> productList ;
    private  String subcat_id, subcat_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productList = new ArrayList<>();
        get_shared_pref();
        getCatIdByIntent();
        is_connection();

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Products");
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        getProduct(subcat_id);
        cart(UserId);
    }

    private void get_shared_pref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = String.valueOf(user.getReg_id());

    }
    private void getCatIdByIntent() {
        subcat_id = getIntent().getStringExtra("subcat_id");
        subcat_name = getIntent().getStringExtra("subcat_name");
    }
    private void getProduct(String subcat_id) {
        final ProgressDialog progressDialog = ProgressDialog.show(Product.this, "Please Wait..", "Loading..", false, false);

        String url = URLs.URL_PRODUCTS + subcat_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            progressDialog.dismiss();
        try {
            Log.d("Response for Product========", response);
            JSONObject obj = new JSONObject(response);
            if (!obj.getBoolean("error")) {
            //    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                JSONArray recordsArray = obj.getJSONArray("records");
                //Log.d("Array", String.valueOf(recordsArray));
                ArrayList<ProductModel> productList = new ArrayList<>();
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject productJson = recordsArray.getJSONObject(i);
                    String subcatId = productJson.getString("subcat_id");
                    String product_id = productJson.getString("product_id");
                    String productName = productJson.getString("product_name");
                    String productImage = productJson.getString("product_image");
                    String price = productJson.getString("product_price");
                    String product_desc = productJson.getString("product_desc");
                    /*Log.d("I", String.valueOf(i));
                    Log.d("productId==", product_id);
                    Log.d("subcatId==", subcatId);
                    Log.d("productName==", productName);
                    Log.d("productImage==", productImage);
                    Log.d("price==", price);*/
                    productList.add(new ProductModel(subcatId,product_id, productName,productImage, price,product_desc));
                }
                //Log.d("ProductListSize", String.valueOf(productList.size()));
                setupRecyclerView(productList);
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

    private void setupRecyclerView(ArrayList<ProductModel> productList) {
        recyclerView = findViewById(R.id.productRecycler);
        //Log.d("RecyclerView", "RecyclerView initialized");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new ProductAdapter(productList, Product.this));
        //Log.d("Adapter", "Adapter set successfully");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uppermenu,menu);

            View cartActionView = menu.findItem(R.id.cart_menu).getActionView();
            count = cartActionView.findViewById(R.id.badge);
            count.setText(abc);
            cartActionView.findViewById(R.id.cart_container).setOnClickListener(view -> startActivity(new Intent(Product.this, cart.class)));
            cartActionView.findViewById(R.id.cart_img).setOnClickListener(view -> startActivity(new Intent(Product.this, cart.class)));

            return true;
        }


    public void cart(String userId) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CART_COUNT, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                abc = jsonObject.getString("records");
                invalidateOptionsMenu();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reg_id", userId);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(Product.this, Category.class);
            startActivity(intent);
            return true;
        }   else if (id == R.id.wishlist) {
            Intent intent = new Intent(Product.this, wish_list.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.cart) {
            Intent intent = new Intent(Product.this, cart.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.myorder) {
            Intent intent = new Intent(Product.this, Orders.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.profile) {
            Intent intent = new Intent(Product.this, MyProfile.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.chgpass) {
            Intent intent = new Intent(Product.this, ChangePass.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.feedback) {
            Intent intent = new Intent(Product.this, feedback.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.aboutus) {
            Intent intent = new Intent(Product.this, About_us.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.contactus) {
            Intent intent = new Intent(Product.this, contact_us.class);
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
        cart(UserId);
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
        if (!isConnected(Product.this)) {
            Toast.makeText(Product.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(Product.this).setTitle("No Internet Connection").setIcon(android.R.drawable.stat_notify_error)
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