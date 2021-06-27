package com.zero.foodie.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
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
        MaterialCardView cardView;
        FloatingActionButton floatingActionButton;
        TextView recipeTitle,recipeBriefDescription;
        recipeImage = view.findViewById(R.id.recipe_image);
        floatingActionButton = view.findViewById(R.id.viewRecipe);
        recipeTitle = view.findViewById(R.id.recipe_title);
        recipeBriefDescription = view.findViewById(R.id.recipe_briefDescription);
        cardView = view.findViewById(R.id.recipe_cardView);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Long Press to view full recipe", Toast.LENGTH_SHORT).show();
            }
        });


        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(recipes.get(position).getLink())));
                return true;
            }
        });

        Glide.with(context).load(recipes.get(position).getImageURL()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.temp_image).into(recipeImage);
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
