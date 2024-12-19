package com.example.cqqch.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cqqch.R;
import com.example.cqqch.actividades.AcercaDeActivity;
import com.example.cqqch.actividades.AjustesActivity;
import com.example.cqqch.actividades.MisFavoritosActivity;
import com.example.cqqch.actividades.MainActivity;
import com.example.cqqch.actividades.MenuPrincipal;
import com.example.cqqch.actividades.PerfilActivity;
import com.example.cqqch.actividades.PoliticaPrivacidadActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

/**
 * BaseActivity sirve como clase base para otras actividades de la aplicación.
 * Proporciona funcionalidades comunes como la gestión del menú lateral y del menú inferior.
 */
public class BaseActivity extends AppCompatActivity {

    // Elementos comunes del diseño
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected BottomNavigationView bottomNavigationView;
    protected ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No se llama a setContentView() aquí, ya que las subclases lo hacen.
    }

    /**
     * Configura el menú lateral (navigation drawer) y el menú inferior (bottom navigation).
     * Este método debe ser llamado por todas las subclases después de establecer su diseño.
     */
    protected void setupNavigation() {
        // Inicializa el DrawerLayout y establece un color transparente para el scrim (sombra)
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        // Inicializa el menú lateral
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configura el comportamiento del menú lateral
        if (navigationView != null) {
            navigationView.setBackgroundColor(getResources().getColor(R.color.colorBackground, getTheme()));
            navigationView.setItemBackgroundResource(android.R.color.transparent);

            // Maneja los clics en los elementos del menú lateral
            navigationView.setNavigationItemSelectedListener(item -> {
                handleNavigationDrawerClick(item);
                return true;
            });

            // Configura los clics para "Política de Privacidad" y "Acerca de"
            TextView privacyPolicy = navigationView.findViewById(R.id.nav_privacy_policy);
            TextView about = navigationView.findViewById(R.id.nav_about);

            if (privacyPolicy != null) {
                privacyPolicy.setOnClickListener(v -> {
                    startActivity(new Intent(this, PoliticaPrivacidadActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }

            if (about != null) {
                about.setOnClickListener(v -> {
                    startActivity(new Intent(this, AcercaDeActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
            }
        }

        // Configura el comportamiento del menú inferior
        if (bottomNavigationView != null) {
            bottomNavigationView.setBackgroundColor(Color.TRANSPARENT);
            bottomNavigationView.setElevation(0f); // Elimina la sombra
            bottomNavigationView.setItemBackgroundResource(android.R.color.transparent);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                handleBottomNavigationClick(item); // Maneja los clics en los elementos del menú inferior
                return true;
            });
        }

        // Configura el botón del menú lateral para abrir/cerrar el DrawerLayout
        menuButton = findViewById(R.id.menu_button);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            });
        }
    }

    /**
     * Maneja los clics en los elementos del menú lateral.
     * Las subclases pueden sobrescribir este método para manejar elementos adicionales.
     *
     * @param item El elemento del menú que se seleccionó.
     */
    protected void handleNavigationDrawerClick(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
        } else if (id == R.id.nav_favoritos) {
            startActivity(new Intent(this, MisFavoritosActivity.class));
        } else if (id == R.id.nav_ajustes) {
            startActivity(new Intent(this, AjustesActivity.class));
        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion();
        } else if (id == R.id.nav_privacy_policy) {
            startActivity(new Intent(this, PoliticaPrivacidadActivity.class)); // Redirige a Política de Privacidad
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AcercaDeActivity.class)); // Redirige a Acerca de
        }
        // Cierra el menú lateral después de la selección
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Maneja los clics en los elementos del menú inferior.
     * Las subclases pueden sobrescribir este método para manejar acciones adicionales.
     *
     * @param item El elemento del menú que se seleccionó.
     */
    protected void handleBottomNavigationClick(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MenuPrincipal.class));
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, MisFavoritosActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AcercaDeActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, PerfilActivity.class));
        }
    }

    /**
     * Maneja el cierre de sesión.
     * Limpia el stack de actividades y redirige al usuario a la pantalla de inicio de sesión.
     */
    private void cerrarSesion() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
