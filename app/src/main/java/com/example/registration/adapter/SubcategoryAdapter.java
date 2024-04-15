package com.example.registration.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.SubCategoryModel;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.Product;
import com.example.registration.R;
import com.squareup.picasso.Picasso;


import java.util.List;


public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {
    private Context context;
    LayoutInflater inflater;
    private List<SubCategoryModel> subCategoryList;
    SharedPreferences preferences;
    String UserId;


   public SubcategoryAdapter(List<SubCategoryModel> subCategoryList, Context context){
        this.subCategoryList = subCategoryList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
       User user = SharedPrefManager.getInstance(context).getUser();
       UserId = String.valueOf(user.getReg_id());

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cat_id, sub_cat_id, sub_cat_name;
        ImageView sub_cat_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_id = itemView.findViewById(R.id.cat_id);
            sub_cat_id = itemView.findViewById(R.id.subcatId);
            sub_cat_name = itemView.findViewById(R.id.subcategoryName);
            sub_cat_image = itemView.findViewById(R.id.subcategoryImage);
        }

        public void setCategoryId(String catId){
            cat_id.setText(catId);
        }
        public void setSubCategoryId(String subcatId){
            sub_cat_id.setText(subcatId);
        }

        public void setCategoryName(String subcategoryName) {
            sub_cat_name.setText(subcategoryName);
            Log.d(subcategoryName, "setCategoryName");
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.subcategory_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setCategoryId(subCategoryList.get(position).getCatId());
        holder.setSubCategoryId(subCategoryList.get(position).getSubcatId());
        holder.setCategoryName(subCategoryList.get(position).getSubcategoryName());
        Picasso.get().load(URLs.SUBCAT_IMG_URL + subCategoryList.get(position).getSubcategoryImage()).placeholder(R.drawable.profile).error(R.drawable.aboutus).into(holder.sub_cat_image);
        //Log.d("SUB CATEGORY IMG  ===", URLs.SUBCAT_IMG_URL +subCategoryList.get(position).getSubcategoryName());
        holder.sub_cat_image.setOnClickListener(view -> {
            Intent intent = new Intent(context, Product.class);
            intent.putExtra("cat_id",subCategoryList.get(position).getCatId());
            intent.putExtra("subcat_id",subCategoryList.get(position).getSubcatId());
            intent.putExtra("cat_name",subCategoryList.get(position).getSubcategoryName());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }


}
