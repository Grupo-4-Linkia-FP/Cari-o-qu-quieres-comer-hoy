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

/**
 * Actividad para mostrar la política de privacidad de la aplicación.
 * La política de privacidad se carga desde un archivo de texto almacenado en `res/raw`.
 */
public class PoliticaPrivacidadActivity extends AppCompatActivity {

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, define el tema según el sistema y carga el contenido de la política de privacidad.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuración del tema: Sigue el modo del sistema (oscuro o claro)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // Establece el diseño de la actividad
        setContentView(R.layout.activity_politica_privacidad);

        // Vincula la vista del TextView donde se mostrará la política de privacidad
        TextView tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);

        // Carga el contenido de la política de privacidad desde un archivo de texto en res/raw
        try {
            InputStream is = getResources().openRawResource(R.raw.privacy_policy);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            // Lee cada línea del archivo y las concatena
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            tvPrivacyPolicy.setText(builder.toString());
        } catch (IOException e) {
            // En caso de error, muestra un mensaje de error en el TextView
            tvPrivacyPolicy.setText("Error al cargar la política de privacidad.");
            e.printStackTrace();
        }
    }
}
