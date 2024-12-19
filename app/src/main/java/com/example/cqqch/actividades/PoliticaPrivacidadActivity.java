package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cqqch.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.appcompat.app.AppCompatDelegate;

public class PoliticaPrivacidadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuración para seguir el tema del sistema
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_politica_privacidad);

        TextView tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);

        // Leer el archivo de texto de res/raw
        try {
            InputStream is = getResources().openRawResource(R.raw.privacy_policy);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            tvPrivacyPolicy.setText(builder.toString());
        } catch (IOException e) {
            tvPrivacyPolicy.setText("Error al cargar la política de privacidad.");
            e.printStackTrace();
        }
    }
}
