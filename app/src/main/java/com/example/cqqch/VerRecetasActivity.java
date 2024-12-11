package com.example.cqqch;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VerRecetasActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecetaAdapter adapter;
    private List<Receta> recetaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();
        getLayoutInflater().inflate(R.layout.activity_ver_recetas, findViewById(R.id.content_frame));

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recetas_list);
        if (recyclerView == null) {
            Log.e("VerRecetasActivity", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recetaList = generateDummyRecetas();
        adapter = new RecetaAdapter(recetaList, this::onFavoriteClicked);
        recyclerView.setAdapter(adapter);
    }

    private void onFavoriteClicked(Receta receta) {
        receta.setFavorite(!receta.isFavorite());
        adapter.notifyDataSetChanged();
        Log.d("VerRecetasActivity", receta.getName() + " marcado como favorito: " + receta.isFavorite());
    }

    private List<Receta> generateDummyRecetas() {
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta("Receta 1", "Postre", "Harina, Azúcar", 30, 4.8, "$12", "Deliciosa receta de postre.", false));
        recetas.add(new Receta("Receta 2", "Entrada", "Lechuga, Zanahoria", 15, 4.5, "$8", "Entrada saludable.", false));
        return recetas;
    }
}