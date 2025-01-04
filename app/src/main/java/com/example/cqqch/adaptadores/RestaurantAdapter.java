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
    private OnEditClickListener editClickListener;

    public RestaurantAdapter(List<Restaurant> restaurantList, OnFavoriteClickListener favoriteClickListener, OnDeleteClickListener deleteClickListener, OnEditClickListener editClickListener) {
        this.restaurantList = restaurantList;
        this.favoriteClickListener = favoriteClickListener;
        this.deleteClickListener = deleteClickListener;
        this.editClickListener = editClickListener;
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
        holder.category.setText("Categoría: " + restaurant.getCategory());
        holder.price.setText("Precio: " + restaurant.getPrice() + "€");
        holder.rating.setText("Rating: " + restaurant.getRating());
        holder.comment.setText("Comentario: " + restaurant.getComment());
        holder.tvCanGo.setText("Se puede ir: " + (restaurant.isCanGo() ? "Sí" : "No"));
        holder.tvCanOrder.setText("Se puede pedir: " + (restaurant.isCanOrder() ? "Sí" : "No"));

        // Asignar la imagen basada en las respuestas
        int imageResource = getImageBasedOnAnswers(restaurant.isCanGo(), restaurant.isCanOrder());
        holder.responseImage.setImageResource(imageResource);

        holder.favoriteIcon.setImageResource(
                restaurant.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Listeners
        holder.favoriteIcon.setOnClickListener(v -> favoriteClickListener.onFavoriteClick(restaurant));
        holder.deleteIcon.setOnClickListener(v -> deleteClickListener.onDeleteClick(restaurant));
        holder.editIcon.setOnClickListener(v -> editClickListener.onEditClick(restaurant));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, category, price, rating, comment, tvCanGo, tvCanOrder;
        ImageView favoriteIcon, deleteIcon, editIcon, responseImage;

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
            editIcon = itemView.findViewById(R.id.edit_icon);
            tvCanGo = itemView.findViewById(R.id.tvCanGo);
            tvCanOrder = itemView.findViewById(R.id.tvCanOrder);
            responseImage = itemView.findViewById(R.id.response_image);
        }
    }

    private int getImageBasedOnAnswers(boolean canGo, boolean canOrder) {
        if (canGo && canOrder) {
            return R.drawable.ic_can_go_and_can_order_yes; // Ambas son "Sí"
        } else if (canGo) {
            return R.drawable.ic_can_go_yes; // Solo "Se puede ir" es "Sí"
        } else if (canOrder) {
            return R.drawable.ic_can_order_yes; // Solo "Se puede pedir" es "Sí"
        } else {
            return R.drawable.ic_todo; // Ninguna es "Sí"
        }
    }

    // Interfaz para edición
    public interface OnEditClickListener {
        void onEditClick(Restaurant restaurant);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Restaurant restaurant);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Restaurant restaurant);
    }
}
