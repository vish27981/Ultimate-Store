package com.example.registration.model;

public class WishListModel {

    private String reg_id,product_id,product_name,product_image;
    private int product_price;

    public WishListModel(String productId, String productName, String productImage, int price) {

        this.product_id = productId;
        this.product_name = productName;
        this.product_price = price;
        this.product_image = productImage;
    }

    public String getReg_id() {
        return reg_id;
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

    public String getProduct_price() {
        return String.valueOf(product_price);
    }
}
