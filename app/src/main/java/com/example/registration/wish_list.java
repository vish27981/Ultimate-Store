package com.example.registration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.adapter.WishlistAdapter;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.example.registration.model.WishListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class wish_list extends AppCompatActivity {

    ListView listView;
    private int UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your WishList");
        sharedPref();
        getWishList(UserId);
    }
    private void sharedPref() {
        User user = SharedPrefManager.getInstance(this).getUser();
        UserId = user.getReg_id();
    }

    private void getWishList(int userId) {
        final ProgressDialog progressDialog =  ProgressDialog.show(wish_list.this,"Please Wait..","Loading..",false,false);

        String url = URLs.URL_GET_WISHLIST + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            progressDialog.dismiss();
            //Log.d("userId", String.valueOf(userId));
            try {
                Log.d("Response of WishList========", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                   //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONArray recordsArray = obj.getJSONArray("records");
                    //Log.d("Array", String.valueOf(recordsArray));
                    ArrayList<WishListModel> wishList = new ArrayList<>();
                    for (int i = 0; i < recordsArray.length(); i++) {
                        JSONObject wishlistJson = recordsArray.getJSONObject(i);
                        String product_name =  wishlistJson.getString("product_name");
                        String product_id = wishlistJson.getString("product_id");
                       int Price = Integer.parseInt(wishlistJson.getString("product_price"));
                        String pimg = wishlistJson.getString("product_image");

                        /*Log.d("I", String.valueOf(i));
                        Log.d("product_id==", product_id);
                        Log.d("Price==", String.valueOf(Price));
                        Log.d("pimg==", pimg);*/

                        wishList.add(new WishListModel(product_id,product_name,pimg,Price));

                    }
                    //Log.d("ProductListSize", String.valueOf(wishList.size()));
                    setupListView(wishList);
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

    private void setupListView(ArrayList<WishListModel> wishList) {
        //Log.d("Cart Response========", String.valueOf(wishList));
        listView = (ListView) findViewById(R.id.list_data);
        WishlistAdapter adapter = new WishlistAdapter(wishList, wish_list.this);
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





