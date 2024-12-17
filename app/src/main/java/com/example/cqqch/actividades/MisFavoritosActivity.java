package com.example.cqqch.actividades;

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

    private RecyclerView rvFavoritos;
    private RestaurantAdapter restaurantAdapter;
    private RecetaAdapter recetaAdapter;

    private List<Restaurant> listaRestaurantes;
    private List<Receta> listaRecetas;

    private DatabaseReference database;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);


        // Inflar layout específico
        View misFavotirosView = getLayoutInflater().inflate(R.layout.activity_mis_favoritos, findViewById(R.id.content_frame));

        // Configura la navegación
        setupNavigation();

        // Inicializar RecyclerViews
        RecyclerView rvFavoritosRestaurantes = misFavotirosView.findViewById(R.id.rvFavoritosRestaurantes);
        RecyclerView rvFavoritosRecetas = misFavotirosView.findViewById(R.id.rvFavoritosRecetas);

        rvFavoritosRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        rvFavoritosRecetas.setLayoutManager(new LinearLayoutManager(this));

        listaRestaurantes = new ArrayList<>();
        listaRecetas = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        // Cargar favoritos separados
        cargarFavoritosRestaurantes(rvFavoritosRestaurantes);
        cargarFavoritosRecetas(rvFavoritosRecetas);
    }

    private void cargarFavoritosRestaurantes(RecyclerView recyclerView) {
        database.child("Restaurantes").child(user.getUid())
                .orderByChild("favorite").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRestaurantes.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Restaurant restaurant = data.getValue(Restaurant.class);
                            if (restaurant != null) listaRestaurantes.add(restaurant);
                        }
                        restaurantAdapter = new RestaurantAdapter(listaRestaurantes, MisFavoritosActivity.this::onFavoriteClicked, MisFavoritosActivity.this::onDeleteClicked);
                        recyclerView.setAdapter(restaurantAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FavoritosActivity", "Error al cargar favoritos de restaurantes", error.toException());
                    }
                });
    }

    private void cargarFavoritosRecetas(RecyclerView recyclerView) {
        database.child("Recetas").child(user.getUid())
                .orderByChild("favorite").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRecetas.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Receta receta = data.getValue(Receta.class);
                            if (receta != null) listaRecetas.add(receta);
                        }
                        recetaAdapter = new RecetaAdapter(listaRecetas, MisFavoritosActivity.this::onFavoriteClickedReceta, MisFavoritosActivity.this::onDeleteClickedReceta);
                        recyclerView.setAdapter(recetaAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FavoritosActivity", "Error al cargar favoritos de recetas", error.toException());
                    }
                });
    }

    private void mostrarResultados() {
        if (!listaRestaurantes.isEmpty()) {
            restaurantAdapter = new RestaurantAdapter(listaRestaurantes, this::onFavoriteClicked, this::onDeleteClicked);
            rvFavoritos.setAdapter(restaurantAdapter);
        }

        if (!listaRecetas.isEmpty()) {
            recetaAdapter = new RecetaAdapter(listaRecetas, this::onFavoriteClickedReceta, this::onDeleteClickedReceta);
            rvFavoritos.setAdapter(recetaAdapter);
        }

        if (listaRestaurantes.isEmpty() && listaRecetas.isEmpty()) {
            Toast.makeText(this, "No hay favoritos seleccionados", Toast.LENGTH_SHORT).show();
        }
    }

    private void onFavoriteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " marcado/desmarcado como favorito", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " eliminado", Toast.LENGTH_SHORT).show();
    }

    private void onFavoriteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " marcado/desmarcado como favorito", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " eliminado", Toast.LENGTH_SHORT).show();
    }

}