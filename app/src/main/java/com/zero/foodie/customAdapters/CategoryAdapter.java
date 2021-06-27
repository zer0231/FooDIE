package com.zero.foodie.customAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zero.foodie.R;
import com.zero.foodie.customFragments.HomeFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context context;
    private ArrayList<String> images;
    private ArrayList<String> catName;
    private FragmentManager fm;
    private Handler handler;
    SharedPreferences preferences;
    String selectedCategory, homeSelected;

    public CategoryAdapter(Context context, ArrayList<String> images, ArrayList<String> catName, FragmentManager fm) {
        this.images = images;
        this.catName = catName;
        this.fm = fm;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.circular_categoryimage, parent, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        homeSelected = preferences.getString("categoryHome", "notSelected");
        selectedCategory = preferences.getString("category", "all");
        handler = new Handler();

        return new CategoryAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Glide.with(context).load(images.get(position)).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.temp_image).into(holder.images);

        if (catName.get(position) == selectedCategory && homeSelected == "categorySelected") {
            categorySelected(holder.images, true);
        } else {
            categorySelected(holder.images, false);
        }
        if (homeSelected == "homeSelected") {
            categorySelected(holder.images, false);
        }

        holder.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("category", catName.get(position));
                editor.apply();
                categorySelected(holder.images, true);
                Fragment fragment = new HomeFragment(context, catName.get(position));
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.commit();
            }
        });
    }

    private void categorySelected(CircleImageView images, boolean b) {
        if (b) {
            images.setBorderColor(context.getResources().getColor(R.color.pastelGreen));
        } else {
            images.setBorderColor(context.getResources().getColor(R.color.red));
        }
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
