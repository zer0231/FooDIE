package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zero.foodie.R;
import com.zero.foodie.customAdapters.OrganizationAdapter;
import com.zero.foodie.model.OrganizationDetail;
import com.zero.foodie.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Context context;
    OrganizationAdapter organizationAdapter;
    RecyclerView recyclerView;
    ArrayList<OrganizationDetail> organizationDetailList;


    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Defines the xml file for the fragment
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        recyclerView = v.findViewById(R.id.orgRecycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        getData();
//        registrationToken();
        return v;
    }


    private void getData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("orgUsers");
        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String data = "";
                    organizationDetailList = new ArrayList<OrganizationDetail>();
                    OrganizationDetail temp ;
                    List<ProductDetail> productList = new ArrayList<ProductDetail>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        temp = new OrganizationDetail(dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("address").getValue(String.class), dataSnapshot.child("phoneNumber").getValue(String.class), dataSnapshot.child("imageLink").getValue(String.class));
//                        productList.clear();
                        for (DataSnapshot ds : dataSnapshot.child("products").getChildren()) {
                            productList.clear();
                            productList.add(new ProductDetail(ds.child("name").getValue(String.class), ds.child("imageLink").getValue(String.class), ds.child("itemType").getValue(String.class), ds.child("briefDescription").getValue(String.class), ds.child("price").getValue(String.class)));

                        }
                        temp.setProducts(productList);


                      //  Toast.makeText(context, productList.size(), Toast.LENGTH_SHORT).show();

                        organizationDetailList.add(temp);
                    }

                    organizationAdapter = new OrganizationAdapter(context, organizationDetailList);
                    recyclerView.setAdapter(organizationAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void registrationToken() {
        // final String[] token = new String[1];
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                    }
                });

    }
}
