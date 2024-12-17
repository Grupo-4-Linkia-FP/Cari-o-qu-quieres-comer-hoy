package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etUsuario, etContrasena;
    private Button btValidar, btNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula los elementos del layout
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btValidar = findViewById(R.id.btValidar);
        btNuevoUsuario = findViewById(R.id.btNuevoUsuario);

        // Configura el evento de clic para el botón de validar
        btValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el email y la contraseña ingresados
                String email = etUsuario.getText().toString().trim();
                String password = etContrasena.getText().toString().trim();
                //comprueba que los campos no estes vacios, si no lo están llama a la función login usuario
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    loginUsuario(email, password);
                }
            }
        });

        // Configura el evento de clic para el botón de nuevo usuario
        btNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre la actividad nuevo usuario
                Intent intent = new Intent(MainActivity.this, NuevoUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void loginUsuario(String email, String password) {
        //intenta iniciar sesión con el usuario y contraseña proporcionados
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login exitoso
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Login exitoso, bienvenido " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        // Abre el menu principal
                        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                        startActivity(intent);

                    } else {
                        // Si falla el login, muestra un mensaje de error y captura los errores para lanzar el mensaje correspondiente
                        String errorMessage;
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            errorMessage = "No existe el usuario";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            errorMessage = "Credenciales incorrectas";
                        } catch (Exception e) {
                            errorMessage = "Error en el login";
                        }
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}