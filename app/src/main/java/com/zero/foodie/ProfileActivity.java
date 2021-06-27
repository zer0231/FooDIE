package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.customAdapters.HistoryAdapter;
import com.zero.foodie.model.OrderHistoryModel;
import com.zero.foodie.model.CartModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
      finish();
    }

    public void openReview(View view) {
        Toast.makeText(this, "Reviews", Toast.LENGTH_SHORT).show();
    }

    public void openInfo(View view) {
        Toast.makeText(this, "Information", Toast.LENGTH_SHORT).show();
    }

    public void openHistory(View view) {
        Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }
}
