package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;
import com.example.cqqch.adaptadores.RestaurantAdapter;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad para mostrar una lista de restaurantes a los que se puede ir ("canGo = true").
 * Proporciona funciones para marcar como favorito, editar o eliminar restaurantes.
 */
public class VerRestaurantesActivity extends BaseActivity {

    // Componentes principales
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> listaRestaurantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico dentro del marco base
        View verRestaurantesView = getLayoutInflater().inflate(R.layout.activity_ver_restaurantes, findViewById(R.id.content_frame), true);

        // Configura la barra de navegación
        setupNavigation();

        // Inicializa componentes
        listaRestaurantes = new ArrayList<>();
        recyclerView = verRestaurantesView.findViewById(R.id.restaurant_list);

        if (recyclerView == null) {
            Log.e("VerRestaurantesActivity", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestaurantAdapter(listaRestaurantes, this::onFavoriteClicked, this::onDeleteClicked, this::onEditClicked);

        recyclerView.setAdapter(adapter);

        // Carga los datos de Firebase
        cargarRestaurantes();
    }

    /**
     * Método para cargar los restaurantes desde Firebase y mostrarlos en la lista.
     * Solo se incluyen restaurantes que respondieron "Sí" a la pregunta "¿Se puede ir?".
     */
    private void cargarRestaurantes() {
        // Obtiene el usuario actualmente autenticado
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verifica si el usuario está autenticado
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene el ID del usuario autenticado
        String userId = currentUser.getUid();

        // Referencia a la base de datos Firebase en la ruta "Restaurantes/{userId}"
        FirebaseDatabase.getInstance().getReference("Restaurantes").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    // Se ejecuta cuando se reciben los datos de Firebase
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Limpia la lista antes de agregar nuevos datos
                        listaRestaurantes.clear();

                        // Itera a través de todos los hijos en el nodo actual
                        for (DataSnapshot restauranteSnapshot : snapshot.getChildren()) {
                            // Convierte cada snapshot en un objeto Restaurant
                            Restaurant restaurante = restauranteSnapshot.getValue(Restaurant.class);

                            // Agrega a la lista solo los restaurantes donde "canGo" sea true
                            if (restaurante != null && restaurante.isCanGo()) {
                                listaRestaurantes.add(restaurante);
                            }
                        }

                        // Notifica al adaptador que la lista ha cambiado para que se actualice la vista
                        adapter.notifyDataSetChanged();
                    }

                    // Se ejecuta si ocurre un error al intentar leer los datos
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Registra el error en los logs
                        Log.e("VerRestaurantesActivity", "Error al cargar restaurantes", error.toException());

                        // Muestra un mensaje al usuario indicando que hubo un problema
                        Toast.makeText(VerRestaurantesActivity.this, "Error al cargar restaurantes", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * Marca o desmarca un restaurante como favorito.
     * @param restaurant Restaurante seleccionado.
     */
    private void onFavoriteClicked(Restaurant restaurant) {
        restaurant.setFavorite(!restaurant.isFavorite());
        adapter.notifyDataSetChanged();

        // Actualiza el estado de favorito en Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase.getInstance().getReference("Restaurantes")
                    .child(userId)
                    .orderByChild("name")
                    .equalTo(restaurant.getName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().child("favorite").setValue(restaurant.isFavorite());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("VerRestaurantesActivity", "Error al actualizar favorito", databaseError.toException());
                        }
                    });
        }
    }

    /**
     * Elimina un restaurante de Firebase y de la lista local.
     * @param restaurant Restaurante seleccionado.
     */
    private void onDeleteClicked(Restaurant restaurant) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase.getInstance().getReference("Restaurantes")
                    .child(userId)
                    .orderByChild("name")
                    .equalTo(restaurant.getName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(VerRestaurantesActivity.this, "Restaurante eliminado", Toast.LENGTH_SHORT).show();
                                            listaRestaurantes.remove(restaurant);
                                            adapter.notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(VerRestaurantesActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("VerRestaurantesActivity", "Error al eliminar restaurante", databaseError.toException());
                        }
                    });
        }
    }

    /**
     * Lanza la actividad para editar el restaurante seleccionado.
     * @param restaurant Restaurante seleccionado.
     */
    private void onEditClicked(Restaurant restaurant) {

        Intent intent = new Intent(this, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }


}
