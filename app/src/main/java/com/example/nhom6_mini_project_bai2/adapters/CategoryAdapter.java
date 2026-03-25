package com.example.nhom6_mini_project_bai2.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6_mini_project_bai2.R;
import com.example.nhom6_mini_project_bai2.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, boolean isSelected);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.categoryName);

        // Highlight if selected
        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#BBDEFB")); // Light Blue
            holder.tvCategoryName.setTextColor(Color.BLUE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.tvCategoryName.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            if (selectedPosition == position) {
                // Deselect
                selectedPosition = -1;
                listener.onCategoryClick(null, false);
            } else {
                // Select
                selectedPosition = position;
                listener.onCategoryClick(category, true);
            }
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            cardView = (CardView) itemView;
        }
    }
}
