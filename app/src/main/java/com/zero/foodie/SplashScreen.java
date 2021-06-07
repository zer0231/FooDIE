package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends AppCompatActivity {
    private int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView logoImage = findViewById(R.id.logoImage);
        timer = 2000;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Glide.with(this).load(R.drawable.chewing).placeholder(R.drawable.logo).into(logoImage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    i = new Intent(SplashScreen.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                } else {
                    i = new Intent(SplashScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                SplashScreen.this.startActivity(i);
            }
        }, timer);
    }
}