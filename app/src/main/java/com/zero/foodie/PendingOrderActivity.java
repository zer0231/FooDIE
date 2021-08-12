package com.zero.foodie;

import android.os.Bundle;
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
import com.zero.foodie.model.CartModel;
import com.zero.foodie.model.OrderHistoryModel;
import com.zero.foodie.model.UserDetail;

import java.util.ArrayList;

public class PendingOrderActivity extends AppCompatActivity {
    FirebaseDatabase ref;
    LinearLayout currentOrderLinearLayout;
    final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingorder);
        currentOrderLinearLayout = findViewById(R.id.currentOrderLinearLayout);
        ref = FirebaseDatabase.getInstance();

        ref.getReference("Users/" + userID + "/Waiting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String toBeStored;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TextView simple_detail = new TextView(PendingOrderActivity.this);
                        simple_detail.setBackgroundColor(getResources().getColor(R.color.darkGreen));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        simple_detail.setPadding(5, 20, 5, 20);
                        params.setMargins(5, 50, 5, 50);
                        simple_detail.setLayoutParams(params);

                        String id = dataSnapshot.getKey();
                        toBeStored = id + "\n";

                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            String foodName = children.child("prductName").getValue(String.class);
                            Float quantity = children.child("quantity").getValue(Float.class);
                            String price = children.child("price").getValue(String.class);
                            String subTotal = children.child("subTotal").getValue(String.class);

                            toBeStored += foodName + "\t" + quantity + "\t" + price + "\t" + subTotal + "\n";
                        }
                        simple_detail.setText(toBeStored);
                        currentOrderLinearLayout.addView(simple_detail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
