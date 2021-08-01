package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zero.foodie.customFragments.CartFragment;
import com.zero.foodie.customFragments.FavouriteFragment;
import com.zero.foodie.customFragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private boolean loggedIn;
    BottomNavigationView bottomNavigationView;

    private FirebaseDatabase firebaseDatabase;
    BadgeDrawable badgeDrawableCart;
    private DatabaseReference databaseReference;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

//        setting objects and other variables


        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        loggedIn = false;
        // String imageUrl = "https://e7.pngegg.com/pngimages/246/366/png-clipart-computer-icons-avatar-user-profile-man-avatars-logo-monochrome.png";
;
        //Setting HomeFragment as default
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(MainActivity.this, "all")).commit();


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Toast.makeText(MainActivity.this, "Closing", Toast.LENGTH_SHORT).show();
            }
        };

        drawer.addDrawerListener(toggle);//adding 3 line icon in toolbar for opening navigation drawer
        toggle.syncState();
        cartBadge();


        try {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Toast.makeText(MainActivity.this, "Home is selected", Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(MainActivity.this, "all")).commit();
                            break;
                        case R.id.nav_profile:
                            profileShow();
                            break;
                        case R.id.nav_Recipe:
                            Toast.makeText(MainActivity.this, "Recipe is selected", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, RecipeActivity.class));
                            break;
                        case R.id.nav_share:
//                            registrationToken();
                            startActivity(new Intent(MainActivity.this,FoodActivity.class));
                          //  Toast.makeText(MainActivity.this, "Thankyou mf", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            ;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomFood:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(MainActivity.this, "all")).commit();
                        break;

                    case R.id.bottomCart:

                        try {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment(MainActivity.this)).commit();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.bottomFav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavouriteFragment(MainActivity.this)).commit();
                        break;

                    case R.id.bottomProfile:
                        profileShow();
                        break;
                }
                return true;
            }
        });


        // Glide.with(this).load(imageUrl).fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profileButton);


    }

    private void profileShow() {
        Intent i;
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            i = new Intent(MainActivity.this, LoginActivity.class);
        } else {
            i = new Intent(MainActivity.this, ProfileActivity.class);
        }
        startActivity(i);
    }

    private void registrationToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //check if navigation drawer is open or not and if yes then close
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return true;
    }

    public void cartBadge() {
        final int[] cartItem = {0};
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference();
        cartReference.child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/cart/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItem[0] = (int) snapshot.getChildrenCount();
                badgeDrawableCart = bottomNavigationView.getOrCreateBadge(R.id.bottomCart);
                badgeDrawableCart.setNumber(cartItem[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}