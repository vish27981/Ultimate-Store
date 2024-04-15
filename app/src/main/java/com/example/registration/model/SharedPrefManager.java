package com.example.registration.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.registration.Login;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleylogin";
    private static final String KEY_USERNAME = "keyusername";
    private  static final String KEY_MOBILE = "keymobile";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context){
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getReg_id());
        editor.putString(KEY_USERNAME, user.getReg_name());
        editor.putString(KEY_MOBILE, user.getReg_mobile());
        editor.putString(KEY_EMAIL, user.getReg_email());
        editor.putString(KEY_PASSWORD, user.getReg_password());
        editor.apply();
    }
     public boolean isLoggedIn(){
         SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
         return sharedPreferences.getString(KEY_USERNAME, null) != null;
     }
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PASSWORD, null)
        );
    }

    public void saveUser(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getReg_id());
        editor.putString(KEY_USERNAME, user.getReg_name());
        editor.putString(KEY_MOBILE, user.getReg_mobile());
        editor.putString(KEY_EMAIL, user.getReg_email());
        editor.apply();
    }

    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(ctx, "You have been Logout", Toast.LENGTH_SHORT).show();
        ctx.startActivity(new Intent(ctx, Login.class));
    }
}
