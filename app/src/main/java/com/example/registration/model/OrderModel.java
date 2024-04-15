package com.example.registration.model;

public class OrderModel {

    private String product_name,product_image,date;
   private  int  product_price;

    public OrderModel(String product_name, String product_image,int product_price,String  date){
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
        this.date = date;
    }

    public OrderModel(int product_price,String  date){

        this.product_price = product_price;
        this.date = date;
    }



    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public int getProduct_price() {
        return product_price;
    }

    public String getDate() {
        return date;
    }
}
