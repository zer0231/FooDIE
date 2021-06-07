package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.zero.foodie.customAdapters.CategoryAdapter;
import com.zero.foodie.customAdapters.ProductAdapter;
import com.zero.foodie.model.ProductDetail;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Context context;
    ProductAdapter productAdapter;
    RecyclerView recyclerView, circularRecyclerView;
    ArrayList<ProductDetail> productList;
    SearchView searchView;
    CategoryAdapter categoryAdapter;
    String category;

    public HomeFragment(Context context, String category) {
        this.category = category;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        circularRecyclerView = v.findViewById(R.id.categoryRecyclerView);
        circularRecyclerView.setHasFixedSize(true);
        recyclerView = v.findViewById(R.id.orgRecycleView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager circularLinearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        circularRecyclerView.setLayoutManager(circularLinearLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        getCategoryData();
        getData();
//        registrationToken();
        return v;
    }


    public void getCategoryData() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                 dataSnapshot.child("name").getValue(String.class)
                    names.add(dataSnapshot.getKey());

                    try {
                        images.add(dataSnapshot.getValue().toString());
                    } catch (Exception e) {
                        images.add("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.373cI-NISN44rQmiYFt4gwHaHa%26pid%3DApi&f=1");
                    }

                }
                categoryAdapter = new CategoryAdapter(context, images, names, getFragmentManager());
                circularRecyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    getData();
                } else {
                    productAdapter.getFilter().filter(newText);
                }

                // productAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    private void getData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");

        if (category == "all") {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String data = "";

                    ArrayList<ProductDetail> productList = new ArrayList<ProductDetail>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productList.add(new ProductDetail(dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("imageLink").getValue(String.class), dataSnapshot.child("itemType").getValue(String.class), dataSnapshot.child("briefDescription").getValue(String.class), dataSnapshot.child("price").getValue(String.class), dataSnapshot.getKey()));
                        //productList.clear();
                    }

                    productAdapter = new ProductAdapter(context, productList);
                    recyclerView.setAdapter(productAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(context, "Not all", Toast.LENGTH_SHORT).show();
            ref.orderByChild("itemType").equalTo(category).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String data = "";

                    ArrayList<ProductDetail> productList = new ArrayList<ProductDetail>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productList.add(new ProductDetail(dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("imageLink").getValue(String.class), dataSnapshot.child("itemType").getValue(String.class), dataSnapshot.child("briefDescription").getValue(String.class), dataSnapshot.child("price").getValue(String.class), dataSnapshot.getKey()));
                        //productList.clear();
                    }

                    productAdapter = new ProductAdapter(context, productList);
                    recyclerView.setAdapter(productAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private String registrationToken() {
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        token[0] = task.getResult();
                    }
                });

        return token[0];

    }
}
