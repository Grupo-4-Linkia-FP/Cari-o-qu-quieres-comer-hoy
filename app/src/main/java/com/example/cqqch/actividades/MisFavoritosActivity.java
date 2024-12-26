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
import com.example.cqqch.adaptadores.RecetaAdapter;
import com.example.cqqch.adaptadores.RestaurantAdapter;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Receta;
import com.example.cqqch.modelos.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisFavoritosActivity extends BaseActivity {

    private static final String TAG = "MisFavoritosActivity";

    // Adaptadores
    private RestaurantAdapter restaurantAdapter;
    private RecetaAdapter recetaAdapter;

    // Listas de datos
    private List<Restaurant> listaRestaurantes;
    private List<Receta> listaRecetas;

    // Firebase
    private DatabaseReference database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Inflar layout específico dentro de activity_base
        View favoritesView = getLayoutInflater().inflate(
                R.layout.activity_mis_favoritos,
                findViewById(R.id.content_frame)
        );

        // Configura la navegación lateral (Drawer) si corresponde
        setupNavigation();

        // Inicializar RecyclerViews
        RecyclerView rvFavoritosRestaurantes = favoritesView.findViewById(R.id.rvFavoritosRestaurantes);
        RecyclerView rvFavoritosRecetas = favoritesView.findViewById(R.id.rvFavoritosRecetas);

        rvFavoritosRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        rvFavoritosRecetas.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar listas
        listaRestaurantes = new ArrayList<>();
        listaRecetas = new ArrayList<>();

        // Obtener usuario actual de Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Si no hay usuario logueado, muestra mensaje y termina la Activity
            Toast.makeText(this, "No estás autenticado. Inicia sesión para ver favoritos.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtener referencia a la base de datos
        database = FirebaseDatabase.getInstance().getReference();

        // Cargar favoritos (restaurantes y recetas) por separado
        cargarFavoritosRestaurantes(rvFavoritosRestaurantes);
        cargarFavoritosRecetas(rvFavoritosRecetas);
    }

    /**
     * Carga los restaurantes marcados como favoritos (campo "favorite" = true)
     * para el usuario actual, y los asigna a un RecyclerView.
     */
    private void cargarFavoritosRestaurantes(@NonNull RecyclerView recyclerView) {
        database.child("Restaurantes")
                .child(user.getUid())
                .orderByChild("favorite")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRestaurantes.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Restaurant restaurant = data.getValue(Restaurant.class);
                            if (restaurant != null) {
                                listaRestaurantes.add(restaurant);
                            }
                        }
                        restaurantAdapter = new RestaurantAdapter(
                                listaRestaurantes,
                                MisFavoritosActivity.this::onFavoriteClicked,
                                MisFavoritosActivity.this::onDeleteClicked,
                                MisFavoritosActivity.this::onEditClicked
                        );
                        recyclerView.setAdapter(restaurantAdapter);

                        if (listaRestaurantes.isEmpty()) {
                            Log.d(TAG, "No hay restaurantes favoritos para este usuario.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error al cargar favoritos de restaurantes", error.toException());
                    }
                });
    }

    /**
     * Carga las recetas marcadas como favoritas (campo "favorite" = true)
     * para el usuario actual, y las asigna a un RecyclerView.
     */
    private void cargarFavoritosRecetas(@NonNull RecyclerView recyclerView) {
        database.child("Recetas")
                .child(user.getUid())
                .orderByChild("favorite")
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRecetas.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Receta receta = data.getValue(Receta.class);
                            if (receta != null) {
                                listaRecetas.add(receta);
                            }
                        }
                        recetaAdapter = new RecetaAdapter(
                                listaRecetas,
                                MisFavoritosActivity.this::onFavoriteClickedReceta,
                                MisFavoritosActivity.this::onDeleteClickedReceta,
                                MisFavoritosActivity.this::onEditClickedReceta
                        );
                        recyclerView.setAdapter(recetaAdapter);

                        if (listaRecetas.isEmpty()) {
                            Log.d(TAG, "No hay recetas favoritas para este usuario.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error al cargar favoritos de recetas", error.toException());
                    }
                });
    }


    // Cuando se hace clic en favorito de un Restaurant
    private void onFavoriteClicked(Restaurant restaurant) {
        // Aquí podrías actualizar Firebase para marcar/desmarcar favorito
        Toast.makeText(this,
                        restaurant.getName() + " marcado/desmarcado como favorito",
                        Toast.LENGTH_SHORT)
                .show();
    }

    // Cuando se hace clic en eliminar un Restaurant
    private void onDeleteClicked(Restaurant restaurant) {
        // Lógica para eliminarlo de Firebase si corresponde
        Toast.makeText(this,
                        restaurant.getName() + " eliminado",
                        Toast.LENGTH_SHORT)
                .show();
    }

    // Cuando se hace clic en favorito de una Receta
    private void onFavoriteClickedReceta(Receta receta) {
        // Aquí podrías actualizar Firebase para marcar/desmarcar favorito
        Toast.makeText(this,
                        receta.getName() + " marcado/desmarcado como favorito",
                        Toast.LENGTH_SHORT)
                .show();
    }

    // Cuando se hace clic en eliminar una Receta
    private void onDeleteClickedReceta(Receta receta) {
        // Lógica para eliminarla de Firebase si corresponde
        Toast.makeText(this,
                        receta.getName() + " eliminada",
                        Toast.LENGTH_SHORT)
                .show();
    }

    // Cuando se hace clic en editar un Restaurant
    private void onEditClicked(Restaurant restaurant) {
        Intent intent = new Intent(this, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    // Cuando se hace clic en editar una Receta
    private void onEditClickedReceta(Receta receta) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("receta", receta);
        startActivity(intent);
    }
}
