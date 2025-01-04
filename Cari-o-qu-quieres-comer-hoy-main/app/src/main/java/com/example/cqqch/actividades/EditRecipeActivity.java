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

public class EditRecipeActivity extends BaseActivity {

    // Modelo
    private Receta receta;

    // Vistas
    private EditText etName, etDescription, etIngredients, etPreparationTime, etPrice, etRating;
    private Spinner spCategory;
    private Button btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Usamos un layout base que contiene un FrameLayout para “inyectar” nuestra UI
        setContentView(R.layout.activity_base);

        // 2) Inflamos el layout específico en el FrameLayout (content_frame)
        getLayoutInflater().inflate(R.layout.activity_edit_recipe, findViewById(R.id.content_frame));

        // 3) Configuramos la navegación lateral (Drawer, si corresponde)
        setupNavigation();

        // 4) Inicializamos las vistas
        setupEditRecipeActivity();

        // 5) Obtenemos la receta del intent
        receta = getIntent().getParcelableExtra("receta");
        if (receta == null) {
            Toast.makeText(this, "Error al cargar la receta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 6) Cargamos los datos de la receta en los campos
        cargarDatosReceta();
    }

    private void setupEditRecipeActivity() {
        // EditTexts
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etIngredients = findViewById(R.id.etIngredients);
        etPreparationTime = findViewById(R.id.etPreparationTime);
        etPrice = findViewById(R.id.etPrice);
        etRating = findViewById(R.id.etRating);

        // Spinner Categoría
        spCategory = findViewById(R.id.spCategory);

        // Botón Guardar
        btnGuardar = findViewById(R.id.btnSave);

        // Configuramos el spinner y el botón
        configurarSpinnerCategoria();
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void configurarSpinnerCategoria() {
        // Puedes tener un array en res/values/arrays.xml (por ejemplo, R.array.categorias_recetas)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_recetas, // EJEMPLO: "Postres", "Ensaladas", "Carnes"...
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void cargarDatosReceta() {
        etName.setText(receta.getName());
        etDescription.setText(receta.getDescription());
        etIngredients.setText(receta.getIngredients());
        etPreparationTime.setText(String.valueOf(receta.getPreparationTime()));
        etPrice.setText(receta.getPrice());
        etRating.setText(String.valueOf(receta.getRating()));


        // Asignar la categoría actual en el spinner
        setSpinnerValue(spCategory, receta.getCategory());
    }

    /**
     * Utilidad para seleccionar el valor correspondiente en un Spinner
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


    private void guardarCambios() {
        try {
            // Recogemos los valores editados
            receta.setName(etName.getText().toString());
            receta.setDescription(etDescription.getText().toString());
            receta.setIngredients(etIngredients.getText().toString());
            // Convertimos a int
            receta.setPreparationTime(Integer.parseInt(etPreparationTime.getText().toString()));
            receta.setPrice(etPrice.getText().toString());
            // Convertimos a double
            receta.setRating(Double.parseDouble(etRating.getText().toString()));
            receta.setCategory(spCategory.getSelectedItem().toString());

            // Actualizamos en Firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Usamos un ID único
            DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference()
                    .child("Recetas")
                    .child(userId)
                    .child(receta.getId());


            // Preparamos los campos a actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", receta.getName());
            updates.put("description", receta.getDescription());
            updates.put("ingredients", receta.getIngredients());
            updates.put("preparationTime", receta.getPreparationTime());
            updates.put("price", receta.getPrice());
            updates.put("rating", receta.getRating());
            // Si tienes más campos (isFavorite, etc.), inclúyelos también

            // Realizamos la actualización
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
