package com.example.cqqch.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cqqch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Actividad para registrar nuevos usuarios en la aplicación.
 * Utiliza Firebase Authentication para la autenticación y Firebase Realtime Database para almacenar datos adicionales como el nombre del usuario.
 */
public class RegistrarseActivity extends AppCompatActivity {

    // Firebase Auth
    private FirebaseAuth mAuth;

    // Vistas
    private EditText emailField, passwordField;
    private Button btnRegistrarse;
    private TextView btnIniciarSesion;
    private EditText nameField;

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, inicializa Firebase y define eventos para los botones.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula las vistas del diseño
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Configura el texto del enlace "Iniciar sesión" con formato HTML
        String htmlText = "Ya tengo cuenta. <font color='#E53935'>Iniciar sesión</font>";
        btnIniciarSesion.setText(HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        // Evento clic para registrarse
        btnRegistrarse.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Validaciones de entrada
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrarseActivity.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!esEmailValido(email)) {
                Toast.makeText(RegistrarseActivity.this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            } else if (!esPasswordValida(password)) {
                Toast.makeText(RegistrarseActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            } else {
                registerUsuario(email, password);
            }
        });

        // Evento clic para ir a la actividad de inicio de sesión
        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrarseActivity.this, IniciarSesion.class);
            startActivity(intent);
        });
    }

    /**
     * Valida si un correo electrónico tiene el formato correcto.
     *
     * @param email Correo electrónico a validar.
     * @return `true` si el correo es válido, `false` en caso contrario.
     */
    private boolean esEmailValido(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Valida si una contraseña cumple con los requisitos mínimos.
     *
     * @param password Contraseña a validar.
     * @return `true` si la contraseña tiene al menos 6 caracteres, `false` en caso contrario.
     */
    private boolean esPasswordValida(String password) {
        return password.length() >= 6;
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication y almacena su nombre en Firebase Realtime Database.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    private void registerUsuario(String email, String password) {
        String name = nameField.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario con correo y contraseña
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            // Guarda el nombre en Realtime Database
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(userId)
                                    .child("name")
                                    .setValue(name)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(RegistrarseActivity.this, "Registro exitoso, bienvenido " + name, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegistrarseActivity.this, MenuPrincipal.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegistrarseActivity.this, "Error al guardar nombre", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(RegistrarseActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
