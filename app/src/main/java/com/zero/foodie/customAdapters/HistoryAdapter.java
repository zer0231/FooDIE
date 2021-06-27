package com.zero.foodie.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zero.foodie.R;
import com.zero.foodie.model.OrderHistoryModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    Context context;
    ArrayList<OrderHistoryModel> orderList;

    public HistoryAdapter(Context context, ArrayList<OrderHistoryModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_history, parent, false);
        return new HistoryAdapter.HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        OrderHistoryModel currentOrder = orderList.get(position);
        int i;
        ArrayList<String> names = currentOrder.getProductList();
        ArrayList<String> prices = currentOrder.getPrice();
        ArrayList<String> quantity = currentOrder.getQuantity();
        for (i = 0; i < names.size(); i++) {
            holder.historyName.append("\n" + names.get(i));
        }
        for (i = 0; i < prices.size(); i++) {
            holder.historyPrice.append("\n" + prices.get(i));
        }
        for (i = 0; i < quantity.size(); i++) {
            holder.historyQuantity.append("\n" + quantity.get(i));
        }
        holder.historyDate.setText(currentOrder.getDate());
        holder.historyPrice.append("\n\nTotal:"+currentOrder.getTotal());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView historyName, historyPrice, historyQuantity, historyDate;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            historyName = itemView.findViewById(R.id.historyName);
            historyPrice = itemView.findViewById(R.id.historyPrice);
            historyQuantity = itemView.findViewById(R.id.historyQuantity);
            historyDate = itemView.findViewById(R.id.historyDate);


        }
    }
}
