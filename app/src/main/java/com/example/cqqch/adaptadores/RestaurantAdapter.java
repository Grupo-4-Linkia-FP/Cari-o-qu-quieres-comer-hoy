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
import com.example.cqqch.modelos.Restaurant;

import java.util.List;

/**
 * Adaptador para mostrar una lista de restaurantes en un RecyclerView.
 * Permite manejar las interacciones con los elementos de la lista (editar, eliminar, marcar como favorito).
 */
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private List<Restaurant> restaurantList;
    private OnFavoriteClickListener favoriteClickListener;
    private OnDeleteClickListener deleteClickListener;
    private OnEditClickListener editClickListener;

    /**
     * Constructor del adaptador.
     *
     * @param restaurantList       Lista de restaurantes a mostrar.
     * @param favoriteClickListener Listener para manejar clics en el ícono de favorito.
     * @param deleteClickListener   Listener para manejar clics en el ícono de eliminación.
     * @param editClickListener     Listener para manejar clics en el ícono de edición.
     */
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
        // Obtiene el restaurante actual
        Restaurant restaurant = restaurantList.get(position);

        // Asigna los datos al ViewHolder
        holder.name.setText(restaurant.getName());
        holder.address.setText(restaurant.getAddress());

        // Categoría
        String tituloCategoria = "Categoría: ";
        String contenidoCategoria = restaurant.getCategory();
        SpannableString spannableCategoria = new SpannableString(tituloCategoria + contenidoCategoria);
        spannableCategoria.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloCategoria.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.category.setText(spannableCategoria);

        // Precio
        String tituloPrecio = "Precio: ";
        String contenidoPrecio = restaurant.getPrice() + "€";
        SpannableString spannablePrecio = new SpannableString(tituloPrecio + contenidoPrecio);
        spannablePrecio.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloPrecio.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.price.setText(spannablePrecio);

        // Rating
        String tituloRating = "Rating: ";
        String contenidoRating = String.format("%.1f", restaurant.getRating());
        SpannableString spannableRating = new SpannableString(tituloRating + contenidoRating);
        spannableRating.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloRating.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.rating.setText(spannableRating);

        // Comentario
        String tituloComentario = "Comentario: ";
        String contenidoComentario = restaurant.getComment();
        SpannableString spannableComentario = new SpannableString(tituloComentario + contenidoComentario);
        spannableComentario.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloComentario.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.comment.setText(spannableComentario);

        // Se puede ir
        String tituloCanGo = "Se puede ir: ";
        String contenidoCanGo = restaurant.isCanGo() ? "Sí" : "No";
        SpannableString spannableCanGo = new SpannableString(tituloCanGo + contenidoCanGo);
        spannableCanGo.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloCanGo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCanGo.setText(spannableCanGo);

        // Se puede pedir
        String tituloCanOrder = "Se puede pedir: ";
        String contenidoCanOrder = restaurant.isCanOrder() ? "Sí" : "No";
        SpannableString spannableCanOrder = new SpannableString(tituloCanOrder + contenidoCanOrder);
        spannableCanOrder.setSpan(new StyleSpan(Typeface.BOLD), 0, tituloCanOrder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvCanOrder.setText(spannableCanOrder);

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

    /**
     * ViewHolder que contiene las vistas de un elemento de la lista.
     */
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

    /**
     * Retorna la imagen adecuada según si el restaurante permite ir o pedir.
     *
     * @param canGo    Si se puede ir al restaurante.
     * @param canOrder Si se puede pedir del restaurante.
     * @return ID del recurso de imagen.
     */
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

    // Interfaz para manejar el evento de clic en eliminar
    public interface OnFavoriteClickListener {
        void onFavoriteClick(Restaurant restaurant);
    }

    // Interfaz para manejar el evento de clic en editar
    public interface OnDeleteClickListener {
        void onDeleteClick(Restaurant restaurant);
    }
}
