package com.example.cqqch;

import android.os.Bundle;
import android.widget.Toast;

public class CocinarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Primero el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Configura navegación
        setupNavigation();

        // Infla el layout específico
        getLayoutInflater().inflate(R.layout.activity_cocinar, findViewById(R.id.content_frame));

        // Lógica específica
        setupCocinarActivity();
    }

    private void setupCocinarActivity() {
        Toast.makeText(this, "Bienvenido a Cocinar", Toast.LENGTH_SHORT).show();
    }
}

