package com.example.cqqch.actividades;

import android.content.Intent;
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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRestaurantActivity extends BaseActivity {

    private EditText etNombre, etDireccion, etNota, etPrecios, etComentarios;
    private Spinner spCategoria, spSePuedeIr, spSePuedePedir;
    private Button btnGuardar;
    private DatabaseReference database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el layout específico dentro del content_frame
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        View addRestaurantView = getLayoutInflater().inflate(R.layout.activity_add_restaurant, contentFrame, true);

        // Configura la navegación
        setupNavigation();

        // Inicializa Firebase
        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Inicializa Google Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBXRYteELnxnpd4IZqpiuQUIdsgsPaFEBU");
        }

        // Vincula las vistas
        etNombre = addRestaurantView.findViewById(R.id.etNombreRestaurante);
        etDireccion = addRestaurantView.findViewById(R.id.etDireccionRestaurante);
        etNota = addRestaurantView.findViewById(R.id.etNotaRestaurante);
        spCategoria = addRestaurantView.findViewById(R.id.spCategoria);
        spSePuedeIr = addRestaurantView.findViewById(R.id.spSePuedeIr);
        spSePuedePedir = addRestaurantView.findViewById(R.id.spSePuedePedir);
        etPrecios = addRestaurantView.findViewById(R.id.etPrecios);
        etComentarios = addRestaurantView.findViewById(R.id.etComentarios);
        btnGuardar = addRestaurantView.findViewById(R.id.btnGuardarRestaurante);
        Button btnVolver = addRestaurantView.findViewById(R.id.btnVolverMenuRestaurante);

        // Configura los Spinners
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categorias_restaurantes, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> yesNoAdapter = ArrayAdapter.createFromResource(
                this, R.array.opciones_si_no, android.R.layout.simple_spinner_item);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSePuedeIr.setAdapter(yesNoAdapter);
        spSePuedePedir.setAdapter(yesNoAdapter);

        // Configura el campo de dirección para abrir el autocompletado
        etDireccion.setOnClickListener(v -> {
            List<Field> fields = Arrays.asList(Field.ID, Field.NAME, Field.ADDRESS, Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(this);
            startActivityForResult(intent, 100);
        });

        // Evento clic para guardar restaurante
        btnGuardar.setOnClickListener(v -> guardarRestaurante());

        // Evento clic para volver a la pantalla anterior sin guardar
        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                etDireccion.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarRestaurante() {
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtén los valores de los campos
        String nombre = etNombre.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String notaStr = etNota.getText().toString().trim();

        if (spCategoria.getSelectedItem() == null || spSePuedeIr.getSelectedItem() == null || spSePuedePedir.getSelectedItem() == null) {
            Toast.makeText(this, "Por favor, selecciona las opciones", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoria = spCategoria.getSelectedItem().toString();
        boolean sePuedeIr = spSePuedeIr.getSelectedItem().toString().equals("Si");
        boolean sePuedePedir = spSePuedePedir.getSelectedItem().toString().equals("Si");
        String precios = etPrecios.getText().toString().trim();
        String comentarios = etComentarios.getText().toString().trim();

        // Validar campos obligatorios
        if (nombre.isEmpty() || direccion.isEmpty() || notaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar nota
        double nota;
        try {
            nota = Double.parseDouble(notaStr);
            if (nota < 1 || nota > 5) {
                Toast.makeText(this, "La nota debe estar entre 1 y 5", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La nota debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar precio
        if (!precios.isEmpty()) {
            try {
                Double.parseDouble(precios);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Generar un ID único para el restaurante
        String userId = currentUser.getUid();
        String restaurantId = database.child("Restaurantes").child(userId).push().getKey();

        // Verificar que se generó el ID correctamente
        if (restaurantId == null) {
            Toast.makeText(this, "Error generando ID único", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear mapa de datos para guardar el restaurante
        Map<String, Object> restaurantData = new HashMap<>();
        restaurantData.put("id", restaurantId); // ID único generado
        restaurantData.put("name", nombre);
        restaurantData.put("nameLowerCase", nombre.toLowerCase());
        restaurantData.put("address", direccion);
        restaurantData.put("category", categoria);
        restaurantData.put("price", precios);
        restaurantData.put("rating", nota);
        restaurantData.put("comment", comentarios);
        restaurantData.put("isFavorite", false);
        restaurantData.put("canGo", sePuedeIr);
        restaurantData.put("canOrder", sePuedePedir);

        // Guardar en Firebase
        database.child("Restaurantes").child(userId).child(restaurantId).setValue(restaurantData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Restaurante guardado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al guardar el restaurante", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Fallo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
