package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.registration.model.SharedPrefManager;
import com.example.registration.model.URLs;
import com.example.registration.model.User;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class payment extends AppCompatActivity implements PaymentResultListener{

    TextView tv_name,edt_email, tv_amount;
    EditText edt_address,  edt_mobile;
    RadioButton radioButton,radioButton1;
    Button checkOut;
    private String email,mobile,address,order_type, amount;
    private String userId;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());

        init();
        intent();
        sharedPref();
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void sharedPref() {

        User user = SharedPrefManager.getInstance(this).getUser();
        String name = user.getReg_name();
        String mobile = user.getReg_mobile();
        String email = user.getReg_email();
        userId = String.valueOf(user.getReg_id());


        tv_name.setText(name);
        edt_address.setText(address);
        edt_mobile.setText(mobile);
        edt_email.setText(email);

    }

    private void intent() {
        amount = getIntent().getStringExtra("total_bill");
        tv_amount.setText(amount);
    }

    private void init() {

        tv_name = findViewById(R.id.txt_name);
        tv_amount = findViewById(R.id.txt_amount);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_address = findViewById(R.id.edt_address);
        radioButton = findViewById(R.id.radio_cod);
        radioButton1 = findViewById(R.id.radio_card);
        checkOut = findViewById(R.id.checkOut);
        order_type="";
        radioButton.setOnClickListener(view -> {
            order_type = "COD";
        });
        radioButton1.setOnClickListener(view -> {
            order_type = "Online Payment";
          startPayment();
        });

        checkOut.setOnClickListener(view -> {
            Log.d("order type is ==:",order_type);
            final_order(tv_name.getText().toString(), tv_amount.getText().toString(),
                    edt_email.getText().toString(), edt_mobile.getText().toString(),
                    edt_address.getText().toString(),
                    order_type);
            return;
        });
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void final_order(final String name, final String amount, final String email, final String mobile,
                             final String address,final String order_type) {

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_PLACE_ORDER, response -> {
            try {
                Log.d("Payment Response===",response);
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(payment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, Orders.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(payment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(payment.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }, error -> Toast.makeText(payment.this, "ERROR", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reg_id", userId);
                params.put("reg_name", name);
                params.put("reg_email", email);
                params.put("reg_mobile", mobile);
                params.put("total_bill", amount);
                params.put("payment_mode", order_type);
                params.put("ship_add", address);

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }



    private void startPayment(){
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_eNwgA5oEnElHde");
        checkout.setImage(R.drawable.logo);

        final Activity activity = this;
        int totalBillAmountPaise = Integer.parseInt(amount) * 100; // Convert rupees to paisee
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Ultimate Store");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("theme.color", "#99b9ff");
            options.put("currency", "INR");
            options.put("amount",   String.valueOf(totalBillAmountPaise));//pass amount in currency subunits
            options.put("prefill.email",String.valueOf(email));
            options.put("prefill.contact",String.valueOf(mobile));
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        //Log.d("PAYMENT_SUCCESS", "Payment successful. Payment ID: " + s);
        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        //Log.d("ON ERROR", "onPaymentError: "+s);
        Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}