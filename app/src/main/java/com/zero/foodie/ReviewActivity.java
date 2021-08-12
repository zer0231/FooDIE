package com.zero.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewActivity extends AppCompatActivity {
    FirebaseDatabase ref = FirebaseDatabase.getInstance();
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    LinearLayout reviewLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        reviewLinearLayout = findViewById(R.id.reviewLinearLayout);
        getData();
    }

    private void getData() {
        ref.getReference("Users/" + userID + "/myReviews/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String rating = dataSnapshot.child("rating").getValue(String.class);
                        String review = dataSnapshot.child("review").getValue(String.class);
                        String foodName = dataSnapshot.child("foodName").getValue(String.class);
                        TextView fullReview = new TextView(ReviewActivity.this);
                        String reviewFormat = foodName + "\t" + rating + "\n" + review + "\n\n";
                        fullReview.setText(reviewFormat);
//                        fullReview.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        fullReview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        reviewLinearLayout.addView(fullReview);
                    }
                } else {
                    Toast.makeText(ReviewActivity.this, "Its Empty !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
