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

public class EditRestaurantActivity extends BaseActivity {

    private Restaurant restaurant;
    private EditText etName, etAddress, etPrice, etComment, etRating;
    private Spinner spCategory, spCanGo, spCanOrder;
    private Button btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // 1) Infla el layout específico dentro de activity_base
        getLayoutInflater().inflate(R.layout.activity_edit_restaurant, findViewById(R.id.content_frame));

        // 2) Configura la navegación lateral
        setupNavigation();

        // 3) Inicializa vistas y spinners
        setupEditRestaurantActivity();

        // 4) Recupera el objeto Restaurant del intent
        restaurant = getIntent().getParcelableExtra("restaurant");

        if (restaurant == null) {
            Toast.makeText(this, "Error al cargar el restaurante", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 5) Carga los datos en la UI
        cargarDatosRestaurante();
    }

    private void setupEditRestaurantActivity() {
        // Vistas
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        spCategory = findViewById(R.id.spCategory);
        etPrice = findViewById(R.id.etPrice);
        etRating = findViewById(R.id.etRating);
        etComment = findViewById(R.id.etComment);
        spCanGo = findViewById(R.id.spCanGo);
        spCanOrder = findViewById(R.id.spCanOrder);
        btnGuardar = findViewById(R.id.btnSave);

        // Configuramos spinners
        configurarSpinners();

        // Listener para el botón Guardar
        btnGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void configurarSpinners() {
        // Categoría
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

        // Se puede pedir
        ArrayAdapter<CharSequence> canOrderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_si_no,
                android.R.layout.simple_spinner_item
        );
        canOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanOrder.setAdapter(canOrderAdapter);
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    /**
     * Rellena la UI con los datos del objeto restaurant.
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
     * Actualiza el registro del restaurante en Firebase, sin crear uno nuevo.
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

            // Validación mínima
            if (restaurant.getId() == null || restaurant.getId().isEmpty()) {
                Toast.makeText(this, "No se puede actualizar: ID inexistente.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtenemos el userId de FirebaseAuth
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // NOTA: Usamos 'restaurant.getId()' como clave para no crear un nodo nuevo
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

            // Actualizamos en Firebase
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
