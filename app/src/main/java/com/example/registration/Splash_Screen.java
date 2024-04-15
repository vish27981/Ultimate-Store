package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;


public class Splash_Screen extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay for splash screen
        new Handler().postDelayed(() -> {
            // Start main activity after splash delay
            startActivity(new Intent(Splash_Screen.this,Login.class));
            finish(); // Close splash activity
        }, SPLASH_DELAY);
    }
    }
