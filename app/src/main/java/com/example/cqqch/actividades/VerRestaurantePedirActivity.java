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

public class VerRestaurantePedirActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico en el content_frame del BaseActivity
        View verRestaurantesPedirView = getLayoutInflater().inflate(R.layout.activity_ver_restaurante_pedir, findViewById(R.id.content_frame), true);

        // Configura la barra de navegación
        setupNavigation();

        // Inicializar RecyclerView y lista
        recyclerView = verRestaurantesPedirView.findViewById(R.id.restaurant_list);
        if (recyclerView == null) {
            Log.e("VerRestaurantesPedir", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList, this::onFavoriteClicked, this::onDeleteClicked);
        recyclerView.setAdapter(adapter);

        // Cargar los datos reales desde Firebase
        cargarRestaurantesQueSePuedenPedir();
    }

    private void cargarRestaurantesQueSePuedenPedir() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        FirebaseDatabase.getInstance().getReference("Restaurantes").child(userId)
                .orderByChild("canOrder")
                .equalTo(true) // Filtrar solo los restaurantes donde "canOrder" = true
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        restaurantList.clear();
                        for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                            Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                            if (restaurant != null) {
                                restaurantList.add(restaurant);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("VerRestaurantesPedir", "Error al cargar restaurantes: " + error.getMessage());
                        Toast.makeText(VerRestaurantePedirActivity.this, "Error al cargar restaurantes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onFavoriteClicked(Restaurant restaurant) {
        restaurant.setFavorite(!restaurant.isFavorite());
        adapter.notifyDataSetChanged();

        // Actualizar estado favorito en Firebase
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
                            Log.e("VerRestaurantesPedir", "Error al actualizar favorito", databaseError.toException());
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
                                snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                                    Toast.makeText(VerRestaurantePedirActivity.this, "Restaurante eliminado", Toast.LENGTH_SHORT).show();
                                    restaurantList.remove(restaurant);
                                    adapter.notifyDataSetChanged();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(VerRestaurantePedirActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("VerRestaurantesPedir", "Error al eliminar restaurante", databaseError.toException());
                        }
                    });
        }
    }
}
