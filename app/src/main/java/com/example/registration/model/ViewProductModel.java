package com.example.registration.model;

public class ViewProductModel {

    private  String product_id, product_name,product_image, price,product_desc;

    public ViewProductModel(String product_id, String product_name, String product_image, String price, String product_desc){

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image  =   product_image;
        this.product_desc = product_desc;
        this.price = price;
    }



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }
}
