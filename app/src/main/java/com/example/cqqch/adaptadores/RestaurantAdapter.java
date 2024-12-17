package com.example.cqqch.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;
import com.example.cqqch.modelos.Restaurant;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private OnFavoriteClickListener favoriteClickListener;
    private OnDeleteClickListener deleteClickListener;

    public RestaurantAdapter(List<Restaurant> restaurantList, OnFavoriteClickListener favoriteClickListener, OnDeleteClickListener deleteClickListener) {
        this.restaurantList = restaurantList;
        this.favoriteClickListener = favoriteClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.name.setText(restaurant.getName());
        holder.address.setText(restaurant.getAddress());
        holder.category.setText(restaurant.getCategory());
        holder.price.setText(restaurant.getPrice());
        holder.rating.setText("Rating: " + restaurant.getRating());
        holder.comment.setText(restaurant.getComment());

        holder.favoriteIcon.setImageResource(
                restaurant.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        holder.favoriteIcon.setOnClickListener(v -> favoriteClickListener.onFavoriteClick(restaurant));

        // Configura el icono de eliminar
        holder.deleteIcon.setOnClickListener(v -> deleteClickListener.onDeleteClick(restaurant));
    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, category, price, rating, comment;
        ImageView favoriteIcon, deleteIcon;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            address = itemView.findViewById(R.id.restaurant_address);
            category = itemView.findViewById(R.id.restaurant_category);
            price = itemView.findViewById(R.id.restaurant_price);
            rating = itemView.findViewById(R.id.restaurant_rating);
            comment = itemView.findViewById(R.id.restaurant_comment);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Restaurant restaurant);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Restaurant restaurant);
    }
}