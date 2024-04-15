package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registration.adapter.OrderAdapter;
import com.example.registration.model.OrderModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Orders extends AppCompatActivity {

    ListView listView;
    private int UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        sharedPref();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Orders");
        getMyOrderDetails(UserId);
    }

    private void sharedPref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = user.getReg_id();
    }

    private void getMyOrderDetails(int userId) {
        String url = URLs.URL_ORDER_DETAILS + userId ;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d("Response for Orders ============",response);
                            JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                         //   Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            ArrayList<OrderModel> orderList = new ArrayList<>();
                            JSONArray recordsArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < recordsArray.length(); i++) {
                                JSONObject recordObject = recordsArray.getJSONObject(i);
                              //  String product_name = recordObject.getString("product_name");
                               // String product_image = recordObject.getString("product_image");
                                int price = recordObject.getInt("total_bill");
                                String date = recordObject.getString("order_date");
                                orderList.add(new OrderModel(price, date));
                            }
                            //Log.d("ProductListSize", String.valueOf(orderList.size()));
                            setupListView(orderList);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void setupListView(ArrayList<OrderModel> orderList) {
        listView = findViewById(R.id.list_data);
        OrderAdapter adapter = new OrderAdapter(orderList,Orders.this);
        listView.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}