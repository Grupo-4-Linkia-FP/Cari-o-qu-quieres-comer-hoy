package com.example.cqqch.adaptadores;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
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

/**
 * Adaptador para gestionar y mostrar una lista de recetas en un RecyclerView.
 */
public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder> {

    private List<Receta> recetaList; // Lista de recetas a mostrar.
    private OnFavoriteClickListener favoriteClickListener; // Evento al marcar receta como favorita.
    private OnDeleteClickListener deleteClickListener; // Evento al eliminar una receta.
    private OnEditClickListener editClickListener; // Evento al editar una receta.

    /**
     * Constructor para inicializar el adaptador con la lista de recetas y los eventos.
     */
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

    /**
     * Método para inflar el diseño de cada elemento de la lista.
     */
    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);
        return new RecetaViewHolder(view);
    }

    /**
     * Método para vincular los datos de una receta al ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder, int position) {
        Receta receta = recetaList.get(position);

        // Configuración del nombre con estilo de texto.
        String tituloNombre = "Nombre: ";
        String contenidoNombre = receta.getName();
        SpannableString spannableNombre = new SpannableString(tituloNombre + contenidoNombre);
        spannableNombre.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloNombre.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.name.setText(spannableNombre);

        // Configuración de la categoría.
        String tituloCategoria = "Categoría: ";
        String contenidoCategoria = receta.getCategory();
        SpannableString spannableCategoria = new SpannableString(tituloCategoria + contenidoCategoria);
        spannableCategoria.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloCategoria.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.category.setText(spannableCategoria);

        // Configuración de los ingredientes.
        String tituloIngredientes = "Ingredientes: ";
        String contenidoIngredientes = receta.getIngredients();
        SpannableString spannableIngredientes = new SpannableString(tituloIngredientes + contenidoIngredientes);
        spannableIngredientes.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloIngredientes.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.ingredients.setText(spannableIngredientes);

        // Configuración del tiempo de preparación.
        String tituloTiempo = "Tiempo: ";
        String contenidoTiempo = receta.getPreparationTime() + " min";
        SpannableString spannableTiempo = new SpannableString(tituloTiempo + contenidoTiempo);
        spannableTiempo.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloTiempo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.preparationTime.setText(spannableTiempo);

        // Configuración de la puntuación.
        String tituloPuntuacion = "Puntuación: ";
        String contenidoPuntuacion = String.format("%.1f", receta.getRating());
        SpannableString spannablePuntuacion = new SpannableString(tituloPuntuacion + contenidoPuntuacion);
        spannablePuntuacion.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloPuntuacion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.rating.setText(spannablePuntuacion);

        // Configuración del precio.
        String tituloPrecio = "Precio: ";
        String contenidoPrecio = receta.getPrice() + "€";
        SpannableString spannablePrecio = new SpannableString(tituloPrecio + contenidoPrecio);
        spannablePrecio.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloPrecio.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.price.setText(spannablePrecio);

        // Configuración de la descripción.
        String tituloDescripcion = "Descripción: ";
        String contenidoDescripcion = receta.getDescription();
        SpannableString spannableDescripcion = new SpannableString(tituloDescripcion + contenidoDescripcion);
        spannableDescripcion.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloDescripcion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.description.setText(spannableDescripcion);

        // Asignar la imagen según la categoría.
        holder.image.setImageResource(getImageResourceForCategory(receta.getCategory()));

        // Configurar el ícono de favorito.
        holder.favoriteIcon.setImageResource(
                receta.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline
        );

        // Configurar listeners para los eventos.
        holder.favoriteIcon.setOnClickListener(v -> favoriteClickListener.onFavoriteClick(receta));
        holder.deleteIcon.setOnClickListener(v -> deleteClickListener.onDeleteClick(receta));
        holder.editIcon.setOnClickListener(v -> editClickListener.onEditClick(receta));
    }

    /**
     * Devuelve el recurso de imagen correspondiente a la categoría de la receta.
     */
    private int getImageResourceForCategory(String category) {
        if (category == null) {
            return R.drawable.ic_todo;
        }

        switch (category) {
            case "Vegano y Vegetariano":
            case "Ensaladas":
                return R.drawable.ic_ensalada;
            case "Postres y Repostería":
                return R.drawable.ic_dulce;
            case "Carne":
                return R.drawable.ic_carne;
            case "Pasta y Arroces":
                return R.drawable.ic_italiana;
            case "Pescado y Marisco":
                return R.drawable.ic_pescado;
            default:
                return R.drawable.ic_todo;
        }
    }

    @Override
    public int getItemCount() {
        return recetaList.size();
    }

    /**
     * ViewHolder para contener las vistas de cada elemento de la lista.
     */
    public static class RecetaViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, ingredients, preparationTime, rating, price, description;
        ImageView image, favoriteIcon, deleteIcon, editIcon;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlace de las vistas del diseño.
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

    // Interfaces para manejar eventos.
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
