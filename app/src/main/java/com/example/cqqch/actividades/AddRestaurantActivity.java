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
import com.example.cqqch.modelos.Restaurant;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRestaurantActivity extends BaseActivity {

    private EditText etNombre, etDireccion, etNota, etPrecios, etComentarios;
    private Spinner spCategoria, spSePuedeIr, spSePuedePedir;
    private Button btnGuardar;
    private DatabaseReference database;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Primero llama a super, luego setContentView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Inflar el layout secundario dentro del frame del layout base
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        View addRestaurantView = getLayoutInflater().inflate(R.layout.activity_add_restaurant, contentFrame, true);

        // Llama a setupNavigation() después de inflar el layout
        setupNavigation();

        // Inicializa Firebase
        database = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Vincula los elementos del layout
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

        // Asignar adaptadores a los Spinners (asegúrate de tener los arrays en res/values/arrays.xml)
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categorias_restaurantes, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> yesNoAdapter = ArrayAdapter.createFromResource(
                this, R.array.opciones_si_no, android.R.layout.simple_spinner_item);
        yesNoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSePuedeIr.setAdapter(yesNoAdapter);
        spSePuedePedir.setAdapter(yesNoAdapter);

        // Evento clic para guardar restaurante
        btnGuardar.setOnClickListener(v -> guardarRestaurante());

        //Evento clic para volver a la pantalla anterior sin guardar
        btnVolver.setOnClickListener(v -> finish());

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

        // Primero verifica que spinners no estén vacíos y tengan una selección
        if (spCategoria.getSelectedItem() == null
                || spSePuedeIr.getSelectedItem() == null
                || spSePuedePedir.getSelectedItem() == null) {
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

        // Crea el restaurante
        Restaurant restaurante = new Restaurant(
                nombre,
                direccion,
                categoria,
                precios,
                nota,
                comentarios,
                false, // Por defecto, no es favorito
                sePuedeIr,
                sePuedePedir
        );

        // Guarda en la base de datos con `push()`
        String userId = currentUser.getUid();
        database.child("Restaurantes").child(userId).push().setValue(restaurante)
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
