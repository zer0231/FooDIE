package com.zero.foodie.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.foodie.R;
import com.zero.foodie.customFragments.HomeFragment;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    private ArrayList<String> images;
    private ArrayList<String> catName;
    private FragmentManager fm;

   public CategoryAdapter(Context context, ArrayList<String> images,ArrayList<String> catName,FragmentManager fm) {
        this.images = images;
        this.catName = catName;
        this.fm = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circular_categoryimage, parent, false);
        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Glide.with(context).load(images.get(position)).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.sponge).into(holder.images);
        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment(context,catName.get(position));
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
            }
        });
     }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        CircleImageView images;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.circularCategory);
        }
    }
}
