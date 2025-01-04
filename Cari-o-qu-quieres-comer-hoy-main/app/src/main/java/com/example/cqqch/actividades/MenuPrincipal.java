package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * MenuPrincipal es la actividad principal que muestra un saludo y
 * varios botones (IR, COCINAR, PEDIR, ELIGE TÚ) para navegar a otras actividades.
 * <p>
 * Esta clase hereda de BaseActivity, por lo que usa un layout base (activity_base)
 * y dentro de él infla el layout específico (menu_principal).
 */
public class MenuPrincipal extends BaseActivity {

    private TextView titleText; // Referencia al TextView que mostrará el saludo
    private LinearLayout btnIr, btnCocinar, btnPedir, btnEligeTu; // Botones de navegación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Primero: establece el layout base
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Infla el contenido específico para esta actividad en el content_frame del layout base
        View menuPrincipalView = getLayoutInflater().inflate(R.layout.menu_principal, findViewById(R.id.content_frame));

        // Configura la navegación (drawer, bottom navigation) definida en BaseActivity
        setupNavigation();

        // Obtén referencias a las vistas del layout inflado
        titleText = menuPrincipalView.findViewById(R.id.title_text);
        btnIr = menuPrincipalView.findViewById(R.id.btn_ir_container);
        btnCocinar = menuPrincipalView.findViewById(R.id.btn_cocinar_container);
        btnPedir = menuPrincipalView.findViewById(R.id.btn_pedir_container);
        btnEligeTu = menuPrincipalView.findViewById(R.id.btn_elige_tu_container);

        // Carga el nombre del usuario y actualiza el saludo
        loadUserNameAndSetGreeting();

        // Configura los listeners de los botones
        setupButtons();
    }

    /**
     * Carga el nombre del usuario desde Realtime Database y actualiza el título con "¡Hola [nombre]!"
     * Si no se encuentra el nombre, deja el texto por defecto.
     */
    private void loadUserNameAndSetGreeting() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Si no hay usuario autenticado, no se puede cargar el nombre
            titleText.setText("¡Hola! ¿Qué quieres comer hoy?");
            return;
        }

        String uid = user.getUid();

        // Obtén la referencia a /users/uid/name en la Realtime Database
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombreUsuario = snapshot.getValue(String.class);
                        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                            // Si hay nombre, lo usamos en el saludo
                            titleText.setText("¡Hola " + nombreUsuario + "! ¿Qué quieres comer hoy?");
                        } else {
                            // Si no hay nombre, texto por defecto
                            titleText.setText("¡Hola! ¿Qué quieres comer hoy?");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Si hay un error al leer el nombre, muestra un toast y deja el texto por defecto
                        Toast.makeText(MenuPrincipal.this, "Error al cargar el nombre del usuario", Toast.LENGTH_SHORT).show();
                        titleText.setText("¡Hola! ¿Qué quieres comer hoy?");
                    }
                });
    }

    /**
     * Configura la acción de los botones para navegar a las distintas actividades:
     * IR -> IrActivity
     * COCINAR -> CocinarActivity
     * PEDIR -> PedirActivity
     * ELIGE TÚ -> EligeTuActivity
     */
    private void setupButtons() {
        // Botón IR
        btnIr.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón IR presionado");
            Intent intent = new Intent(MenuPrincipal.this, IrActivity.class);
            startActivity(intent);
        });

        // Botón COCINAR
        btnCocinar.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón COCINAR presionado");
            Intent intent = new Intent(MenuPrincipal.this, CocinarActivity.class);
            startActivity(intent);
        });

        // Botón PEDIR
        btnPedir.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón PEDIR presionado");
            Intent intent = new Intent(MenuPrincipal.this, PedirActivity.class);
            startActivity(intent);
        });

        // Botón ELIGE TÚ
        btnEligeTu.setOnClickListener(v -> {
            Log.d("MenuPrincipal", "Botón ELIGE TÚ presionado");
            Intent intent = new Intent(MenuPrincipal.this, EligeTuActivity.class);
            startActivity(intent);
        });
    }
}
