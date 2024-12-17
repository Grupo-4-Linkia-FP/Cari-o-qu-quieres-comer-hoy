package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.cqqch.R;
import com.example.cqqch.actividades.VerRestaurantesActivity;
import com.example.cqqch.base.BaseActivity;

public class IrActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Primero setea el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Luego configura la navegación
        setupNavigation();

        // Ahora infla el layout específico de esta actividad en el content_frame
        getLayoutInflater().inflate(R.layout.activity_ir, findViewById(R.id.content_frame));

        // Finalmente configura la lógica específica de esta actividad
        setupIrActivity();
    }

    private void setupIrActivity() {
        Button btnVerRestaurantes = findViewById(R.id.btn_ver_restaurante);
        Button btnAnadirRestaurante = findViewById(R.id.btn_add_restaurant);

        btnVerRestaurantes.setOnClickListener(v -> {
            Log.d("IrActivity", "Botón Ver Restaurantes presionado");
            Intent intent = new Intent(IrActivity.this, VerRestaurantesActivity.class);
            startActivity(intent);
        });

        btnAnadirRestaurante.setOnClickListener(v -> {
            Toast.makeText(this, "Añadir Restaurante", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(IrActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });
    }
}
