package com.zero.foodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private SharedPreferenceHandler sharedPreferenceHandler;
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setting objects and other variables
        profileButton = findViewById(R.id.profile_button);
        toolbar = findViewById(R.id.toolbar);
        drawer =findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        sharedPreferenceHandler = new SharedPreferenceHandler(this);
        loggedIn= false;
       // String imageUrl = "https://e7.pngegg.com/pngimages/246/366/png-clipart-computer-icons-avatar-user-profile-man-avatars-logo-monochrome.png";
        Toast.makeText(this, sharedPreferenceHandler.getUser(), Toast.LENGTH_SHORT).show();


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };//adding 3 line icon in toolbar for opening navigation drawer

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "3 line", Toast.LENGTH_SHORT).show();
    }
});


        // Glide.with(this).load(imageUrl).fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profileButton);


        profileButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!sharedPreferenceHandler.getUser().equals(""))
            {
                loggedIn = true;
            }
            else
            {
                loggedIn = false;
            }
            Toast.makeText(MainActivity.this, new SharedPreferenceHandler(MainActivity.this).getUser(), Toast.LENGTH_SHORT).show();
           try {
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
           }catch (Exception ex)
           {
               Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
           }

        }
    });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "Profile is selected", Toast.LENGTH_SHORT).show();
                    break;

                    case R.id.nav_Recipe:
                        Toast.makeText(MainActivity.this, "Recipie is selected", Toast.LENGTH_SHORT).show();
                    break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        //check if navigation drawer is open or not and if yes then close
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

    }
}