package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    private Button btnRegistrarse;
    private TextView btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del layout
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Define el texto con HTML para hacer parte del texto rojo
        String htmlText = "Ya tengo cuenta. <font color='#E53935'>Iniciar sesión</font>";
        btnIniciarSesion.setText(Html.fromHtml(htmlText));

        // Configura el evento de clic para el botón de registrarse
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!esEmailValido(email)) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
                } else if (!esPasswordValida(password)) {
                    Toast.makeText(MainActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    registerUsuario(email, password);
                }
            }
        });

        // Configura el evento de clic para el texto de iniciar sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad de iniciar sesión
                Intent intent = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(intent);
            }
        });
    }

    private boolean esEmailValido(String email) {
        // Utiliza el patrón de Android para validar correos electrónicos
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean esPasswordValida(String password) {
        // Comprueba que la contraseña tenga al menos 6 caracteres
        return password.length() >= 6;
    }

    private void registerUsuario(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Registro exitoso, bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        // Redirige al menú principal
                        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
