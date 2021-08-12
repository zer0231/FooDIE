package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileActivity extends AppCompatActivity {
    Button backButton, history_close;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.back_to_main_menu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void openReview(View view) {
        startActivity(new Intent(ProfileActivity.this, ReviewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void openInfo(View view) {
        startActivity(new Intent(ProfileActivity.this,PendingOrderActivity.class));
        FancyToast.makeText(ProfileActivity.this, "Cannot open at the moment", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, false).show();

    }

    public void openHistory(View view) {
        //   getData();

        startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));

    }


    public void logout(View view) {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        finish();
    }
}
