package com.example.cqqch.adaptadores;

import android.content.Context;
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
    private OnEditClickListener editClickListener;

    public RecetaAdapter(
            List<Receta> recetaList,
            OnFavoriteClickListener favoriteClickListener,
            OnDeleteClickListener deleteClickListener,
            OnEditClickListener editClickListener) {
        this.recetaList = recetaList;
        this.favoriteClickListener = favoriteClickListener;
        this.deleteClickListener = deleteClickListener;
        this.editClickListener = editClickListener;
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

        // Asignar valores a los elementos del ViewHolder
        holder.name.setText(receta.getName());
        holder.category.setText("Categoría: " + receta.getCategory());
        holder.ingredients.setText("Ingredientes: " + receta.getIngredients());
        holder.preparationTime.setText("Tiempo: " + receta.getPreparationTime() + " min");
        holder.rating.setText("Puntuación: " + receta.getRating());
        holder.price.setText("Precio: " + receta.getPrice() + "€");
        holder.description.setText("Descripción: " + receta.getDescription());

        // Asignar imagen basada en la categoría
        holder.image.setImageResource(getImageResourceForCategory(receta.getCategory()));

        // Configurar icono de favorito
        holder.favoriteIcon.setImageResource(
                receta.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Configurar listeners
        holder.favoriteIcon.setOnClickListener(v -> favoriteClickListener.onFavoriteClick(receta));
        holder.deleteIcon.setOnClickListener(v -> deleteClickListener.onDeleteClick(receta));
        holder.editIcon.setOnClickListener(v -> editClickListener.onEditClick(receta));
    }

    /**
     * Devuelve un recurso de imagen distinto según la categoría.
     * Ajusta los nombres de categorías y drawables a los que tengas en tu proyecto.
     */
    private int getImageResourceForCategory(String category) {
        if (category == null) {
            return R.drawable.ic_todo;
        }

        switch (category) {
            case "Vegano y Vegetariano":
            case "Ensaladas":
                return R.drawable.ic_ensalada;

            case "Postres":
            case "Dulces":
                return R.drawable.ic_dulce;

            case "Carne":
                return R.drawable.ic_carne;

            case "Pasta y Arroces":
                return R.drawable.ic_italiana;

            case "Pescado y Marisco":
                return R.drawable.ic_pescado;

            default:
                return R.drawable.ic_todo; // Imagen por defecto
        }
    }

    @Override
    public int getItemCount() {
        return recetaList.size();
    }

    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, ingredients, preparationTime, rating, price, description;
        ImageView image, favoriteIcon, deleteIcon, editIcon;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlace de los elementos del diseño
            name = itemView.findViewById(R.id.receta_name);
            category = itemView.findViewById(R.id.receta_category);
            ingredients = itemView.findViewById(R.id.receta_ingredients);
            preparationTime = itemView.findViewById(R.id.receta_preparation_time);
            rating = itemView.findViewById(R.id.receta_rating);
            price = itemView.findViewById(R.id.receta_price);
            description = itemView.findViewById(R.id.receta_description);

            image = itemView.findViewById(R.id.receta_image);
            favoriteIcon = itemView.findViewById(R.id.favorite_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
            editIcon = itemView.findViewById(R.id.edit_icon);
        }
    }

    // Interfaz para manejar el evento de edición
    public interface OnEditClickListener {
        void onEditClick(Receta receta);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Receta receta);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Receta receta);
    }
}
