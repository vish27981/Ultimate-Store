package com.example.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.adapter.OrderDetailsAdapter;
import com.example.registration.adapter.ViewProductAdapter;
import com.example.registration.model.OrderDetailsModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.ViewProductModel;
import com.example.registration.model.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    String UserId,product_id;
    List<OrderDetailsModel> orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);

        orderDetails = new ArrayList<>();
        get_shared_pref();
        getProductByIntent();
        getOrderDetails(UserId, product_id);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
    }

    private void get_shared_pref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = String.valueOf(user.getReg_id());

    }

    private void getProductByIntent() {
        product_id = getIntent().getStringExtra("product_id");
    }

private void getOrderDetails(String userId, String productId)  {
       String url = URLs.URL_ORDER_DETAILS1 + userId + productId;
    StringRequest stringRequest = new StringRequest( Request.Method.GET,url, response -> {
            try {
                Log.d("Response for View Product========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    //    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<OrderDetailsModel> orderDetails = new ArrayList<>();
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject record = recordsArray.getJSONObject(i);
                        String productName = record.getString("product_name");
                    String productImage = record.getString("product_image");
                    String ship_add =  record.getString("ship_add");
                    String payment_mode = record.getString("payment_mode");
                    int quantity = record.getInt("quantity");
                    int total_bill = record.getInt("product_price");
                    orderDetails.add(new OrderDetailsModel(productName,productImage,ship_add,payment_mode,quantity,total_bill));
                                }
                    setupRecyclerView(orderDetails);
                } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {

                e.printStackTrace();
                            }
                            }, error -> Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show());
                            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void setupRecyclerView(ArrayList<OrderDetailsModel> orderDetails) {
        recyclerView = findViewById(R.id.OrderDetailsRecycler);
        Log.d("RecyclerView", "RecyclerView initialized");
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new OrderDetailsAdapter(orderDetails, OrderDetails.this));
        Log.d("Adapter", "Adapter set successfully");
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