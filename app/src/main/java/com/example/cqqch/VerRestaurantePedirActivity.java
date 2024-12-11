package com.example.cqqch;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VerRestaurantePedirActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        // Infla el diseño específico en el content_frame del BaseActivity
        getLayoutInflater().inflate(R.layout.activity_ver_restaurante_pedir, findViewById(R.id.content_frame));

        // Configurar RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.restaurant_list);

        if (recyclerView == null) {
            Log.e("VerRestaurantesPedir", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        // Configurar LayoutManager y Adaptador
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = generateRestaurantsForPedir();
        adapter = new RestaurantAdapter(restaurantList, this::onFavoriteClicked);
        recyclerView.setAdapter(adapter);
    }

    private void onFavoriteClicked(Restaurant restaurant) {
        restaurant.setFavorite(!restaurant.isFavorite());
        adapter.notifyDataSetChanged();
        Log.d("VerRestaurantesPedir", restaurant.getName() + " marcado como favorito: " + restaurant.isFavorite());
    }

    private List<Restaurant> generateRestaurantsForPedir() {
        List<Restaurant> allRestaurants = new ArrayList<>();

        // Agregar datos ficticios
        allRestaurants.add(new Restaurant("Restaurante 1", "Dirección 1", "Categoría 1", "$$", 4.5, "Buen ambiente", false, true, true));
        allRestaurants.add(new Restaurant("Restaurante 2", "Dirección 2", "Categoría 2", "$$$", 4.0, "Excelente comida", false, true, false));
        allRestaurants.add(new Restaurant("Restaurante 3", "Dirección 3", "Categoría 3", "$", 3.8, "Servicio rápido", false, false, true));

        // Filtrar solo los restaurantes con "Se puede pedir?" = true
        List<Restaurant> filteredList = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.isCanOrder()) {
                filteredList.add(restaurant);
            }
        }

        return filteredList;
    }
}
