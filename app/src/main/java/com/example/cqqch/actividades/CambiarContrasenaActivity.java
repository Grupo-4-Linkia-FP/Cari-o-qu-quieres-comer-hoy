package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cqqch.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Actividad que permite al usuario cambiar su contraseña.
 * Verifica la contraseña actual, autentica al usuario y actualiza la contraseña en Firebase Authentication.
 */
public class CambiarContrasenaActivity extends AppCompatActivity {

    // Campos de entrada de contraseña
    private EditText currentPasswordField, newPasswordField;
    // Botón para confirmar el cambio de contraseña
    private Button btnChangePassword;

    /**
     * Método llamado al crear la actividad.
     * Inicializa las vistas y configura el listener para cambiar la contraseña.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        // Vincular vistas del diseño
        currentPasswordField = findViewById(R.id.current_password_field);
        newPasswordField = findViewById(R.id.new_password_field);
        btnChangePassword = findViewById(R.id.btn_change_password_action);

        // Configurar el evento "onClick" para el botón de cambio de contraseña
        btnChangePassword.setOnClickListener(v -> {
            // Obtener las contraseñas ingresadas por el usuario
            String currentPass = currentPasswordField.getText().toString().trim();
            String newPass = newPasswordField.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (currentPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el usuario autenticado actual
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Credenciales del usuario para reautenticación
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPass);
            // Reautenticar al usuario con la contraseña actual
            user.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> {
                        // Si la reautenticación es exitosa, actualizamos la contraseña
                        user.updatePassword(newPass)
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
