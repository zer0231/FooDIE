package com.zero.foodie;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class FoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Button add_to_cart = findViewById(R.id.add_to_cart);
        Button rate = findViewById(R.id.rate_btn);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FoodActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Dialog dialogRate = new Dialog(FoodActivity.this);
                dialogRate.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialogRate.setContentView(R.layout.dialog_rate);
                dialogRate.setCancelable(false);
                RatingBar ratingBar = dialogRate.findViewById(R.id.ratingbar);

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);

                TextInputEditText ratingReview = dialogRate.findViewById(R.id.ratingReview);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialogRate.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;


                dialogRate.findViewById(R.id.custom_dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogRate.dismiss();
                    }
                });

                dialogRate.findViewById(R.id.custom_dialog_btn_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FoodActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                        FirebaseDatabase.getInstance().getReference().child("products/"+currentItem.getProId()+"/userRating/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/review").setValue(ratingReview.getText().toString());
//                        FirebaseDatabase.getInstance().getReference().child("products/"+currentItem.getProId()+"/userRating/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/rating").setValue(ratingBar.getRating()+"");
//                        FirebaseDatabase.getInstance().getReference().child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/myReviews/"+currentItem.getProId()+"/review").setValue(ratingReview.getText().toString());
//                        FirebaseDatabase.getInstance().getReference().child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/myReviews/"+currentItem.getProId()+"/rating").setValue(ratingBar.getRating()+"");
                        dialogRate.dismiss();
                    }
                });
                dialogRate.show();
                dialogRate.getWindow().setAttributes(layoutParams);

            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
