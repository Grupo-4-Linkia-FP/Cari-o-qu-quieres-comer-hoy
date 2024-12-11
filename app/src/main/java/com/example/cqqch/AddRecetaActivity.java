package com.example.cqqch;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRecetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configura el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Configura la navegación
        setupNavigation();

        // Infla el contenido específico de esta actividad
        getLayoutInflater().inflate(R.layout.activity_cocinar, findViewById(R.id.content_frame));

        // Configura los botones específicos
        setupCocinarActivity();

    }

    private void setupCocinarActivity() {
    }

    private void setupNavigation() {
    }
}