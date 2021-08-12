package com.zero.foodie.customFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
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
    TextView errorTextView;
    FirebaseDatabase firebaseDatabase;
    Context context;
    ProductAdapter productAdapter;
    RecyclerView recyclerView, circularRecyclerView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CategoryAdapter categoryAdapter;
    String category;
    ProgressBar circularProgressIndicator;

    public HomeFragment() {
    }

    public HomeFragment(Context context, String category) {
        this.category = category;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home, parent, false);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        String token = task.getResult();
                        FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/notificationToken").setValue(token);
                    }
                });
            }
        });
        circularProgressIndicator = v.findViewById(R.id.progress_circular_home);
        circularRecyclerView = v.findViewById(R.id.categoryRecyclerView);
        circularRecyclerView.setHasFixedSize(true);
        recyclerView = v.findViewById(R.id.orgRecycleView);
        recyclerView.setHasFixedSize(true);
        errorTextView = v.findViewById(R.id.ErrorTxtView);
        recyclerView.setItemAnimator(null);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        LinearLayoutManager circularLinearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
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
                Toast.makeText(context, error.getMessage().trim(), Toast.LENGTH_SHORT).show();

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
                    try {
                        getData();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        productAdapter.getFilter().filter(newText);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                // productAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void getData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("products");
        if (category.equals("all")) {
            ref.orderByChild("status").equalTo("available").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ProductDetail> productList = new ArrayList<ProductDetail>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productList.add(new ProductDetail(dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("imageLink").getValue(String.class), dataSnapshot.child("itemType").getValue(String.class), dataSnapshot.child("briefDescription").getValue(String.class), dataSnapshot.child("price").getValue(String.class), dataSnapshot.getKey()));
                    }
                    productAdapter = new ProductAdapter(context, productList);
                    recyclerView.setAdapter(productAdapter);
                    circularProgressIndicator.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, error.getMessage().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            editor.putString("categoryHome", "homeSelected");
        } else {

            ref.orderByChild("status").equalTo("available").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ArrayList<ProductDetail> productList = new ArrayList<ProductDetail>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                      if (dataSnapshot.child("itemType").getValue(String.class).equals(category)) {
                             productList.add(new ProductDetail(dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("imageLink").getValue(String.class), dataSnapshot.child("itemType").getValue(String.class), dataSnapshot.child("briefDescription").getValue(String.class), dataSnapshot.child("price").getValue(String.class), dataSnapshot.getKey()));
                        }//productList.clear();
                    }
                    if(productList.isEmpty())
                    {
                        errorTextView.setText("Sorry this category is empty");
                    }
                    else{
                        circularProgressIndicator.setVisibility(View.GONE);
                        errorTextView.setVisibility(View.GONE);
                    }
                    productAdapter = new ProductAdapter(context, productList);
                    recyclerView.setAdapter(productAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            editor.putString("categoryHome", "categorySelected");
        }
        editor.apply();
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
