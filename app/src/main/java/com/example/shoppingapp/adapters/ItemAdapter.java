package com.example.shoppingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<item> itemList;
    private OnItemClickListener onItemClickListener;

    // Constructor
    public ItemAdapter(List<item> itemList) {
        this.itemList = itemList != null ? itemList : new ArrayList<>();
    }

    // Interface for item click handling
    public interface OnItemClickListener {
        void onItemRemove(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        item item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.price.setText(String.format("$%.2f", item.getPrice() * item.getAmount()));
        holder.amount.setText("Quantity: " + item.getAmount());

        holder.deleteIcon.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemRemove(position);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> updateItemAmount(holder, item, 1));
        holder.btnDecrease.setOnClickListener(v -> updateItemAmount(holder, item, -1));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Method to add an item
    public void addItem(item newItem) {
        itemList.add(newItem);
        notifyItemInserted(itemList.size() - 1);
    }

    // Method to remove an item
    public void removeItem(int position) {
        if (position >= 0 && position < itemList.size()) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Helper method to update item quantity and price
    private void updateItemAmount(ItemViewHolder holder, item item, int delta) {
        int newAmount = item.getAmount() + delta;
        if (newAmount > 0) {
            item.setAmount(newAmount);
            holder.amount.setText("Quantity: " + newAmount);
            holder.price.setText(String.format("$%.2f", item.getPrice() * newAmount));
        }
    }

    // ViewHolder class
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price, amount;
        ImageView deleteIcon;
        Button btnIncrease, btnDecrease;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            description = itemView.findViewById(R.id.itemDescription);
            price = itemView.findViewById(R.id.itemPrice);
            amount = itemView.findViewById(R.id.itemAmount);
            deleteIcon = itemView.findViewById(R.id.itemDelete);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
