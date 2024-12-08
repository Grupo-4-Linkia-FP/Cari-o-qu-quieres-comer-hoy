package com.example.cqqch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

public class PerfilActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Usamos el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Inflamos el contenido específico de PerfilActivity dentro del content_frame
        // Antes el perfil tenía un gradiente, prueba sin él para descartar duplicación:
        getLayoutInflater().inflate(R.layout.activity_perfil, findViewById(R.id.content_frame));

        // Configura los menús
        setupNavigation();

        // Configuración específica de PerfilActivity
        setupUserProfile();
    }

    private void setupUserProfile() {
        Toast.makeText(this, "Configurando perfil...", Toast.LENGTH_SHORT).show();
    }
}
