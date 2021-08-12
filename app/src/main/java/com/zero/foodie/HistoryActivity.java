package com.zero.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.customAdapters.HistoryAdapter;
import com.zero.foodie.model.CartModel;
import com.zero.foodie.model.OrderHistoryModel;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ArrayList<OrderHistoryModel> orderHistory;
    RecyclerView historyRecyclerView;
    HistoryAdapter historyAdapter;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        historyRecyclerView.setLayoutManager(linearLayoutManager);
        getData();

        close = findViewById(R.id.close_btn_history);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void getData() {

        FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("completedOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    orderHistory = new ArrayList<OrderHistoryModel>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        ArrayList<String> names = new ArrayList<>();
                        ArrayList<String> quantity = new ArrayList<>();
                        ArrayList<String> price = new ArrayList<>();
                        OrderHistoryModel orderHistoryModel = new OrderHistoryModel();

                        String DateAndTime = dataSnapshot.getKey();
                        orderHistoryModel.setDate(DateAndTime);
                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                            CartModel temp = children.getValue(CartModel.class);
                            names.add(temp.getPrductName());
                            orderHistoryModel.setProductList(names);
                             price.add(temp.getPrice());
                            orderHistoryModel.setPrice(price);

                            quantity.add(String.valueOf(temp.getQuantity()));
                            orderHistoryModel.setQuantity(quantity);
                        }
                        orderHistory.add(orderHistoryModel);
                    }
                    historyAdapter = new HistoryAdapter(HistoryActivity.this, orderHistory);
                    historyRecyclerView.setAdapter(historyAdapter);
                } else {
                    Toast.makeText(HistoryActivity.this, "Its Empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
