package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.DatabaseQueryHandler;
import com.zero.foodie.R;
import com.zero.foodie.customAdapters.CartAdapter;
import com.zero.foodie.customAdapters.CheckoutAdapter;
import com.zero.foodie.model.CartModel;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    private Context context;
    final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static ArrayList<CartModel> cartItem;
    RecyclerView recyclerView;
    Button checkOut;
    ScrollView scrollView;

    CartAdapter cartAdapter;
    TextView noItem,cartTotal;

    public CartFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_cart, parent, false);
        scrollView = v.findViewById(R.id.scrollView3);
        noItem = v.findViewById(R.id.cart_message);
        cartTotal = v.findViewById(R.id.cart_fragment_total);
        recyclerView = v.findViewById(R.id.cartRecyclerViewer);
        recyclerView.setHasFixedSize(true);
        checkOut = v.findViewById(R.id.checkoutButton);
        cartItem = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        getData();
        FirebaseDatabase.getInstance().getReference("Users/"+userID+"/cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren())
                {
                    int finalTotal = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String total = dataSnapshot.child("subTotal").getValue(String.class);
                        int intTotal=Integer.parseInt(total);
                        finalTotal = finalTotal + intTotal;

                    }
                    cartTotal.setText("Rs. "+finalTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CheckoutAdapter checkoutAdapter = new CheckoutAdapter();
                    Log.d("cartItem", cartItem.get(0).getImage());
                    checkoutAdapter.display(getFragmentManager());
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }


    public static void makeOrder(String finalUniqueID, String latitude, String longitude) {
        for (int i = 0; i < cartItem.size(); i++) {
            cartItem.get(i).setDeliveryLatitude(latitude);
            cartItem.get(i).setDeliveryLongitude(longitude);
        }

        FirebaseDatabase.getInstance().getReference("pendingOrder").child(finalUniqueID).setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/cart").removeValue();
                    new DatabaseQueryHandler().copy("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Waiting", "pendingOrder/" + finalUniqueID);

//                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/exactAddress").setValue(latitude+","+longitude);
                }
            }
        });

    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItem.clear();
                if (!snapshot.hasChildren()) {
                    noItem.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel temp = dataSnapshot.getValue(CartModel.class);
                    cartItem.add(temp);
                }
                cartAdapter = new CartAdapter(context, cartItem);
                recyclerView.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}

