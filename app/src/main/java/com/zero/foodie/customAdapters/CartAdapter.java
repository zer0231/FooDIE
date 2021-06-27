package com.zero.foodie.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.foodie.MainActivity;
import com.zero.foodie.R;
import com.zero.foodie.model.CartModel;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private Context context;
    private ArrayList<CartModel> cartList;
    FirebaseDatabase database;
    DatabaseReference index;
    String userID = FirebaseAuth.getInstance().getUid();

    public CartAdapter(Context context, ArrayList<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_cart, parent, false);
        database = FirebaseDatabase.getInstance();
        index = database.getReference("Users").child(userID);
        return new CartAdapter.CartHolder(view);
    }

    //((MainActivity) v.getContext()).cartBadge();
    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        CartModel currentItem = cartList.get(position);
        holder.cartName.setText(currentItem.getPrductName());
        holder.cartPrice.setText(currentItem.getSubTotal());
        final String[] CurrentQuantity = {String.valueOf(currentItem.getQuantity())};
        holder.cartQuantity.setText(CurrentQuantity[0]);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(position);
                int currentQuantity = currentItem.getQuantity();
                currentQuantity = currentQuantity + 1;
                String subPrice = parseInt(currentItem.getPrice()) * currentQuantity + "";
                try {
                    index.child("cart").child(currentItem.getProductID()).child("quantity").setValue(currentQuantity);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                try {
                    index.child("cart").child(currentItem.getProductID()).child("subTotal").setValue(subPrice);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ((MainActivity) v.getContext()).cartBadge();
            }
        });

        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = currentItem.getQuantity();
                if (!(currentQuantity <= 1)) {
                    notifyItemChanged(position);
                    currentQuantity = currentQuantity - 1;
                    String subPrice = parseInt(currentItem.getPrice()) * currentQuantity + "";
                    try {
                        index.child("cart").child(currentItem.getProductID()).child("quantity").setValue(currentQuantity);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    try {
                        index.child("cart").child(currentItem.getProductID()).child("subTotal").setValue(subPrice);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity) v.getContext()).cartBadge();

                } else {
                    Toast.makeText(context, "To remove press remove button", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Glide.with(context).load(currentItem.getImage()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.temp_image).into(holder.cartImage);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        TextView cartName, cartPrice, cartQuantity;
        ImageView cartImage;
        ImageButton add, sub;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.addCartItem);
            sub = itemView.findViewById(R.id.subCartItem);
            cartName = itemView.findViewById(R.id.cartProductName);
            cartPrice = itemView.findViewById(R.id.cartProductPrice);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            cartImage = itemView.findViewById(R.id.cartProductImage);

        }
    }
}
