package com.example.project498;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ShoeViewHolder> {
    private List<Shoe> favouriteShoes;
    private OnShoeClickListener listener;

    public FavouriteAdapter(List<Shoe> favouriteShoes, OnShoeClickListener listener) {
        this.favouriteShoes = favouriteShoes;
        this.listener = listener;
    }

    @Override
    public ShoeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflates the item layout for the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_shoe, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoeViewHolder holder, int position) {
        Shoe shoe = favouriteShoes.get(position);

        // Bind the shoe details to the views
        holder.shoeName.setText(shoe.getName());
        holder.shoePrice.setText("$" + shoe.getPrice());
        holder.shoeImage.setImageResource(shoe.getImageResId());  // Set the shoe image

        // Set the click listener for the remove button
        holder.removeButton.setOnClickListener(v -> {
            listener.onRemoveClick(shoe);  // Notify the listener to remove the shoe
        });

        // Set the click listener for the entire item (for detailed view, for example)
        holder.itemView.setOnClickListener(v -> listener.onShoeClick(shoe));
    }

    @Override
    public int getItemCount() {
        return favouriteShoes.size();
    }

    // ViewHolder class to hold the view references for each item in the RecyclerView
    public static class ShoeViewHolder extends RecyclerView.ViewHolder {
        TextView shoeName, shoePrice;
        ImageView shoeImage;
        Button removeButton;

        public ShoeViewHolder(View itemView) {
            super(itemView);
            shoeName = itemView.findViewById(R.id.shoe_name);
            shoePrice = itemView.findViewById(R.id.shoe_price);
            shoeImage = itemView.findViewById(R.id.shoe_image);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }

    // Interface to handle click events for shoes (either to view details or remove)
    public interface OnShoeClickListener {
        void onShoeClick(Shoe shoe);       // Called when shoe item is clicked (for details, for example)
        void onRemoveClick(Shoe shoe);     // Called when remove button is clicked
    }
}
