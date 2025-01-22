package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Receta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Actividad para editar los detalles de una receta existente.
 * Permite al usuario cargar datos desde un objeto Receta, realizar cambios
 * y guardar las actualizaciones en Firebase.
 */
public class EditRecipeActivity extends BaseActivity {

    // Modelo de receta
    private Receta receta;

    // Elementos de la interfaz
    private EditText etName, etDescription, etIngredients, etPreparationTime, etPrice, etRating;
    private Spinner spCategory;
    private Button btnGuardar;

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, inicializa las vistas y carga los datos de la receta a editar.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Usar un diseño base para inflar el contenido específico
        setContentView(R.layout.activity_base);

        // Infla el diseño específico en el marco de contenido del diseño base
        getLayoutInflater().inflate(R.layout.activity_edit_recipe, findViewById(R.id.content_frame));

        // Configura la navegación heredada de BaseActivity
        setupNavigation();

        // Inicializa las vistas y configura los elementos de la actividad
        setupEditRecipeActivity();

        // Obtiene la receta desde el Intent
        receta = getIntent().getParcelableExtra("receta");
        if (receta == null) {
            Toast.makeText(this, "Error al cargar la receta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Carga los datos de la receta en los campos de la interfaz
        cargarDatosReceta();
    }

    /**
     * Inicializa las vistas de la actividad y configura el spinner y el botón de guardar.
     */
    private void setupEditRecipeActivity() {
        // Inicializa los campos de texto
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etIngredients = findViewById(R.id.etIngredients);
        etPreparationTime = findViewById(R.id.etPreparationTime);
        etPrice = findViewById(R.id.etPrice);
        etRating = findViewById(R.id.etRating);

        // Inicializa el spinner de categoría
        spCategory = findViewById(R.id.spCategory);

        // Inicializa el botón de guardar
        btnGuardar = findViewById(R.id.btnSave);

        // Configura el spinner y el evento del botón
        configurarSpinnerCategoria();
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    /**
     * Configura el spinner de categorías con datos desde un recurso de arrays.
     */
    private void configurarSpinnerCategoria() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_recetas,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    /**
     * Carga los datos de la receta en los campos de texto y selecciona la categoría correspondiente.
     */
    private void cargarDatosReceta() {
        etName.setText(receta.getName());
        etDescription.setText(receta.getDescription());
        etIngredients.setText(receta.getIngredients());
        etPreparationTime.setText(String.valueOf(receta.getPreparationTime()));
        etPrice.setText(receta.getPrice());
        etRating.setText(String.valueOf(receta.getRating()));

        // Selecciona la categoría actual en el spinner
        setSpinnerValue(spCategory, receta.getCategory());
    }

    /**
     * Selecciona un valor específico en un Spinner.
     *
     * @param spinner El Spinner a modificar.
     * @param value   El valor a seleccionar.
     */
    private void setSpinnerValue(Spinner spinner, String value) {
        if (value == null) return;

        @SuppressWarnings("unchecked")
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();

        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }


    /**
     * Guarda los cambios realizados en la receta y actualiza los datos en Firebase.
     */
    private void guardarCambios() {
        try {
            // Recoge los valores de los campos de texto
            receta.setName(etName.getText().toString());
            receta.setDescription(etDescription.getText().toString());
            receta.setIngredients(etIngredients.getText().toString());
            receta.setPreparationTime(Integer.parseInt(etPreparationTime.getText().toString()));
            receta.setPrice(etPrice.getText().toString());
            receta.setRating(Double.parseDouble(etRating.getText().toString()));
            receta.setCategory(spCategory.getSelectedItem().toString());

            // Referencia a Firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference()
                    .child("Recetas")
                    .child(userId)
                    .child(receta.getId());


            // Crea un mapa con los datos actualizados
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", receta.getName());
            updates.put("description", receta.getDescription());
            updates.put("ingredients", receta.getIngredients());
            updates.put("preparationTime", receta.getPreparationTime());
            updates.put("price", receta.getPrice());
            updates.put("rating", receta.getRating());

            // Actualiza los datos en Firebase
            recipeRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditRecipeActivity.this,
                                "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(EditRecipeActivity.this,
                                    "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
                    );

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Hay campos numéricos con valores no válidos.", Toast.LENGTH_SHORT).show();
        }
    }
}
