package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;
import com.example.cqqch.adaptadores.RecetaAdapter;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Receta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Actividad que muestra una lista de recetas guardadas por el usuario.
 * Permite marcar recetas como favoritas, eliminarlas o editarlas.
 */
public class VerRecetasActivity extends BaseActivity {

    private static final String TAG = "VerRecetasActivity";

    // RecyclerView y Adaptador para mostrar las recetas
    private RecyclerView recyclerView;
    private RecetaAdapter adapter;
    private final List<Receta> listaRecetas = new ArrayList<>();

    /**
     * Método llamado al crear la actividad.
     * Configura el diseño, inicializa la navegación y carga los datos de las recetas.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Inflar el layout específico en el marco base
        View verRecetasView = getLayoutInflater().inflate(
                R.layout.activity_ver_recetas,
                findViewById(R.id.content_frame),
                true
        );

        // Configurar la navegación
        setupNavigation();

        // Inicializar vistas y adaptador
        recyclerView = verRecetasView.findViewById(R.id.recetas_list);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(
                listaRecetas,           // Lista de recetas
                this::onFavoriteClicked,  // Favorito
                this::onDeleteClicked,    // Eliminar
                this::onEditClicked       // Editar
        );
        recyclerView.setAdapter(adapter);

        // Cargar las recetas desde Firebase
        cargarRecetas();
    }

    /**
     * Carga las recetas del usuario actual desde Firebase.
     */
    private void cargarRecetas() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        final String userId = currentUser.getUid();
        FirebaseDatabase.getInstance()
                .getReference("Recetas")
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaRecetas.clear();
                        for (DataSnapshot recetaSnapshot : snapshot.getChildren()) {
                            Receta receta = recetaSnapshot.getValue(Receta.class);
                            if (receta != null) {
                                listaRecetas.add(receta);
                            }
                        }
                        // Notificamos cambios al adaptador
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error al cargar recetas", error.toException());
                        Toast.makeText(VerRecetasActivity.this,
                                "Error al cargar recetas",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Marca o desmarca una receta como favorita.
     *
     * @param receta Objeto de receta seleccionada.
     */
    private void onFavoriteClicked(Receta receta) {
        // Invierte localmente el estado de favorito
        boolean nuevoValor = !receta.isFavorite();
        receta.setFavorite(nuevoValor);
        adapter.notifyDataSetChanged();

        // Actualiza en Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String userId = currentUser.getUid();
            FirebaseDatabase.getInstance()
                    .getReference("Recetas")
                    .child(userId)
                    .orderByChild("name")
                    .equalTo(receta.getName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef()
                                        .child("favorite")
                                        .setValue(nuevoValor)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "Favorito actualizado en Firebase");
                                        })
                                        .addOnFailureListener(e -> {
                                            receta.setFavorite(!nuevoValor);
                                            adapter.notifyDataSetChanged();
                                            Log.e(TAG, "Error al actualizar favorito", e);
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Error en la búsqueda
                            Log.e(TAG, "Error al actualizar favorito", error.toException());
                        }
                    });
        }
    }

    /**
     * Elimina una receta seleccionada.
     *
     * @param receta Objeto de receta seleccionada.
     */
    private void onDeleteClicked(Receta receta) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        final String userId = currentUser.getUid();
        FirebaseDatabase.getInstance()
                .getReference("Recetas")
                .child(userId)
                .orderByChild("name")
                .equalTo(receta.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(
                                                VerRecetasActivity.this,
                                                "Receta eliminada",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        listaRecetas.remove(receta);
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(
                                                VerRecetasActivity.this,
                                                "Error al eliminar",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        Log.e(TAG, "Error al eliminar receta", e);
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error en la consulta para eliminar", error.toException());
                    }
                });
    }

    /**
     * Lanza la actividad para editar la receta seleccionada.
     *
     * @param receta Objeto de receta seleccionada.
     */
    private void onEditClicked(Receta receta) {
        // Lanza la pantalla de edición
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("receta", receta);
        startActivity(intent);
    }

}
