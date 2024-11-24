package com.example.cqqch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistrarse;
    private TextView btnIniciarSesion;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Usa tu layout de la pantalla de inicio

        // Vincula los botones del layout
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Configura el evento de clic para el botón de registrarse
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NuevoUsuario.class);
                startActivity(intent);
            }
        });

        // Configura el evento de clic para el botón de iniciar sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IniciarSesion.class);
                startActivity(intent);
            }
        });
    }
}
