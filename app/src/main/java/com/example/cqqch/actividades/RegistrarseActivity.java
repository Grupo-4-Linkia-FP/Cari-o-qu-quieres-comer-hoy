package com.example.cqqch.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cqqch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button btnRegistrarse;
    private TextView btnIniciarSesion;
    private EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del layout de registro
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Ajusta el texto con HTML si lo deseas
        String htmlText = "Ya tengo cuenta. <font color='#E53935'>Iniciar sesión</font>";
        btnIniciarSesion.setText(Html.fromHtml(htmlText));

        // Evento click para el botón de registrarse
        btnRegistrarse.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

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

        // Evento click para ir a Iniciar Sesión
        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrarseActivity.this, IniciarSesion.class);
            startActivity(intent);
        });
    }

    private boolean esEmailValido(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean esPasswordValida(String password) {
        return password.length() >= 6;
    }

    private void registerUsuario(String email, String password) {
        String name = nameField.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

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
