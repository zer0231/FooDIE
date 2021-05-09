package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profileButton = findViewById(R.id.profile_button);
        String imageUrl = "https://e7.pngegg.com/pngimages/246/366/png-clipart-computer-icons-avatar-user-profile-man-avatars-logo-monochrome.png";
        Glide.with(this).load(imageUrl).fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profileButton);
        loggedIn = true;

        profileButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
           if(loggedIn)
           {
               i = new Intent(MainActivity.this,ProfileActivity.class);
           }
           else
           {
              i = new Intent(MainActivity.this,LoginActivity.class);

           }

           startActivity(i);
            //  Toast.makeText(MainActivity.this, "This should open the profile or login page", Toast.LENGTH_SHORT).show();
        }
    });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
       if(loggedIn)
       {
           loggedIn = false;
           Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
       }
       else
       {
           loggedIn = true;
           Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
       }
    }
}