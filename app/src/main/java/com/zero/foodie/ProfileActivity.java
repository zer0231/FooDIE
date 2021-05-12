package com.zero.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button logoutBtn = findViewById(R.id.profileLogoutBtn);
        TextView profileView = findViewById(R.id.profileTxt);
        profileView.setText(new SharedPreferenceHandler(ProfileActivity.this).getUser());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPreferenceHandler(ProfileActivity.this).destroyUser();
                Toast.makeText(ProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

    }


}