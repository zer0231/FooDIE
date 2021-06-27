package com.zero.foodie.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.R;
import com.zero.foodie.model.CartModel;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {
    Context context;
    List<String> productId;
    CartModel temp;

    public FavouriteAdapter(Context context, List<String> productId) {
        this.context = context;
        this.productId = productId;
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_favourite, parent, false);
        return new FavouriteAdapter.FavouriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteHolder holder, int position) {
        String currentID = productId.get(position);
        FirebaseDatabase.getInstance().getReference("products/" + currentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temp = new CartModel(currentID, snapshot.child("name").getValue(String.class), 1, snapshot.child("imageLink").getValue(String.class), snapshot.child("price").getValue(String.class), snapshot.child("price").getValue(String.class),FirebaseAuth.getInstance().getCurrentUser().getUid());
                holder.name.setText(snapshot.child("name").getValue(String.class));
                holder.price.setText(snapshot.child("price").getValue(String.class));
                Glide.with(context).load(snapshot.child("imageLink").getValue(String.class)).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.temp_image).into(holder.poster);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.moveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/cart/" + currentID).setValue(temp);
                Toast.makeText(context, "Added an Instance", Toast.LENGTH_SHORT).show();
            }
        });
        holder.removeFromFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/favourites/" + currentID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        productId.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productId.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name, price;
        Button moveToCart, removeFromFavourite;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.favImage);
            name = itemView.findViewById(R.id.favName);
            price = itemView.findViewById(R.id.favPrice);
            moveToCart = itemView.findViewById(R.id.moveToCart);
            removeFromFavourite = itemView.findViewById(R.id.removeFromFavourite);
        }
    }
}
