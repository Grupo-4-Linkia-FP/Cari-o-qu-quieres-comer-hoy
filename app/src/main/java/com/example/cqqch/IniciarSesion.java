package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button btnIniciarSesion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion); // Verifica que iniciar_sesion.xml esté correctamente en res/layout/

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del layout
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Verifica si las vistas no son nulas (evitar problemas si hay errores en el ID)
        if (emailField == null || passwordField == null || btnIniciarSesion == null) {
            Toast.makeText(this, "Error al vincular los elementos del diseño. Revisa los IDs.", Toast.LENGTH_SHORT).show();
            return; // Salir del método si hay un error al vincular las vistas
        }

        // Configura el evento de clic para el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(IniciarSesion.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!esEmailValido(email)) {
                    Toast.makeText(IniciarSesion.this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
                } else if (!esPasswordValida(password)) {
                    Toast.makeText(IniciarSesion.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    loginUsuario(email, password);
                }
            }
        });
    }

    private boolean esEmailValido(String email) {
        // Valida que el email tenga un formato correcto
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean esPasswordValida(String password) {
        // Comprueba que la contraseña tenga al menos 6 caracteres
        return password.length() >= 6;
    }

    private void loginUsuario(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(IniciarSesion.this, "Login exitoso, bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            // Redirige al menú principal
                            Intent intent = new Intent(IniciarSesion.this, MenuPrincipal.class);
                            startActivity(intent);
                        }
                    } else {
                        String errorMessage;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            errorMessage = "No existe el usuario";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Credenciales incorrectas";
                        } catch (Exception e) {
                            errorMessage = "Error en el login: " + (task.getException() != null ? task.getException().getMessage() : "desconocido");
                        }
                        Toast.makeText(IniciarSesion.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
