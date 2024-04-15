package com.example.registration.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.model.ViewProductModel;
import com.example.registration.model.VolleySingleton;
import com.example.registration.R;
import com.example.registration.ViewProduct;
import com.example.registration.cart;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProductAdapter extends RecyclerView.Adapter<ViewProductAdapter.ViewHolder>  {

    private List<ViewProductModel> viewproductModelList;

    private Context context;
    LayoutInflater inflater;

    String UserId;
    private boolean isAddedToWishlist = false;

    public ViewProductAdapter(List<ViewProductModel> viewproductModelList, Context context) {
        this.viewproductModelList = viewproductModelList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        User user = SharedPrefManager.getInstance(context).getUser();
        UserId = String.valueOf(user.getReg_id());
    }

    @NonNull
    @Override
    public ViewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_product_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewProductAdapter.ViewHolder holder, int position) {

            holder.setProductId(viewproductModelList.get(position).getProduct_id());
            holder.setProductName(viewproductModelList.get(position).getProduct_name());
            holder.setPrice("Rs." + viewproductModelList.get(position).getPrice());
            holder.setDescription(viewproductModelList.get(position).getProduct_desc());
            Picasso.get().load(URLs.PRODUCT_IMG_URL + viewproductModelList.get(position).getProduct_image()).into(holder.ProductImage);
            Log.d("Product Img",URLs.PRODUCT_IMG_URL+ viewproductModelList.get(position).getProduct_image());

            holder.wishList.setOnClickListener(v -> {
                isAddedToWishlist = !isAddedToWishlist;
                insertWishlist(viewproductModelList.get(position).getProduct_id(),viewproductModelList.get(position).getPrice(),viewproductModelList.get(position).getProduct_name(),UserId);
            });
            holder.AddToCart.setOnClickListener(view -> {
                insertCart(viewproductModelList.get(position).getProduct_id(),viewproductModelList.get(position).getPrice(),viewproductModelList.get(position).getProduct_name(),UserId);
            });
        }
    private void insertWishlist(String productId, String price, String productName, String userId) {

        Log.d("insertCart Params", "product_id: " + productId + ", name: " + productName + ", price: " + price + ", userId: " + userId);
        String url = URLs.URL_WISHLIST;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("JSON Response", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", productId);
                params.put("product_name", productName);
                params.put("product_price", price); // Use the captured product_price
                params.put("reg_id", userId);
                Log.d("Request Params", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void insertCart(String product_id, String product_price, String product_name,String userId) {
        //Log.d("insertCart Params", "product_id: " + product_id + ", name: " + product_name +", price: " + product_price + ", userId: " + userId);
        String url = URLs.URL_CART;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("Response for insert cart", response);
                JSONObject obj = new JSONObject(response);
                if (!obj.getBoolean("error")) {
                    if (context instanceof ViewProduct) {
                        ((ViewProduct) context).cart(userId);
                    }
                    //Toast.makeText(context, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, cart.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_id);
                params.put("product_name", product_name);
                params.put("product_price", product_price);
                params.put("reg_id", userId);
               // Log.d("Request Params", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    @Override
    public int getItemCount() {
        return viewproductModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ProductId;
        private final TextView ProductName ;
        private final TextView ProductPrice,ProductDesc;
        private final ImageView ProductImage, wishList;
        Button AddToCart;

        public ViewHolder(View itemView) {
            super(itemView);
            ProductId= itemView.findViewById(R.id.product_id);
            ProductName = itemView.findViewById(R.id.name);
            ProductPrice = itemView.findViewById(R.id.Price);
            ProductImage = itemView.findViewById(R.id.pimg);
            ProductDesc = itemView.findViewById(R.id.product_desc);
            wishList = itemView.findViewById(R.id.wishlist);
            ProductDesc.setMovementMethod(new ScrollingMovementMethod());
            AddToCart = itemView.findViewById(R.id.cart);
        }
        public void setProductId(String product_id){
            ProductId.setText(product_id);
        }
        public void setProductName(String product_name){
            ProductName.setText(product_name);
        }
        public void setPrice(String price){
            ProductPrice.setText(price);
        }
        public void setDescription(String product_desc){ProductDesc.setText(product_desc);}
    }
}
