package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;

public class PedirActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configura el diseño base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        // Infla el diseño específico
        getLayoutInflater().inflate(R.layout.activity_pedir, findViewById(R.id.content_frame));

        // Configurar la lógica para los botones
        setupPedirActivity();
    }

    private void setupPedirActivity() {
        Button btnVerRestaurantes = findViewById(R.id.btn_ver_restaurante);
        Button btnAnadirRestaurante = findViewById(R.id.btn_add_restaurant);

        btnVerRestaurantes.setOnClickListener(v -> {
            Intent intent = new Intent(PedirActivity.this, VerRestaurantePedirActivity.class);
            startActivity(intent);
        });

        btnAnadirRestaurante.setOnClickListener(v -> {
            Intent intent = new Intent(PedirActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });
    }
}
