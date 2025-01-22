package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;

/**
 * Actividad que proporciona opciones para que los usuarios puedan gestionar
 * restaurantes relacionados con pedidos. Incluye la visualización y
 * la adición de nuevos restaurantes.
 */
public class PedirActivity extends BaseActivity {

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, la navegación y la lógica específica de la actividad.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configura el diseño base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        // Infla el diseño específico para esta actividad en el contenedor del diseño base
        getLayoutInflater().inflate(R.layout.activity_pedir, findViewById(R.id.content_frame));

        // Configura la lógica específica de esta actividad
        setupPedirActivity();
    }

    /**
     * Configura los botones de la actividad y define sus acciones al ser presionados.
     */
    private void setupPedirActivity() {
        // Botón para ver la lista de restaurantes disponibles para pedidos
        Button btnVerRestaurantes = findViewById(R.id.btn_ver_restaurante);
        // Botón para añadir un nuevo restaurante
        Button btnAnadirRestaurante = findViewById(R.id.btn_add_restaurant);

        // Define la acción al hacer clic en "Ver Restaurantes"
        btnVerRestaurantes.setOnClickListener(v -> {
            Intent intent = new Intent(PedirActivity.this, VerRestaurantePedirActivity.class);
            startActivity(intent);
        });

        // Define la acción al hacer clic en "Añadir Restaurante"
        btnAnadirRestaurante.setOnClickListener(v -> {
            Intent intent = new Intent(PedirActivity.this, AddRestaurantActivity.class);
            startActivity(intent);
        });
    }
}
