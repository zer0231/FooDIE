package com.zero.foodie.customAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zero.foodie.FoodActivity;
import com.zero.foodie.R;
import com.zero.foodie.model.OrganizationDetail;

import java.util.ArrayList;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrganizationHolder> {
    private ArrayList<OrganizationDetail> organizationDetails;
    private Context context;

    public OrganizationAdapter(Context context, ArrayList<OrganizationDetail> organizationDetails)
    {
        this.context = context;
        this.organizationDetails = organizationDetails;
    }
    @NonNull
    @Override
    public OrganizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_product,parent,false);
        return new OrganizationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationHolder holder, int position) {
        OrganizationDetail currentItem = organizationDetails.get(position);

//        Here we set to the view in layout
        Glide.with(context).load(currentItem.getOrgImageLink()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.temp_image).into(holder.poster);
        holder.name.setText(currentItem.getOrgName());
        holder.address.setText(currentItem.getOrgAddress());
        holder.visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               for(int i = 0;i < currentItem.getProducts().size();i++)
//                {
//                    if(currentItem.getOrgName() == currentItem.getProducts().get(i).getOrgId())
//                            {
//                                Toast.makeText(context,currentItem.getOrgName()+":"+ currentItem.getProducts().get(i).getProName(), Toast.LENGTH_SHORT).show();
//                            }
//
//                }
                context.startActivity(new Intent(context, FoodActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
    }



    @Override
    public int getItemCount() {
        return organizationDetails.size();
    }

    public class OrganizationHolder extends RecyclerView.ViewHolder {
        //Here we initialize view
        public TextView name,address;
        public Button visit,contatct;
        public ImageView poster;
        public OrganizationHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.proPoster);
        }
    }
}
