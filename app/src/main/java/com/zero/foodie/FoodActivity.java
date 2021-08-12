package com.zero.foodie;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zero.foodie.model.CartModel;
import com.zero.foodie.model.ProductDetail;
import com.zero.foodie.model.UserDetail;

public class FoodActivity extends AppCompatActivity {
    ImageView foodImage;
    TextView foodTitle, foodPrice,foodDesc,userReview;
    FirebaseDatabase ref;
    String foodID, userID,allUserRate;
    UserDetail userDetail;
    ProductDetail currentProduct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Button add_to_cart = findViewById(R.id.add_to_cart);
        ref = FirebaseDatabase.getInstance();
        foodID = getIntent().getStringExtra("foodID");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReview = findViewById(R.id.user_review);
        foodImage = findViewById(R.id.food_image);
        foodTitle = findViewById(R.id.food_title);
        foodPrice = findViewById(R.id.food_price);
        foodDesc = findViewById(R.id.food_desc);
        Button rate = findViewById(R.id.rate_btn);


        ref.getReference("Users/"+userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    userDetail =  snapshot.getValue(UserDetail.class);
               //     Toast.makeText(FoodActivity.this, userDetail.getUserName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.getReference().child("products/"+foodID+"/userRating/").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Toast.makeText(FoodActivity.this, "Review added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            allUserRate = "";
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      ref.getReference().child("products/"+foodID+"/userRating/").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.hasChildren())
              {
                  allUserRate = "";
                  for (DataSnapshot dataSnapshot : snapshot.getChildren())
                  {
                      if(dataSnapshot.exists())
                      {
                          String rating =  dataSnapshot.child("rating").getValue(String.class);
                          String review =  dataSnapshot.child("review").getValue(String.class);
                          String userName = dataSnapshot.child("userName").getValue(String.class);
                          allUserRate += userName+" : "+rating+"\n"+review+"\n\n";
                      }
                     // Toast.makeText(FoodActivity.this, allUserRate, Toast.LENGTH_SHORT).show();
                  }
                  userReview.setText(allUserRate);
              }
              else
              {
                  userReview.setText("Sorry no review found !!!");
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

         ref.getReference().child("products/" + foodID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                           currentProduct = new ProductDetail(snapshot.child("name").getValue(String.class), snapshot.child("imageLink").getValue(String.class), snapshot.child("itemType").getValue(String.class), snapshot.child("briefDescription").getValue(String.class), snapshot.child("price").getValue(String.class), snapshot.getKey());
                            foodPrice.setText("Rs "+currentProduct.getProPrice());
                            foodDesc.setText(currentProduct.getProBriefDescription());
                            foodTitle.setText(currentProduct.getProName());
                            Glide.with(FoodActivity.this).load(currentProduct.getProImageLink()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.temp_image).into(foodImage);
                        }
                        else {
                            Toast.makeText(FoodActivity.this, "The item doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FoodActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FoodActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
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
                        if(ratingReview.getText().toString() == null)
                        {
                            ratingReview.setText("");
                            Toast.makeText(FoodActivity.this, "oof", Toast.LENGTH_SHORT).show();
                        }
                        FancyToast.makeText(FoodActivity.this, "Thank you For Rating", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                        ref.getReference().child("products/" + foodID + "/userRating/" + userID + "/review").setValue(ratingReview.getText().toString());
                        ref.getReference().child("products/" + foodID + "/userRating/" + userID + "/rating").setValue(ratingBar.getRating() + "");
                        ref.getReference().child("products/" + foodID + "/userRating/" + userID + "/userName").setValue(userDetail.getUserName());

                        ref.getReference().child("Users/" + userID + "/myReviews/" + foodID + "/review").setValue(ratingReview.getText().toString());
                        ref.getReference().child("Users/" + userID + "/myReviews/" + foodID + "/rating").setValue(ratingBar.getRating() + "");
                        ref.getReference().child("Users/" + userID + "/myReviews/" + foodID + "/foodName").setValue(currentProduct.getProName());

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
