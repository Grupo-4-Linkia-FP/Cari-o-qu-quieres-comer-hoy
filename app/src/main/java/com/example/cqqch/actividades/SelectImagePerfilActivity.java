package com.example.cqqch.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Actividad para permitir a los usuarios seleccionar una imagen de perfil.
 * Actualiza la imagen de perfil seleccionada en Firebase Realtime Database.
 */
public class SelectImagePerfilActivity extends BaseActivity {

    // Elementos de imagen que el usuario puede seleccionar
    ImageView img1, img2, img3, img4;

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, la navegación y los eventos de selección de imagen.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico para la selección de imagen dentro del marco base
        View perfilView = getLayoutInflater().inflate(R.layout.activity_select_image_perfil, findViewById(R.id.content_frame), true);

        // Configura la navegación lateral heredada de BaseActivity
        setupNavigation();

        // Vincula los elementos de imagen del diseño
        img1 = perfilView.findViewById(R.id.img_perfil_1);
        img2 = perfilView.findViewById(R.id.img_perfil_2);
        img3 = perfilView.findViewById(R.id.img_perfil_3);
        img4 = perfilView.findViewById(R.id.img_perfil_4);

        // Asigna un evento de clic a cada imagen para seleccionar la imagen de perfil
        img1.setOnClickListener(v -> selectImage("img_perfil_m1"));
        img2.setOnClickListener(v -> selectImage("img_perfil_m2"));
        img3.setOnClickListener(v -> selectImage("img_perfil_f1"));
        img4.setOnClickListener(v -> selectImage("img_perfil_f2"));
    }

    /**
     * Método para actualizar la imagen de perfil seleccionada en Firebase.
     *
     * @param imageName Nombre de la imagen seleccionada (corresponde a un recurso o referencia en Firebase).
     */
    private void selectImage(String imageName) {
        // Obtiene el usuario autenticado actualmente
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Verifica si hay un usuario autenticado
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtiene la referencia al nodo del usuario en Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid());

        // Actualiza el campo "profileImage" con el nombre de la imagen seleccionada
        userRef.child("profileImage").setValue(imageName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SelectImagePerfilActivity.this, "Imagen de perfil actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SelectImagePerfilActivity.this, "Error al actualizar la imagen", Toast.LENGTH_SHORT).show();
                });
    }
}
