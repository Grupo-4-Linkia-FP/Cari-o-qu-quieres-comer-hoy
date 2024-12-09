package com.example.cqqch;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VerRestaurantesActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        // Inflar el diseño específico en el content_frame del BaseActivity
        getLayoutInflater().inflate(R.layout.activity_ver_restaurantes, findViewById(R.id.content_frame));

        // Configurar RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // Encuentra el RecyclerView
        recyclerView = findViewById(R.id.restaurant_list);
        if (recyclerView == null) {
            Log.e("VerRestaurantesActivity", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        // Configura el LayoutManager y el adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = generateDummyRestaurants();
        adapter = new RestaurantAdapter(restaurantList, this::onFavoriteClicked);
        recyclerView.setAdapter(adapter);
    }

    // Manejo del clic en el corazón (favorito)
    private void onFavoriteClicked(Restaurant restaurant) {
        restaurant.setFavorite(!restaurant.isFavorite());
        adapter.notifyDataSetChanged();

    }

    // Generar datos ficticios para la lista
    private List<Restaurant> generateDummyRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Restaurante 1", "Dirección 1", "Categoría 1", "$$", 4.5, "Buen ambiente", false));
        restaurants.add(new Restaurant("Restaurante 2", "Dirección 2", "Categoría 2", "$$$", 4.0, "Excelente comida", false));
        restaurants.add(new Restaurant("Restaurante 3", "Dirección 3", "Categoría 3", "$", 3.8, "Servicio rápido", false));
        return restaurants;
    }
}
