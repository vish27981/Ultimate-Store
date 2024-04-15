package com.example.registration.adapter;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registration.OrderDetails;
import com.example.registration.ViewProduct;
import com.example.registration.model.OrderModel;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.example.registration.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    List<OrderModel> orderList;
    Context context;
    TextView tv_name, tv_price, tv_date;
    CardView cardOrder;
    ImageView iv;
    String UserId,product_id;
    public OrderAdapter(List<OrderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
        User user = SharedPrefManager.getInstance(context).getUser();
        UserId = String.valueOf(user.getReg_id());

    }


    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {

            view = LayoutInflater.from(context).inflate(R.layout.layout_my_orders, viewGroup, false);
        }
        tv_name = view.findViewById(R.id.txt_product_name);
        tv_price = view.findViewById(R.id.txt_product_price);
        tv_date = view.findViewById(R.id.txt_date);
        iv = view.findViewById(R.id.img_product);
        cardOrder = view.findViewById(R.id.cardOrder);

        //tv_name.setText(orderList.get(i).getProduct_name());
        tv_price.setText("Total: Rs." + orderList.get(i).getProduct_price());
        tv_date.setText(orderList.get(i).getDate());
       // Picasso.get().load(URLs.PRODUCT_IMG_URL + orderList.get(i).getProduct_image()).into(iv);
        cardOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetails.class);
                //intent.putExtra("product_id",orderList.get(i).getProduct_id());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
