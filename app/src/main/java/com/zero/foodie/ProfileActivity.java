package com.zero.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
CircleImageView profileImage;
TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profileImage);
        txt = findViewById(R.id.temp);
        Button logoutBtn = findViewById(R.id.profileLogoutBtn);
        EditText profileName = findViewById(R.id.profileName);


profileName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(ProfileActivity.this, "Long press to edit", Toast.LENGTH_SHORT).show();
    }
});

        FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 String userName = snapshot.child("userName").getValue(String.class);
               profileName.setText(userName);
                String userProfileImage = snapshot.child("userProfilePicture").getValue(String.class);
                Glide.with(ProfileActivity.this).load(userProfileImage).placeholder(R.drawable.sponge).into(profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                profileName.setFocusableInTouchMode(true);
                profileName.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);
                return true;
            }
        });

        FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(ProfileActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this, SplashScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
