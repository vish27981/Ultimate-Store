package com.example.registration.model;

public class OrderDetailsModel {

    String product_id,product_name,product_image,ship_add,payment_mode;

    int quantity,total_bill;

    public OrderDetailsModel(String product_name,String product_image,
                             String ship_add, String payment_mode,int quantity,int total_bill){

        this.product_name = product_name;
        this.product_image = product_image;
        this.ship_add = ship_add;
        this.payment_mode = payment_mode;
        this.quantity = quantity;
        this.total_bill = total_bill;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getShip_add() {
        return ship_add;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal_bill() {
        return total_bill;
    }
}
