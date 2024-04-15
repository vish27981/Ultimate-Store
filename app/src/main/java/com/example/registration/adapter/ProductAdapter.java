package com.example.registration.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registration.model.ProductModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.R;
import com.example.registration.ViewProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductModel> productList;
    private Context context;
    LayoutInflater inflater;

    String UserId;


    public ProductAdapter(List<ProductModel> productList,Context context) {
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        User user = SharedPrefManager.getInstance(context).getUser();
        UserId = String.valueOf(user.getReg_id());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ProductId;
        private final  TextView SubCategoryId ;
        private final TextView ProductName ;
        private final TextView ProductPrice,ProductDesc;
        private final ImageView ProductImage, wishList;
        Button  ViewProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            ProductId= itemView.findViewById(R.id.product_id);
            SubCategoryId = itemView.findViewById(R.id.subcat_id);
            ProductName = itemView.findViewById(R.id.name);
            ProductPrice = itemView.findViewById(R.id.Price);
            ProductImage = itemView.findViewById(R.id.proimg);
            ProductDesc = itemView.findViewById(R.id.product_desc);
            wishList = itemView.findViewById(R.id.wishlist);

            ViewProduct = itemView.findViewById(R.id.viewProduct);
        }

        public void setSubCatId(String subcat_id){
            SubCategoryId.setText(subcat_id);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setSubCatId(productList.get(position).getSubCat_id());
        holder.setProductId(productList.get(position).getProduct_id());
        holder.setProductName(productList.get(position).getProduct_name());
        holder.setPrice("Rs." + productList.get(position).getPrice());
        holder.setDescription(productList.get(position).getProduct_desc());
        Picasso.get().load(URLs.PRODUCT_IMG_URL + productList.get(position).getProduct_image()).into(holder.ProductImage);
        //Log.d("Product Img",URLs.PRODUCT_IMG_URL + productList.get(position).getProduct_image());

        holder.ViewProduct.setOnClickListener(view -> {
            //getProduct(subcat_id);
            Intent intent = new Intent(context, ViewProduct.class);
            intent.putExtra("product_id",productList.get(position).getProduct_id());
            context.startActivity(intent);
        });
    }




    @Override
    public int getItemCount() {
        return productList.size();
    }
}
