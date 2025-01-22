package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cqqch.R;

/**
 * Esta actividad muestra la pantalla "Acerca de" en la aplicación.
 * Incluye un botón para volver a la pantalla anterior.
 */
public class AcercaDeActivity extends AppCompatActivity {

    /**
     * Método llamado cuando la actividad es creada.
     * Configura la vista y el botón "Volver".
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        // Configuración del botón de Volver
        Button btnBack = findViewById(R.id.btn_back);
        // Configura el evento "onClick" para el botón.
        // Cuando el botón es pulsado, la actividad actual se cierra
        // y vuelve a la pantalla anterior.
        btnBack.setOnClickListener(v -> finish());
    }
}
