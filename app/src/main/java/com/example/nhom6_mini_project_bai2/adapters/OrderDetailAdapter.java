package com.example.nhom6_mini_project_bai2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6_mini_project_bai2.dal.AppDatabase;
import com.example.nhom6_mini_project_bai2.entities.OrderDetail;
import com.example.nhom6_mini_project_bai2.entities.Product;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<OrderDetail> orderDetails;
    private AppDatabase db;

    public OrderDetailAdapter(List<OrderDetail> orderDetails, AppDatabase db) {
        this.orderDetails = orderDetails;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        Product product = db.shoppingDao().getProductById(detail.productId);
        
        holder.text1.setText(product != null ? product.productName : "Unknown Product");
        holder.text2.setText("Qty: " + detail.quantity + " - Price: $" + detail.unitPrice);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
