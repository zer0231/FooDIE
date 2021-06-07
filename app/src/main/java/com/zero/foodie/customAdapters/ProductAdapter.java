package com.zero.foodie.customAdapters;

import android.content.Context;
import android.graphics.drawable.VectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero.foodie.MainActivity;
import com.zero.foodie.R;
import com.zero.foodie.model.CartModel;
import com.zero.foodie.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    private ArrayList<ProductDetail> productsList;
    private Context context;
    private ArrayList<ProductDetail> displayedList;

    public ProductAdapter(Context context, ArrayList<ProductDetail> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_product, parent, false);
        return new ProductHolder(view);
    }



    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ProductDetail> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ProductDetail item : productsList) {
                    if (item.getProName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productsList.clear();
            productsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        String userID = "ok";
        try {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String finalUserID = userID;
        ProductDetail currentItem = productsList.get(position);
        FirebaseDatabase.getInstance().getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/favourites/" + currentItem.getProId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    holder.addToFavourite.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite_24));
                    currentItem.setFavourite(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/cart/" + currentItem.getProId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    notifyItemChanged(position);
                    currentItem.setInCart(true);
                    holder.addToCart.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_done_24));
                } else {
                    notifyItemChanged(position);
                    currentItem.setInCart(false);
                    holder.addToCart.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_shopping_cart_24));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        Glide.with(context).load(currentItem.getProImageLink()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.sponge).into(holder.poster);
        holder.name.setText(currentItem.getProName());
        holder.price.setText(currentItem.getProPrice());
        holder.addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem.isFavourite()) {
                    FirebaseDatabase.getInstance().getReference().child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/favourites/" + currentItem.getProId()).removeValue();
                    holder.addToFavourite.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite_border_24));
                   notifyItemChanged(position);
                    currentItem.setFavourite(false);
                }
                else
                {
                    notifyItemChanged(position);
                    FirebaseDatabase.getInstance().getReference().child("Users/" + finalUserID + "/favourites/" + currentItem.getProId()).setValue("true");
                    holder.addToFavourite.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_favorite_24));
                    currentItem.setFavourite(true);
                }
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentItem.isInCart()) {
                    try {
                        FirebaseDatabase.getInstance().getReference().child("Users/" + finalUserID + "/cart/" + currentItem.getProId()).setValue(new CartModel(currentItem.getProId(), currentItem.getProName(), 1, currentItem.getProImageLink(), currentItem.getProPrice(), currentItem.getProPrice()));
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    currentItem.setInCart(true);
                    Toast.makeText(context, "Added from your cart" + currentItem.getProId(), Toast.LENGTH_SHORT).show();
                    holder.addToCart.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_done_24));
                    ((MainActivity) context).cartBadge();
                } else {
                    try {
                        FirebaseDatabase.getInstance().getReference().child("Users/" + finalUserID + "/cart/" + currentItem.getProId()).removeValue();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    currentItem.setInCart(false);
                    holder.addToCart.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_shopping_cart_24));
                    ((MainActivity) context).cartBadge();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        return Searched_Filter;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        //Here we initialize view
        public TextView name, price;
        public Button visit;
        public CardView cardView;
        public ImageButton addToCart, addToFavourite;
        public ImageView poster;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.productCardView);
            name = itemView.findViewById(R.id.proName);
            price = itemView.findViewById(R.id.proPice);
            poster = itemView.findViewById(R.id.proPoster);
            addToCart = itemView.findViewById(R.id.addToCart);
            addToFavourite = itemView.findViewById(R.id.proFavourite);
        }
    }
}
