package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;

/**
 * Actividad que proporciona opciones para que los usuarios interactúen con restaurantes.
 * Los usuarios pueden ver la lista de restaurantes o añadir uno nuevo.
 */
public class IrActivity extends BaseActivity {

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, la navegación y la lógica específica de la actividad.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configura el diseño base antes de inicializar el resto
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Configura la navegación lateral heredada de BaseActivity
        setupNavigation();

        // Infla el diseño específico de esta actividad en el contenedor del diseño base
        getLayoutInflater().inflate(R.layout.activity_ir, findViewById(R.id.content_frame));

        // Configura la lógica y los eventos específicos de esta actividad
        setupIrActivity();
    }

    /**
     * Inicializa los botones y configura los eventos de clic.
     */
    private void setupIrActivity() {
        // Botón para ver la lista de restaurantes
        Button btnVerRestaurantes = findViewById(R.id.btn_ver_restaurante);
        // Botón para añadir un nuevo restaurante
        Button btnAnadirRestaurante = findViewById(R.id.btn_add_restaurant);

        // Configura el evento del botón "Ver Restaurantes"
        btnVerRestaurantes.setOnClickListener(v -> {
            Log.d("IrActivity", "Botón Ver Restaurantes presionado");
            Intent intent = new Intent(IrActivity.this, VerRestaurantesActivity.class);
            startActivity(intent);
        });

        // Configura el evento del botón "Añadir Restaurante"
        btnAnadirRestaurante.setOnClickListener(v -> {
            Toast.makeText(this, "Añadir Restaurante", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(IrActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });
    }
}
