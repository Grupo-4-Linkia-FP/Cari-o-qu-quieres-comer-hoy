package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NuevoUsuario extends AppCompatActivity {
    private EditText etUsuario, etContrasena;
    private Button btnRegistrar;
    private FirebaseAuth mAuth;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_usuario);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del layout
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnRegistrar = findViewById(R.id.btNuevoUsuario); // Asegúrate de que este ID sea correcto
        floatingActionButton = findViewById(R.id.fabnuevousuario); // Vincula el FloatingActionButton

        // Configura el evento de clic para el botón de registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el email y la contraseña ingresados
                String email = etUsuario.getText().toString().trim();
                String password = etContrasena.getText().toString().trim();
                //Si están vacíos lanza un mensaje de error, si están completos lanza la función registerUser
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(NuevoUsuario.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password);
                }
            }
        });

        // Configura el evento de clic para retroceder
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuevoUsuario.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(String email, String password) {
        //crea un usuario en fire base con los datos que introducimos
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registro exitoso
                        Toast.makeText(NuevoUsuario.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Inicia MainActivity al registrarse con éxito
                        Intent intent = new Intent(NuevoUsuario.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Si falla el registro, muestra un mensaje de error
                        Toast.makeText(NuevoUsuario.this, "Error al registrarse: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}