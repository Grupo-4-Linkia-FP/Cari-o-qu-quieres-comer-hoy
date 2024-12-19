package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * PerfilActivity muestra la información del usuario (email o nombre) y ofrece opciones:
 * Cambiar Contraseña, Mis Favoritos, Cambiar Foto de Perfil y Cerrar Sesión.
 */
public class PerfilActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private TextView usernameText;
    private ImageView profileImage;

    private LinearLayout btnChangePassword, btnFavorites, btnCerrarSesion, btnChangePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        // Infla el layout específico de perfil dentro del content_frame del layout base
        View perfilView = getLayoutInflater().inflate(R.layout.activity_perfil, findViewById(R.id.content_frame));

        setupNavigation();

        // Referencias a las vistas del layout perfil
        profileImage = perfilView.findViewById(R.id.profile_image);
        usernameText = perfilView.findViewById(R.id.username_text);
        btnChangePassword = perfilView.findViewById(R.id.btn_change_password);
        btnFavorites = perfilView.findViewById(R.id.btn_favorites_container);
        btnCerrarSesion = perfilView.findViewById(R.id.btn_cerrar_sesion);
        btnChangePhoto = perfilView.findViewById(R.id.btn_change_photo);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        loadUserProfile();
        setupButtons();
    }

    /**
     * Carga el perfil del usuario desde Realtime Database.
     * - Muestra el nombre (si existe en Realtime Database) o el email.
     * - Muestra la imagen de perfil si está guardada en el campo "profileImage" en Realtime Database.
     */
    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Por defecto muestra el email (por si no hay nombre en Realtime Database)
            usernameText.setText(user.getEmail());

            // Cargar datos desde Realtime Database
            database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Si existe el campo "name", lo mostramos en lugar del email
                        if (snapshot.hasChild("name")) {
                            String nombreUsuario = snapshot.child("name").getValue(String.class);
                            if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
                                usernameText.setText(nombreUsuario);
                            }
                        }

                        // Cargar la imagen de perfil si existe
                        if (snapshot.hasChild("profileImage")) {
                            String imageName = snapshot.child("profileImage").getValue(String.class);
                            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            if (imageResId != 0) {
                                profileImage.setImageResource(imageResId);
                            } else {
                                Log.w("PerfilActivity", "No se encontró el drawable para " + imageName);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("PerfilActivity", "Error al cargar los datos del usuario", error.toException());
                }
            });
        } else {
            usernameText.setText("Usuario no autenticado");
        }
    }

    /**
     * Configura las acciones de los botones:
     * - Cambiar Contraseña -> CambiarContrasenaActivity
     * - Mis Favoritos -> MisFavoritosActivity
     * - Cerrar Sesión -> Cierra sesión y va a IniciarSesion
     * - Cambiar Foto Perfil -> SelectImagenPerfilActivity
     */
    private void setupButtons() {
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, CambiarContrasenaActivity.class);
            startActivity(intent);
        });

        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MisFavoritosActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, SelectImagePerfilActivity.class);
            startActivity(intent);
        });
    }
}
