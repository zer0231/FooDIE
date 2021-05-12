package com.zero.foodie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrganizationHolder> {
    private ArrayList<OrganizationDetail> organizationDetails;
    private Context context;

    public OrganizationAdapter(Context context,ArrayList<OrganizationDetail> organizationDetails)
    {
        this.context = context;
        this.organizationDetails = organizationDetails;
    }
    @NonNull
    @Override
    public OrganizationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.organization_cardview,parent,false);
        return new OrganizationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationHolder holder, int position) {
        OrganizationDetail currentItem = organizationDetails.get(position);
        String id = currentItem.getId();
        String name = currentItem.getName();
        String image_url = currentItem.getImage_url();
        String address = currentItem.getAddress();

        //Here we set to the view in layout
        Glide.with(context).load(image_url).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.sponge).into(holder.poster);
        holder.name.setText(name);
        holder.address.setText(address);
    }

    @Override
    public int getItemCount() {
        return organizationDetails.size();
    }

    public class OrganizationHolder extends RecyclerView.ViewHolder {
        //Here we initialize view
        public TextView name,address;
        public ImageView poster;
        public OrganizationHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.organizationName);
            address = itemView.findViewById(R.id.organizationAddress);
            poster = itemView.findViewById(R.id.organizationPoster);
        }
    }
}
