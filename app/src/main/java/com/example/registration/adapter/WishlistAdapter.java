package com.example.registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.VolleySingleton;
import com.example.registration.model.WishListModel;
import com.example.registration.Product;
import com.example.registration.R;
import com.example.registration.cart;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WishlistAdapter extends BaseAdapter {

    List<WishListModel> wishList;
    Context context;
    String userId;
    TextView product_name,product_price,product_id;
    Button addToCart;
    ImageView product_image;

    public WishlistAdapter(List<WishListModel> wishList, Context context){
        this.wishList = wishList;
        this.context = context;

        User user = SharedPrefManager.getInstance(context).getUser();
        userId = String.valueOf(user.getReg_id());
    }


    @Override
    public int getCount() {
        return wishList.size();
    }

    @Override
    public Object getItem(int position) {
        return wishList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wish_list, parent, false);
        }
        product_id = convertView.findViewById(R.id.product_id);
        product_name = convertView.findViewById(R.id.product_name);
        product_price = convertView.findViewById(R.id.product_price);
        product_image = convertView.findViewById(R.id.img_product);
        addToCart = convertView.findViewById(R.id.addToCart);

        product_id.setText(wishList.get(position).getProduct_id());
        product_name.setText(wishList.get(position).getProduct_name());
        product_price.setText("Rs." + wishList.get(position).getProduct_price());
        Picasso.get().load(URLs.PRODUCT_IMG_URL + wishList.get(position).getProduct_image()).into(product_image);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCart(wishList.get(position).getProduct_id(),wishList.get(position).getProduct_name(),
                        wishList.get(position).getProduct_price(),userId);
            }
        });
        return convertView;
    }

    private void insertCart(String product_id, String getname, String price, String userId) {
      //  Log.d("product_id====" + product_id , "name====" + getname);
        String url = URLs.URL_CART ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("Response for insert cart from wishlist ======== ", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    if(context instanceof Product){
                        ((Product) context).cart(userId);
                    }
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, cart.class);
                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }, error -> Toast.makeText(context,"ERROR", Toast.LENGTH_SHORT).show()

        ){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_id);
                params.put("product_name", getname);
                params.put("product_price", price);
                params.put("reg_id", userId);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
