package com.zero.foodie.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zero.foodie.R;
import com.zero.foodie.model.RecipeBrief;

import java.util.ArrayList;

public class RecipePagerAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<RecipeBrief> recipes;

    @Override
    public int getCount() {
        return recipes.size();
    }

    public RecipePagerAdapter(Context context, ArrayList<RecipeBrief> recipes)
    {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.recipe_cardview, container, false);
        ImageView recipeImage;
        FloatingActionButton floatingActionButton;
        TextView recipeTitle,recipeBriefDescription;
        recipeImage = view.findViewById(R.id.recipe_image);
        floatingActionButton = view.findViewById(R.id.viewRecipe);
        recipeTitle = view.findViewById(R.id.recipe_title);
        recipeBriefDescription = view.findViewById(R.id.recipe_briefDescription);

        Glide.with(context).load(recipes.get(position).getImageURL()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.sponge).into(recipeImage);
        recipeTitle.setText(recipes.get(position).getName());
        recipeBriefDescription.setText(recipes.get(position).getBriefIntro());
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(recipes.get(position).getLink())));
            }
        });
        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
