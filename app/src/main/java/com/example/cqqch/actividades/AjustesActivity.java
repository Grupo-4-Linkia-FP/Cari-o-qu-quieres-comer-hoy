package com.example.cqqch.actividades;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.cqqch.R;
import com.example.cqqch.base.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AjustesActivity extends BaseActivity {

    private CheckBox notificationsEnabled;
    private CheckBox notificationsMessages;
    private CheckBox notificationsRecommendations;
    private Button deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base); // Configurar la base
        super.onCreate(savedInstanceState);


        View acercaDeView = getLayoutInflater().inflate(R.layout.activity_ajustes, findViewById(R.id.content_frame)); // Inflar el layout específico

        setupNavigation(); // Configurar navegación de BaseActivity

        // Inicializar vistas
        notificationsEnabled = acercaDeView.findViewById(R.id.notifications_enabled);
        notificationsMessages = acercaDeView.findViewById(R.id.notifications_messages);
        notificationsRecommendations = acercaDeView.findViewById(R.id.notifications_recommendations);
        deleteAccountButton = acercaDeView.findViewById(R.id.delete_account_button);

        // Cargar preferencias guardadas
        loadPreferences();

        // Guardar cambios en las preferencias de notificación
        notificationsEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> savePreferences());
        notificationsMessages.setOnCheckedChangeListener((buttonView, isChecked) -> savePreferences());
        notificationsRecommendations.setOnCheckedChangeListener((buttonView, isChecked) -> savePreferences());

        // Configurar botón de eliminar cuenta
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        notificationsEnabled.setChecked(sharedPreferences.getBoolean("notifications_enabled", true));
        notificationsMessages.setChecked(sharedPreferences.getBoolean("notifications_messages", true));
        notificationsRecommendations.setChecked(sharedPreferences.getBoolean("notifications_recommendations", true));
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications_enabled", notificationsEnabled.isChecked());
        editor.putBoolean("notifications_messages", notificationsMessages.isChecked());
        editor.putBoolean("notifications_recommendations", notificationsRecommendations.isChecked());
        editor.apply();
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> deleteAccount())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Eliminar datos de Firebase Realtime Database
            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).removeValue();

            // Eliminar cuenta de Firebase Authentication
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
