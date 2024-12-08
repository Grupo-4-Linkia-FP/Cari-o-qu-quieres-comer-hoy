package com.example.cqqch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class MenuPrincipal extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Usa el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Infla el contenido específico de MenuPrincipal
        getLayoutInflater().inflate(R.layout.menu_principal, findViewById(R.id.content_frame));

        // Configura navegación
        setupNavigation();

        // Configura los botones de MenuPrincipal
        setupButtons();
    }

    private void setupButtons() {
        // Botón IR
        LinearLayout btnIr = findViewById(R.id.btn_ir_container);
        btnIr.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón IR presionado");
            Intent intent = new Intent(MenuPrincipal.this, IrActivity.class);
            startActivity(intent); // Navegar a IrActivity
        });

        // Botón COCINAR
        LinearLayout btnCocinar = findViewById(R.id.btn_cocinar_container);
        btnCocinar.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón COCINAR presionado");
            Intent intent = new Intent(MenuPrincipal.this, CocinarActivity.class);
            startActivity(intent);
        });

        // Botón PEDIR
        LinearLayout btnPedir = findViewById(R.id.btn_pedir_container);
        btnPedir.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón PEDIR presionado");
            Intent intent = new Intent(MenuPrincipal.this, PedirActivity.class);
            startActivity(intent);
        });

        // Botón ELIGE TÚ
        LinearLayout btnEligeTu = findViewById(R.id.btn_elige_tu_container);
        btnEligeTu.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón ELIGE TÚ presionado");
            Intent intent = new Intent(MenuPrincipal.this, EligeTuActivity.class);
            startActivity(intent);
        });
    }
}
