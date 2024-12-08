package com.example.cqqch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected BottomNavigationView bottomNavigationView;
    protected ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No se llama a setContentView() aquí, ya que las subclases lo hacen.
    }

    protected void setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);

        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (navigationView != null) {
            navigationView.setBackgroundColor(Color.WHITE);
            navigationView.setItemBackgroundResource(android.R.color.transparent);

            navigationView.setNavigationItemSelectedListener(item -> {
                handleNavigationDrawerClick(item);
                return true;
            });
        }

        if (bottomNavigationView != null) {
            bottomNavigationView.setBackgroundColor(Color.TRANSPARENT);
            bottomNavigationView.setElevation(0f);
            bottomNavigationView.setItemBackgroundResource(android.R.color.transparent);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                handleBottomNavigationClick(item);
                return true;
            });
        }

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
     * Lógica por defecto para el navigation drawer lateral.
     */
    protected void handleNavigationDrawerClick(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
        } else if (id == R.id.nav_favoritos) {
            startActivity(new Intent(this, FavoritosActivity.class));
        } else if (id == R.id.nav_ajustes) {
            startActivity(new Intent(this, AjustesActivity.class));
        } else if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion();
        }
        // Cierra el menú lateral después de la selección
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * Lógica por defecto para el bottom navigation.
     */
    protected void handleBottomNavigationClick(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MenuPrincipal.class));
        } else if (id == R.id.nav_favorites) {
            startActivity(new Intent(this, FavoritosActivity.class));
        } else if (id == R.id.nav_search) {
            startActivity(new Intent(this, SearchActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, PerfilActivity.class));
        }
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, IniciarSesion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
