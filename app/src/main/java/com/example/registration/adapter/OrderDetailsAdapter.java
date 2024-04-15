package com.example.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registration.R;
import com.example.registration.model.OrderDetailsModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    List<OrderDetailsModel> orderDetails;
    Context context;
    LayoutInflater inflater;
    String UserId;


    public OrderDetailsAdapter(List<OrderDetailsModel> orderDetails,Context context){
        this.orderDetails = orderDetails;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        User user = SharedPrefManager.getInstance(context).getUser();
        UserId = String.valueOf(user.getReg_id());
    }


    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_order_details,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        holder.setProductId(orderDetails.get(position).getProduct_id());
        holder.setProductName(orderDetails.get(position).getProduct_name());
        holder.setProductQuantity(orderDetails.get(position).getQuantity());
        holder.setShip_add(orderDetails.get(position).getShip_add());
        holder.setTotal_bill(orderDetails.get(position).getTotal_bill());
        Picasso.get().load(URLs.PRODUCT_IMG_URL + orderDetails.get(position).getProduct_image()).into(holder.product_image);


    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView product_id,product_name,product_quantity,ship_add,total_bill;
        ImageView product_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_id = itemView.findViewById(R.id.product_id);
            product_name = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.quantity);
            ship_add = itemView.findViewById(R.id.ship_add);
            total_bill = itemView.findViewById(R.id.total_bill);
            product_image = itemView.findViewById(R.id.pimg);
        }

        public void setProductId(String productId){
            product_id.setText(productId);
        }
        public void setProductName(String productName){
            product_name.setText(productName);
        }
        public void setProductQuantity(int productQuantity) {
            product_quantity.setText(String.valueOf(productQuantity));
        }

        public  void setShip_add(String shipAdd){ship_add.setText(shipAdd);}
        public void setTotal_bill(int totalBill){total_bill.setText(String.valueOf(totalBill));}
    }
}
