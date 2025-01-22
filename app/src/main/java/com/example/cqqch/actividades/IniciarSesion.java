package com.example.cqqch.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cqqch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * Actividad para manejar el inicio de sesión del usuario.
 * Incluye validación de credenciales, inicio de sesión y recuperación de contraseña.
 */
public class IniciarSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button btnIniciarSesion;
    private TextView forgotPasswordLink;

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz, inicializa Firebase y define las acciones de los botones.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciar_sesion);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula las vistas
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        forgotPasswordLink = findViewById(R.id.forgot_password);

        // Configura el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (validateInputs(email, password)) {
                loginUsuario(email, password);
            }
        });

        // Configura el enlace para recuperar contraseña
        forgotPasswordLink.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                showEmailInputDialog();
            } else if (!esEmailValido(email)) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            } else {
                recuperarPassword(email);
            }
        });
    }

    /**
     * Valida los campos de entrada de correo y contraseña.
     *
     * @param email Correo ingresado por el usuario.
     * @param password Contraseña ingresada por el usuario.
     * @return `true` si los campos son válidos, `false` en caso contrario.
     */
    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!esEmailValido(email)) {
            Toast.makeText(this, "Por favor ingresa un correo válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingresa tu contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Verifica si un correo tiene un formato válido.
     *
     * @param email Correo a validar.
     * @return `true` si el correo es válido, `false` en caso contrario.
     */
    private boolean esEmailValido(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     *
     * @param email Correo del usuario.
     * @param password Contraseña del usuario.
     */
    private void loginUsuario(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Guardar estado de sesión
                        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        // Redirigir al menú principal
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MenuPrincipal.class));
                        finish();
                    } else {
                        manejarErroresDeAutenticacion(task.getException());
                    }
                });
    }

    /**
     * Maneja los errores de autenticación y muestra mensajes al usuario.
     *
     * @param exception Excepción capturada durante el inicio de sesión.
     */
    private void manejarErroresDeAutenticacion(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidUserException) {
            Toast.makeText(this, "No existe una cuenta asociada a este correo", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra un cuadro de diálogo para ingresar el correo en caso de olvido de contraseña.
     */
    private void showEmailInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Contraseña");

        final EditText input = new EditText(this);
        input.setHint("Ingresa tu correo");
        builder.setView(input);

        builder.setPositiveButton("Enviar", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (!TextUtils.isEmpty(email) && esEmailValido(email)) {
                recuperarPassword(email);
            } else {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Envía un correo de recuperación de contraseña al usuario.
     *
     * @param email Correo del usuario.
     */
    private void recuperarPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
