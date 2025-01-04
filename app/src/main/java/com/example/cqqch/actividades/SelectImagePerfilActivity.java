package com.example.cqqch.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectImagePerfilActivity extends BaseActivity {

    ImageView img1, img2, img3, img4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();

        // Infla el layout específico de perfil dentro del content_frame del layout base
        View perfilView = getLayoutInflater().inflate(R.layout.activity_select_image_perfil, findViewById(R.id.content_frame), true);

        img1 = perfilView.findViewById(R.id.img_perfil_1);
        img2 = perfilView.findViewById(R.id.img_perfil_2);
        img3 = perfilView.findViewById(R.id.img_perfil_3);
        img4 = perfilView.findViewById(R.id.img_perfil_4);

        img1.setOnClickListener(v -> selectImage("img_perfil_m1"));
        img2.setOnClickListener(v -> selectImage("img_perfil_m2"));
        img3.setOnClickListener(v -> selectImage("img_perfil_f1"));
        img4.setOnClickListener(v -> selectImage("img_perfil_f2"));
    }

    private void selectImage(String imageName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the profile image in Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(user.getUid());

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
