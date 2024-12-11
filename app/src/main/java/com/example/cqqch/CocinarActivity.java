package com.example.cqqch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class CocinarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configura el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Configura la navegación
        setupNavigation();

        // Infla el contenido específico de esta actividad
        getLayoutInflater().inflate(R.layout.activity_cocinar, findViewById(R.id.content_frame));

        // Configura los botones específicos
        setupCocinarActivity();
    }

    private void setupCocinarActivity() {
        // Muestra un mensaje de bienvenida
        Toast.makeText(this, "Bienvenido a Cocinar", Toast.LENGTH_SHORT).show();

        // Configura los botones
        Button btnVerRecetas = findViewById(R.id.btn_recetas);
        Button btnAddReceta = findViewById(R.id.btn_add_receta);

        // Acción para el botón "Ver Recetas"
        btnVerRecetas.setOnClickListener(v -> {
            Intent intent = new Intent(CocinarActivity.this, VerRecetasActivity.class);
            startActivity(intent);
        });

        // Acción para el botón "Añadir Receta"
        btnAddReceta.setOnClickListener(v -> {
            Intent intent = new Intent(CocinarActivity.this, AddRecetaActivity.class);
            startActivity(intent);
        });
    }
}
