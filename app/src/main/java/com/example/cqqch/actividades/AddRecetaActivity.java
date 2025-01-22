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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Actividad para añadir una nueva receta a la base de datos de Firebase.
 * Permite al usuario ingresar detalles de la receta y guardarlos en la base de datos bajo su ID de usuario.
 */
public class AddRecetaActivity extends BaseActivity {

    // Elementos de la interfaz
    private EditText etNombreReceta, etIngredientes, etTiempoPreparacion, etPuntuacion, etPrecio, etDescripcion;
    private Spinner spCategoriaReceta;
    private Button btnGuardar;

    // Referencia a Firebase
    private DatabaseReference database;
    private FirebaseUser currentUser;

    /**
     * Método llamado cuando la actividad se crea.
     * Configura la interfaz de usuario, inicializa Firebase y configura los listeners de los botones.
     *
     * @param savedInstanceState El estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el layout específico dentro de content_frame
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

        // 6) Listeners
        btnGuardar.setOnClickListener(v -> guardarReceta());
        btnVolver.setOnClickListener(v -> finish());
    }

    /**
     * Método para guardar una nueva receta en Firebase.
     * Verifica los campos del formulario, valida los datos y los guarda en la base de datos.
     */
    private void guardarReceta() {
        // Verificamos que el usuario esté autenticado
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtenemos los valores del formulario
        String nombre = etNombreReceta.getText().toString().trim();
        String ingredientes = etIngredientes.getText().toString().trim();
        String tiempoPrepStr = etTiempoPreparacion.getText().toString().trim();
        String puntuacionStr = etPuntuacion.getText().toString().trim();
        String precio = etPrecio.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spCategoriaReceta.getSelectedItem().toString();

        // Validamos campos obligatorios
        if (nombre.isEmpty() || ingredientes.isEmpty() ||
                tiempoPrepStr.isEmpty() || puntuacionStr.isEmpty() ||
                descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parseamos y validamos tiempo de preparación
        int tiempoPreparacion;
        try {
            tiempoPreparacion = Integer.parseInt(tiempoPrepStr);
            if (tiempoPreparacion <= 0) {
                Toast.makeText(this, "El tiempo de preparación debe ser > 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El tiempo de preparación debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parseamos y validamos puntuación
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

        // Construimos el mapa de datos
        Map<String, Object> recetaData = new HashMap<>();
        // Genera la clave (ID único) con push()
        String userId = currentUser.getUid();
        String recetaId = database.child("Recetas").child(userId).push().getKey();

        // Verificar si se pudo generar un ID único
        if (recetaId == null) {
            Toast.makeText(this, "Error generando ID único", Toast.LENGTH_SHORT).show();
            return;
        }

        recetaData.put("id", recetaId);
        recetaData.put("name", nombre);
        recetaData.put("nameLowerCase", nombre.toLowerCase()); // para búsquedas
        recetaData.put("category", categoria);
        recetaData.put("ingredients", ingredientes);
        recetaData.put("preparationTime", tiempoPreparacion);
        recetaData.put("rating", puntuacion);
        recetaData.put("price", precio);
        recetaData.put("description", descripcion);
        recetaData.put("favorite", false); // Por defecto no es favorita

        // Guardamos el objeto en la ruta "Recetas/userId/recetaId"
        database.child("Recetas")
                .child(userId)
                .child(recetaId)
                .setValue(recetaData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Receta guardada correctamente", Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar la Activity
                    } else {
                        Toast.makeText(this, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Fallo: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
