package com.example.registration.model;

public class CartModel {

    private String reg_id,product_id,product_name,product_image;
    private int product_price,quantity,grandTotal;


    public CartModel(String productId, String productName, String productImage, int price,int quantity) {
        this.product_id = productId;
        this.product_name = productName;
        this.product_image = productImage;
        this.product_price = price;
        this.quantity = quantity;

    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_image() {
        return product_image;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }
}
