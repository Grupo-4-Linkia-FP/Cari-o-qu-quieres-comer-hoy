package com.example.cqqch.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Receta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddRecetaActivity extends BaseActivity {

    private EditText etNombreReceta, etIngredientes, etTiempoPreparacion, etPuntuacion, etPrecio, etDescripcion;
    private Spinner spCategoriaReceta;
    private Button btnGuardar;
    private DatabaseReference database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el layout específico dentro del content_frame
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        View addRecetaView = getLayoutInflater().inflate(R.layout.activity_add_receta, contentFrame, true);

        // Configura la navegación
        setupNavigation();

        // Inicializa Firebase
        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Vincula las vistas
        etNombreReceta = addRecetaView.findViewById(R.id.etNombreReceta);
        etIngredientes = addRecetaView.findViewById(R.id.etIngredientesReceta);
        etTiempoPreparacion = addRecetaView.findViewById(R.id.etTiempoPreparacion);
        etPuntuacion = addRecetaView.findViewById(R.id.etPuntuacionReceta);
        etPrecio = addRecetaView.findViewById(R.id.etPrecioReceta);
        etDescripcion = addRecetaView.findViewById(R.id.etDescripcionReceta);
        spCategoriaReceta = addRecetaView.findViewById(R.id.spCategoriaReceta);
        btnGuardar = addRecetaView.findViewById(R.id.btnGuardarReceta);
        Button btnVolver = addRecetaView.findViewById(R.id.btnVolverMenuReceta);

        // Configura el Spinner de categorías
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_recetas,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoriaReceta.setAdapter(categoryAdapter);

        // Evento para guardar la receta
        btnGuardar.setOnClickListener(v -> guardarReceta());

        // Evento para volver a la pantalla anterior
        btnVolver.setOnClickListener(v -> finish());
    }

    private void guardarReceta() {
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtén los valores del formulario
        String nombre = etNombreReceta.getText().toString().trim();
        String ingredientes = etIngredientes.getText().toString().trim();
        String tiempoPreparacionStr = etTiempoPreparacion.getText().toString().trim();
        String puntuacionStr = etPuntuacion.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoriaReceta.getSelectedItem().toString();

        // Validar campos obligatorios
        if (nombre.isEmpty() || ingredientes.isEmpty() || tiempoPreparacionStr.isEmpty() || puntuacionStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar tiempo de preparación
        int tiempoPreparacion;
        try {
            tiempoPreparacion = Integer.parseInt(tiempoPreparacionStr);
            if (tiempoPreparacion <= 0) {
                Toast.makeText(this, "El tiempo de preparación debe ser un número positivo", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El tiempo de preparación debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar puntuación
        double puntuacion;
        try {
            puntuacion = Double.parseDouble(puntuacionStr);
            if (puntuacion < 1 || puntuacion > 5) {
                Toast.makeText(this, "La puntuación debe estar entre 1 y 5", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La puntuación debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un mapa de datos para guardar la receta
        Map<String, Object> recetaData = new HashMap<>();
        recetaData.put("name", nombre);
        recetaData.put("nameLowerCase", nombre.toLowerCase()); // Guardar nombre en minúsculas para búsquedas
        recetaData.put("category", categoria);
        recetaData.put("ingredients", ingredientes);
        recetaData.put("preparationTime", tiempoPreparacion);
        recetaData.put("rating", puntuacion);
        recetaData.put("price", precio);
        recetaData.put("description", descripcion);
        recetaData.put("isFavorite", false); // Por defecto no es favorita

        // Guardar en la base de datos
        String userId = currentUser.getUid();
        database.child("Recetas").child(userId).push().setValue(recetaData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Receta guardada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Fallo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
