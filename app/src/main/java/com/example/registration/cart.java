package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.example.registration.model.CartModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.razorpay.Checkout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cart extends AppCompatActivity  {

    TextView product_amount, tv_total;
    CartAdapter adapter;
    List<CartModel> cartData = new ArrayList<CartModel>();
    Context context = cart.this;
     ListView listView_cart;
    private int UserId,total;

    private RelativeLayout cart_container, empty_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Checkout.preload(getApplicationContext());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");

        cart_container = findViewById(R.id.activity_cart_container);
        empty_container = findViewById(R.id.activity_cart_empty);
        product_amount = findViewById(R.id.amount_tv);
        tv_total = findViewById(R.id.txt);

        adapter = new CartAdapter(cartData, context);
        sharedPref();
        getCart(UserId);
    }

    private void sharedPref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = user.getReg_id();
    }
    private void getCart(int userId) {
        //Log.d("User id is ======",String.valueOf(userId));
        final ProgressDialog progressDialog =  ProgressDialog.show(cart.this,"Please Wait..","Loading..",false,false);

        String url = URLs.URL_CART_DATA + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            progressDialog.dismiss();
            Log.d("userId", String.valueOf(userId));
            try {
                Log.d("Response for Cart========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                 // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    //Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<CartModel> cartData = new ArrayList<>();
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject cartJson = recordsArray.getJSONObject(i);
                        String product_id = cartJson.getString("product_id");
                        String productName = cartJson.getString("product_name");
                        String productImage = cartJson.getString("product_image");
                        int price = Integer.parseInt(String.valueOf(cartJson.getInt("product_price")));
                        int quantity = Integer.parseInt(String.valueOf(cartJson.getInt("quantity")));

                        /*Log.d("I", String.valueOf(i));
                        Log.d("productName==", productName);
                        Log.d("productImage==", productImage);
                        Log.d("price==", String.valueOf(price));*/
                        cartData.add(new CartModel(product_id, productName,productImage, price,quantity));
                    }
                    //Log.d("CartSize", String.valueOf(cartData.size()));
                    setupListView(cartData);
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
    private void setupListView(ArrayList<CartModel> cartData) {
        //Log.d("Cart Response========", String.valueOf(cartData));
        listView_cart = findViewById(R.id.list_data);
        CartAdapter adapter = new CartAdapter(cartData, cart.this);
        listView_cart.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void proceed_to_pay(View view) {
        Intent intent = new Intent(context, payment.class);
        intent.putExtra("total_bill", product_amount.getText().toString());
        startActivity(intent);
        finish();
    }



    public class CartAdapter extends BaseAdapter {

        List<CartModel> cartData;
        Context context;
        int total;

        public CartAdapter(List<CartModel> cartData, Context context) {
            this.cartData = cartData;
            this.context = context;
            this.total = 0;
            User user = SharedPrefManager.getInstance(context).getUser();
            UserId = Integer.parseInt(String.valueOf(user.getReg_id()));
        }

        @Override
        public int getCount() {
            return cartData.size();
        }

        @Override
        public Object getItem(int i) {
            return cartData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.cart_list, parent, false);
                holder = new ViewHolder();
                holder.product_id = convertView.findViewById(R.id.product_id);
                holder.product_name = convertView.findViewById(R.id.txt_product_name);
                holder.product_price = convertView.findViewById(R.id.txt_product_price);
                holder.img_product = convertView.findViewById(R.id.img_product);
                holder.remove = convertView.findViewById(R.id.remove);
                holder.spin = convertView.findViewById(R.id.spinner);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            if (position >= 0 && position < cartData.size()) {
                CartModel cartItem = cartData.get(position);
                // Populate views with data from cartItem
                holder.product_id.setText(cartItem.getProduct_id());
                holder.product_name.setText(cartItem.getProduct_name());
                holder.product_price.setText("Rs." + String.valueOf(cartItem.getProduct_price()));
                Picasso.get().load(URLs.PRODUCT_IMG_URL + cartItem.getProduct_image()).into(holder.img_product);
                holder.remove.setOnClickListener(view -> removeCart(cartItem.getProduct_id(), String.valueOf(UserId)));
                // Set spinner selection and listener
                holder.spin.setSelection(cartItem.getQuantity() - 1);

                //total of product and quantity
               // total += cartItem.getProduct_price() * cartItem.getQuantity();
                //product_amount.setText(String.valueOf(total));

                holder.spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        // Handle spinner item selection
                        int spinValue = i + 1; // Assuming spinner values start from 1
                        updateCart(spinValue, cartItem.getProduct_price(), String.valueOf(UserId), cartItem.getProduct_id());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            } else {
                // Handle case where position is out of bounds
                Log.e("CartAdapter", "Invalid position: " + position);
            }

            return convertView;
        }

        // ViewHolder pattern for efficient view recycling
        class ViewHolder {
            TextView product_id;
            TextView product_name;
            TextView product_price;
            ImageView img_product;
            Button remove;
            Spinner spin;
        }

        private void updateCart(int quantity, int productPrice, String userId, String productId) {
            //final ProgressDialog progressDialog = ProgressDialog.show(cart.this, "Please Wait..", "Loading..", false, false);

            Log.d("updateCart===============", productId);
            String url = URLs.URL_UPDATE_CART;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                       // progressDialog.dismiss();
                        // Handle the response from the server
                        try {
                            //Log.d("Response of Update Cart ==========", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean isError = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");

                            if (!isError) {
                                JSONArray records = jsonResponse.getJSONArray("records");
                                int newTotal = calculateTotalAmount(); // Calculate the new total amount
                                product_amount.setText(String.valueOf(newTotal));

                                adapter.notifyDataSetChanged();

                            } else {
                              //  Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("updateCart", "JSON parsing error: " + e.getMessage());
                        }
                    },
                    error -> {//progressDialog.dismiss();
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    // Calculate the total product price
                    int totalPrice = quantity * productPrice;
                    Map<String, String> params = new HashMap<>();
                    params.put("quantity", String.valueOf(quantity));
                    params.put("product_price", String.valueOf(totalPrice)); // Send the total product price
                    params.put("reg_id", userId);
                    params.put("product_id", productId);
                    return params;
                }
            };
            // Add the request to the request queue
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }


        private void removeCart(String productId, String userId) {
            final ProgressDialog progressDialog = ProgressDialog.show(cart.this, "Please Wait..", "Loading..", false, false);

            if (userId == null) {
                Log.e("removeCart", "userId is null");
                return;
            }
            String url = URLs.URL_DELETE_CART; // Replace with your actual URL
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                progressDialog.dismiss();
                        try {
                            //Log.d("Response for Remove Cart", response);
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean isError = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");

                            if (!isError) {
                                if (jsonResponse.has("records")) {
                                    JSONArray recordsArray = jsonResponse.getJSONArray("records");
                                  // Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();

                                    int newTotal = calculateTotalAmount(); // Calculate the new total amount
                                    product_amount.setText(String.valueOf(newTotal));
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                               // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                product_amount.setText(String.valueOf(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                progressDialog.dismiss();
                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                cartData.clear();
                adapter.notifyDataSetChanged();
                cart_container.setVisibility(View.GONE);
                empty_container.setVisibility(View.VISIBLE);
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("product_id", productId);
                    params.put("reg_id", userId);
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        }

        private int calculateTotalAmount() {
            int total = 0;
            for (CartModel item : cartData) {
                total += item.getProduct_price() * item.getQuantity();
            }
            return total;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();

        sharedPref();
        getCart(UserId);
    }
}