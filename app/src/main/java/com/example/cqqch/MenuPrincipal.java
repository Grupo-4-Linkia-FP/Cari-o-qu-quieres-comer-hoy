package com.example.cqqch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MenuPrincipal extends AppCompatActivity {

    // Variables
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        // Inicializar los elementos del layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuButton = findViewById(R.id.menu_button);

        // Configurar el evento de clic para el botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir o cerrar el menú lateral
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        // Configurar la navegación para las opciones del menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_perfil) {
                    Intent profileIntent = new Intent(MenuPrincipal.this, PerfilActivity.class);
                    startActivity(profileIntent);
                } else if (id == R.id.nav_favoritos) {
                    Intent favoritesIntent = new Intent(MenuPrincipal.this, FavoritosActivity.class);
                    startActivity(favoritesIntent);
                } else if (id == R.id.nav_cerrar_sesion) {
                    cerrarSesion();
                } else {
                    Log.d("MenuPrincipal", "Elemento del menú no reconocido: " + item.getTitle());
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }

    /**
     * Método para manejar el cierre de sesión
     */
    private void cerrarSesion() {
        // Aquí puedes agregar lógica para limpiar datos o cerrar sesión en el servidor
        Log.d("MenuPrincipal", "Cerrando sesión...");

        // Redirigir al usuario a la pantalla de inicio de sesión
        Intent loginIntent = new Intent(MenuPrincipal.this, IniciarSesion.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(loginIntent);
        finish(); // Finalizar la actividad actual
    }
}
