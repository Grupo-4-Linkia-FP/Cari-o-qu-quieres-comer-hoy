package com.example.cqqch.actividades;

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

public class VerRestaurantesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> listaRestaurantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico en el content_frame del BaseActivity
        View verRestaurantesView = getLayoutInflater().inflate(R.layout.activity_ver_restaurantes, findViewById(R.id.content_frame), true);

        // Llama a setupNavigation() después de inflar el layout
        setupNavigation();

        // Inicializa la lista y el RecyclerView
        listaRestaurantes = new ArrayList<>();
        recyclerView = verRestaurantesView.findViewById(R.id.restaurant_list);

        // Llama a setupNavigation() después de inflar el layout
        if (recyclerView == null) {
            Log.e("VerRestaurantesActivity", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RestaurantAdapter(listaRestaurantes, this::onFavoriteClicked, this::onDeleteClicked);

        recyclerView.setAdapter(adapter);

        // Cargar los datos de Firebase
        cargarRestaurantes();
    }

    private void cargarRestaurantes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        FirebaseDatabase.getInstance().getReference("Restaurantes").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRestaurantes.clear();
                        for (DataSnapshot restauranteSnapshot : snapshot.getChildren()) {
                            Restaurant restaurante = restauranteSnapshot.getValue(Restaurant.class);
                            if (restaurante != null) {
                                listaRestaurantes.add(restaurante);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("VerRestaurantesActivity", "Error al cargar restaurantes", error.toException());
                        Toast.makeText(VerRestaurantesActivity.this, "Error al cargar restaurantes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

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

}
