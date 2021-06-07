package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.MainActivity;
import com.zero.foodie.R;
import com.zero.foodie.customAdapters.CartAdapter;
import com.zero.foodie.customAdapters.ProductAdapter;
import com.zero.foodie.model.CartModel;
import com.zero.foodie.model.ProductDetail;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class CartFragment extends Fragment {
    private Context context;
    ArrayList<CartModel> cartItem;
    RecyclerView recyclerView;
    TextView totalTxt;
    Button checkOut;
    CartAdapter cartAdapter;
    int Total = 0;

    public CartFragment(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_cart, parent, false);
        String uniqueID = String.valueOf(java.time.LocalDate.now());

        uniqueID = uniqueID.replace("-","");
        uniqueID = uniqueID+""+java.time.LocalTime.now();
        uniqueID = uniqueID.replace(":","");
        uniqueID = uniqueID.replace(".","");
        totalTxt = v.findViewById(R.id.totalTextView);
        recyclerView = v.findViewById(R.id.cartRecyclerViewer);
        recyclerView.setHasFixedSize(true);
        checkOut = v.findViewById(R.id.checkoutButton);
        cartItem = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        String finalUniqueID = uniqueID;
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/transaction/"+ finalUniqueID).setValue(cartItem);
                FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/cart").removeValue();

            }
        });
        getData();
        return v;
    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItem.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartModel temp = dataSnapshot.getValue(CartModel.class);
                    Total = Total + parseInt(String.valueOf(temp.getPrice())) * parseInt(String.valueOf(temp.getQuantity()));
                    totalTxt.setText("Total: " + String.valueOf(Total));
                    cartItem.add(temp);
                }
                cartAdapter = new CartAdapter(context, cartItem);
                recyclerView.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

