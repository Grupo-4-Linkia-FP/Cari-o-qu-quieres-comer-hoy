package com.example.cqqch.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;
import com.example.cqqch.modelos.Receta;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {

    private List<Receta> recetaList;
    private OnFavoriteClickListener favoriteClickListener;
    private OnDeleteClickListener deleteClickListener;

    public RecetaAdapter(List<Receta> recetaList, OnFavoriteClickListener favoriteClickListener, OnDeleteClickListener deleteClickListener) {
        this.recetaList = recetaList;
        this.favoriteClickListener = favoriteClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);
        return new RecetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder, int position) {
        Receta receta = recetaList.get(position);

        holder.name.setText(receta.getName());
        holder.category.setText("Categoría: " + receta.getCategory());
        holder.ingredients.setText("Ingredientes: " + receta.getIngredients());
        holder.preparationTime.setText("Tiempo: " + receta.getPreparationTime() + " min");
        holder.rating.setText("Puntuación: " + receta.getRating());
        holder.price.setText("Precio: " + receta.getPrice() + "€");
        holder.description.setText(receta.getDescription());

        holder.favoriteIcon.setImageResource(
                receta.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Configurar acción del icono de favorito
        holder.favoriteIcon.setOnClickListener(v -> favoriteClickListener.onFavoriteClick(receta));

        // Configurar acción del icono de eliminar
        holder.deleteIcon.setOnClickListener(v -> deleteClickListener.onDeleteClick(receta));
    }

    @Override
    public int getItemCount() {
        return recetaList.size();
    }

    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, ingredients, preparationTime, rating, price, description;
        ImageView favoriteIcon, deleteIcon;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.receta_name);
            category = itemView.findViewById(R.id.receta_category);
            ingredients = itemView.findViewById(R.id.receta_ingredients);
            preparationTime = itemView.findViewById(R.id.receta_preparation_time);
            rating = itemView.findViewById(R.id.receta_rating);
            price = itemView.findViewById(R.id.receta_price);
            description = itemView.findViewById(R.id.receta_description);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Receta receta);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Receta receta);
    }
}
