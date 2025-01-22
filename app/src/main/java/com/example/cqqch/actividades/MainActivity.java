package com.example.cqqch.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import com.example.cqqch.R;

/**
 * Actividad principal que sirve como pantalla de inicio de la aplicación.
 * Proporciona opciones para registrarse o iniciar sesión, y redirige a la pantalla principal si el usuario ya ha iniciado sesión.
 */
public class MainActivity extends AppCompatActivity {

    // Botones para registrarse e iniciar sesión
    private Button btnRegistrarse;
    private Button btnIniciarSesion;

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz y define las acciones para los botones.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si el usuario ya inició sesión
        SharedPreferences sharedPreferences = getSharedPreferences("UsuarioPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Si el usuario ya inició sesión, redirige al menú principal
            startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
            finish(); // Evita que el usuario regrese a esta pantalla
            return;
        }

        // Configurar el diseño de la actividad
        setContentView(R.layout.activity_main);

        // Vincular los elementos del diseño
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Configurar el evento clic para el botón de registro
        btnRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarseActivity.class);
            startActivity(intent);
        });

        // Configurar el evento clic para el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IniciarSesion.class);
            startActivity(intent);
        });
    }
}
