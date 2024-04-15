package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.adapter.ViewProductAdapter;

import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.ViewProductModel;
import com.example.registration.model.VolleySingleton;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProduct extends AppCompatActivity {

    RecyclerView recyclerView;
    private  String product_id;
    private String UserId;
    public static String abc;
    NotificationBadge count;
    List<ViewProductModel> viewproductModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        viewproductModelList = new ArrayList<>();
        get_shared_pref();
        getProductByIntent();
        getProduct(product_id);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Products");

    }

    private void getProductByIntent() {
        product_id = getIntent().getStringExtra("product_id");
        Log.d("MY Product ID IS ===============================", product_id);
    }
    private void get_shared_pref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = String.valueOf(user.getReg_id());

    }
    private void getProduct(String product_id) {
        String url = URLs.URL_VIEW_PRODUCT + product_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Log.d("Response for View Product========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    //    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<ViewProductModel> viewproductModelList = new ArrayList<>();
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject productJson = recordsArray.getJSONObject(i);
                        String productId = productJson.getString("product_id");
                        String productName = productJson.getString("product_name");
                        String productImage = productJson.getString("product_image");
                        String price = productJson.getString("product_price");
                        String product_desc = productJson.getString("product_desc");
                       /* Log.d("I", String.valueOf(i));
                        Log.d("productId==", product_id);
                        Log.d("subcatId==", subcatId);
                        Log.d("productName==", productName);
                        Log.d("productImage==", productImage);
                        Log.d("price==", price);*/
                        viewproductModelList.add(new ViewProductModel(productId, productName,productImage, price,product_desc));//seq should be followed according to model
                    }
                    Log.d("ProductListSize", String.valueOf(viewproductModelList.size()));
                    setupRecyclerView(viewproductModelList);
                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show());
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void setupRecyclerView(ArrayList<ViewProductModel> viewproductModelList) {
        recyclerView = findViewById(R.id.ViewProductRecycler);
        Log.d("RecyclerView", "RecyclerView initialized");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new ViewProductAdapter(viewproductModelList, ViewProduct.this));
        Log.d("Adapter", "Adapter set successfully");
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uppermenu,menu);

        View cartActionView = menu.findItem(R.id.cart_menu).getActionView();
        count = cartActionView.findViewById(R.id.badge);
        count.setText(abc);
        cartActionView.findViewById(R.id.cart_container).setOnClickListener(view -> startActivity(new Intent(ViewProduct.this, cart.class)));
        cartActionView.findViewById(R.id.cart_img).setOnClickListener(view -> startActivity(new Intent(ViewProduct.this, cart.class)));

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

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}