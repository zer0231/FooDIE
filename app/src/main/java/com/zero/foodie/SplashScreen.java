package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {
    private int timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        timer = 2000;

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
           Intent i = new Intent(SplashScreen.this,MainActivity.class);
           SplashScreen.this.startActivity(i);
         }
     },timer);
    }
}