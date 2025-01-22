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
import com.example.cqqch.modelos.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Actividad que permite editar los detalles de un restaurante existente.
 * Carga la información de un objeto `Restaurant` y guarda las actualizaciones
 * en Firebase Realtime Database.
 */
public class EditRestaurantActivity extends BaseActivity {

    // Modelo del restaurante
    private Restaurant restaurant;
    // Elementos de la interfaz
    private EditText etName, etAddress, etPrice, etComment, etRating;
    private Spinner spCategory, spCanGo, spCanOrder;
    private Button btnGuardar;

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, inicializa las vistas y carga los datos del restaurante.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico dentro del diseño base
        getLayoutInflater().inflate(R.layout.activity_edit_restaurant, findViewById(R.id.content_frame));

        // Configura la barra de navegación (si corresponde)
        setupNavigation();

        // Inicializa las vistas de la actividad
        setupEditRestaurantActivity();

        // Recupera el objeto Restaurant del intent
        restaurant = getIntent().getParcelableExtra("restaurant");

        if (restaurant == null) {
            Toast.makeText(this, "Error al cargar el restaurante", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Carga los datos del restaurante en los campos de la interfaz
        cargarDatosRestaurante();
    }

    /**
     * Inicializa las vistas de la actividad y configura los spinners y el botón de guardar.
     */
    private void setupEditRestaurantActivity() {

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        spCategory = findViewById(R.id.spCategory);
        etPrice = findViewById(R.id.etPrice);
        etRating = findViewById(R.id.etRating);
        etComment = findViewById(R.id.etComment);
        spCanGo = findViewById(R.id.spCanGo);
        spCanOrder = findViewById(R.id.spCanOrder);
        btnGuardar = findViewById(R.id.btnSave);

        // Configura los spinners de categorías y opciones
        configurarSpinners();

        // Configura el listener para el botón de guardar
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    /**
     * Configura los spinners para categorías, "Se puede ir" y "Se puede pedir".
     */
    private void configurarSpinners() {
        // Categorías
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_restaurantes,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        // Se puede ir
        ArrayAdapter<CharSequence> canGoAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_si_no,
                android.R.layout.simple_spinner_item
        );
        canGoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanGo.setAdapter(canGoAdapter);

        // Opciones "Sí" o "No"
        ArrayAdapter<CharSequence> canOrderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_si_no,
                android.R.layout.simple_spinner_item
        );
        canOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanOrder.setAdapter(canOrderAdapter);
    }

    /**
     * Establece un valor específico en un Spinner.
     *
     * @param spinner Spinner a modificar.
     * @param value   Valor que debe seleccionarse.
     */
    private void setSpinnerValue(Spinner spinner, String value) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    /**
     * Carga los datos del objeto `Restaurant` en los campos de la interfaz.
     */
    private void cargarDatosRestaurante() {
        etName.setText(restaurant.getName());
        etAddress.setText(restaurant.getAddress());
        etPrice.setText(restaurant.getPrice());
        etRating.setText(String.valueOf(restaurant.getRating()));
        etComment.setText(restaurant.getComment());

        setSpinnerValue(spCategory, restaurant.getCategory());
        setSpinnerValue(spCanGo, restaurant.isCanGo() ? "Si" : "No");
        setSpinnerValue(spCanOrder, restaurant.isCanOrder() ? "Si" : "No");
    }

    /**
     * Guarda los cambios realizados en el restaurante y actualiza los datos en Firebase.
     */
    private void guardarCambios() {
        try {
            // Actualiza los campos en el objeto local
            restaurant.setName(etName.getText().toString().trim());
            restaurant.setAddress(etAddress.getText().toString().trim());
            restaurant.setCategory(spCategory.getSelectedItem().toString());
            restaurant.setPrice(etPrice.getText().toString().trim());
            restaurant.setRating(Double.parseDouble(etRating.getText().toString().trim()));
            restaurant.setComment(etComment.getText().toString().trim());
            restaurant.setCanGo(spCanGo.getSelectedItem().toString().equals("Si"));
            restaurant.setCanOrder(spCanOrder.getSelectedItem().toString().equals("Si"));

            // Validación: Verifica que el ID del restaurante exista
            if (restaurant.getId() == null || restaurant.getId().isEmpty()) {
                Toast.makeText(this, "No se puede actualizar: ID inexistente.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Referencia a Firebase
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference()
                    .child("Restaurantes")
                    .child(userId)
                    .child(restaurant.getId());

            // Preparamos un mapa con los campos a actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("id", restaurant.getId());            // ID inmutable
            updates.put("name", restaurant.getName());
            updates.put("address", restaurant.getAddress());
            updates.put("category", restaurant.getCategory());
            updates.put("price", restaurant.getPrice());
            updates.put("rating", restaurant.getRating());
            updates.put("comment", restaurant.getComment());
            updates.put("canGo", restaurant.isCanGo());
            updates.put("canOrder", restaurant.isCanOrder());
            updates.put("favorite", restaurant.isFavorite()); // Si tu modelo lo usa

            // Actualiza los datos en Firebase
            restaurantRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
                    );

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, introduce un valor válido para la nota.", Toast.LENGTH_SHORT).show();
        }
    }
}
