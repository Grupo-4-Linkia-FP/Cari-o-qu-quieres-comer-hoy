package com.example.cqqch.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import com.example.cqqch.R;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistrarse;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si el usuario ya inició sesión
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Redirigir directamente a la pantalla principal
            startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
            finish(); // Evita que el usuario regrese a esta pantalla
            return;
        }

        setContentView(R.layout.activity_main);

        // Vincula los elementos del layout
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Evento click para ir a la pantalla de registro
        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
            startActivity(intent);
        });

        // Evento click para ir a la pantalla de iniciar sesión
        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IniciarSesion.class);
            startActivity(intent);
        });
    }
}
