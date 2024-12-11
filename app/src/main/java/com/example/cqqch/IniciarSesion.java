package com.example.cqqch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button btnIniciarSesion;
    private TextView forgotPasswordLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del diseño
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        forgotPasswordLink = findViewById(R.id.forgot_password);

        // Configura el botón de inicio de sesión
        btnIniciarSesion.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (validateInputs(email, password)) {
                loginUsuario(email, password);
            }
        });

        // Configura el enlace de "¿Olvidaste tu contraseña?"
        forgotPasswordLink.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(IniciarSesion.this, "Por favor ingresa tu correo electrónico para recuperar tu contraseña", Toast.LENGTH_SHORT).show();
            } else if (!esEmailValido(email)) {
                Toast.makeText(IniciarSesion.this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            } else {
                recuperarPassword(email);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!esEmailValido(email)) {
            Toast.makeText(this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!esPasswordValida(password)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean esEmailValido(String email) {
        // Valida el formato del correo electrónico
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean esPasswordValida(String password) {
        // Valida que la contraseña tenga al menos 6 caracteres
        return password.length() >= 6;
    }

    private void loginUsuario(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Login exitoso, bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            // Redirige al menú principal
                            Intent intent = new Intent(IniciarSesion.this, MenuPrincipal.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual
                        }
                    } else {
                        manejarErroresDeAutenticacion(task.getException());
                    }
                });
    }

    private void manejarErroresDeAutenticacion(Exception exception) {
        String errorMessage;
        if (exception instanceof FirebaseAuthInvalidUserException) {
            errorMessage = "No existe el usuario";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMessage = "Credenciales incorrectas";
        } else {
            errorMessage = "Error en el login: " + (exception != null ? exception.getMessage() : "desconocido");
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void recuperarPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Correo de recuperación enviado a " + email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al enviar el correo de recuperación: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
