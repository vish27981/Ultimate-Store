package com.example.registration.model;

public class URLs {

    //ipaddress
    private static final String BASE = "http://192.168.0.110/";
    //localhost address
    private static final String URL_ROOT = BASE + "Ultimate_Store__/api/api/Api.php?apicall=";
    //user registration
    public static final String URL_SIGN_UP = URL_ROOT + "registration";
    //user login
    public static final String URL_LOGIN = URL_ROOT + "login";
    //category
    public  static  final String URL_CATEGORY = URL_ROOT + "category";
    //category image
    public static final String CAT_IMG_URL = BASE + "Ultimate_Store__/";
    //subcategory
    public static final String SUB_CATEGORY = URL_ROOT + "sub_category&cat_id=";
    //subcategory image
    public static final String SUBCAT_IMG_URL = BASE + "Ultimate_Store__/";
    //product image
    public static final String PRODUCT_IMG_URL = BASE + "Ultimate_Store__/";
    //product
    public static final String URL_PRODUCTS = URL_ROOT + "products&subcat_id=";
    //insert cart
    public static final String URL_CART = URL_ROOT + "cart";
    //cart_count
    public static final String URL_CART_COUNT = URL_ROOT + "count";
    //cart data
    public static final String URL_CART_DATA = URL_ROOT + "cartdata&reg_id=";
    //update cart
    public static final String URL_UPDATE_CART = URL_ROOT + "updatecart&quantity=";
    //delete cart
    public static final String URL_DELETE_CART =   URL_ROOT + "deletedata&product_id=";
    //place order
    public static final String URL_PLACE_ORDER = URL_ROOT + "place_order";
    //profile update
    public static final String URL_PROFILE_UPDATE = URL_ROOT + "profile";
    //getting profile details after update
    public static final String URL_GET_PROFILE = URL_ROOT + "get_profile";
    //order details
    public static final String URL_ORDER_DETAILS = URL_ROOT + "my_order_details&reg_id=";
    //feedback
    public static final String URL_FEEDBACK = URL_ROOT + "feedback";
    //change password
    public static final String URL_CHANGE_PASSWORD = URL_ROOT + "password";
    //insert wishlist
    public static final String URL_WISHLIST = URL_ROOT + "wishlist";
    //get wishlist
    public static final String URL_GET_WISHLIST  = URL_ROOT + "get_wishlist&reg_id=";
    //view Product
    public static final String URL_VIEW_PRODUCT = URL_ROOT + "viewproduct&product_id=";
    //forget Password
    public static final String URL_FORGET_PASSWORD = URL_ROOT + "ForgetPassword";

    public static final String URL_ORDER_DETAILS1 = URL_ROOT + "orderDetails&reg_id=";
}