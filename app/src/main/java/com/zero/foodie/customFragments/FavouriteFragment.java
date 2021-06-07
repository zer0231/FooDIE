package com.zero.foodie.customFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.R;
import com.zero.foodie.customAdapters.CartAdapter;
import com.zero.foodie.customAdapters.FavouriteAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private final Context context;
    RecyclerView recyclerView;
    List<String> favouriteProductId;
    FavouriteAdapter favouriteAdapter;

    public FavouriteFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_favourite, parent, false);
        recyclerView = v.findViewById(R.id.favouriteRecyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        getData();
        return v;
    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favouriteProductId = new ArrayList<>();
//                favouriteProductId.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   favouriteProductId.add(dataSnapshot.getKey());
                }
                //favouriteProductId.clear();
                favouriteAdapter = new FavouriteAdapter(context, favouriteProductId);
                recyclerView.setAdapter(favouriteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
