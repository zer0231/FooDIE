package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.zero.foodie.customFragments.HomeFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button logoutBtn = findViewById(R.id.profileLogoutBtn);
        TextView profileView = findViewById(R.id.profileTxt);
         //getSupportFragmentManager().beginTransaction().replace(R.id.homeFrame,new HomeFragment(this)).commit();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(ProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
}
