package com.example.registration.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.registration.model.CategoryModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.R;
import com.example.registration.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;



public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private Context context;
    LayoutInflater inflater;
    private  List<CategoryModel> categoryList;
    String UserId;


    public CategoryAdapter( List<CategoryModel> categoryList,Context context) {
        this.categoryList = categoryList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        User user = SharedPrefManager.getInstance(context).getUser();
        UserId = String.valueOf(user.getReg_id());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView categoryId ;
        private TextView categoryName ;
        private ImageView categoryImage,banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryId= itemView.findViewById(R.id.cat_id);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            banner = itemView.findViewById(R.id.banner);
        }

        public void setCategoryId(String catId){
            categoryId.setText(catId);
        }

        public void setCategoryName(String catName) {
            categoryName.setText(catName);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setCategoryId(categoryList.get(position).getCatId());
        holder.setCategoryName(categoryList.get(position).getCatName());

        Picasso.get().load(URLs.CAT_IMG_URL + categoryList.get(position).getCatImage()).placeholder(R.drawable.profile).error(R.drawable.aboutus).into(holder.categoryImage);
        //Log.d("CATEGORY IMG ===== ", URLs.CAT_IMG_URL + categoryList.get(position).getCatImage());

        holder.categoryImage.setOnClickListener(view -> {
            Intent intent = new Intent(context, SubCategory.class);
            intent.putExtra("cat_id", categoryList.get(position).getCatId());
            intent.putExtra("cat_name", categoryList.get(position).getCatName());
            context.startActivity(intent);
        });
        Picasso.get().load(R.drawable.banner2).into(holder.banner);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

