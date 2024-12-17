package com.example.cqqch.actividades;

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

public class VerRecetasActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecetaAdapter adapter;
    private List<Receta> listaRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico en el content_frame del BaseActivity
        View verRecetasView = getLayoutInflater().inflate(R.layout.activity_ver_recetas, findViewById(R.id.content_frame), true);

        // Configura la navegación
        setupNavigation();

        // Inicializa la lista y el RecyclerView
        listaRecetas = new ArrayList<>();
        recyclerView = verRecetasView.findViewById(R.id.recetas_list);

        if (recyclerView == null) {
            Log.e("VerRecetasActivity", "RecyclerView no se encontró. Verifica el ID en el XML.");
            return;
        }

        // Configura el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(listaRecetas, this::onFavoriteClicked, this::onDeleteClicked);
        recyclerView.setAdapter(adapter);

        // Cargar los datos de Firebase
        cargarRecetas();
    }

    private void cargarRecetas() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        FirebaseDatabase.getInstance().getReference("Recetas").child(userId)
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
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("VerRecetasActivity", "Error al cargar recetas", error.toException());
                        Toast.makeText(VerRecetasActivity.this, "Error al cargar recetas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onFavoriteClicked(Receta receta) {
        receta.setFavorite(!receta.isFavorite());
        adapter.notifyDataSetChanged();

        // Actualiza el estado de favorito en Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase.getInstance().getReference("Recetas")
                    .child(userId)
                    .orderByChild("name")
                    .equalTo(receta.getName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().child("favorite").setValue(receta.isFavorite());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("VerRecetasActivity", "Error al actualizar favorito", databaseError.toException());
                        }
                    });
        }
    }

    private void onDeleteClicked(Receta receta) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseDatabase.getInstance().getReference("Recetas")
                    .child(userId)
                    .orderByChild("name")
                    .equalTo(receta.getName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(VerRecetasActivity.this, "Receta eliminada", Toast.LENGTH_SHORT).show();
                                            listaRecetas.remove(receta);
                                            adapter.notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(VerRecetasActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("VerRecetasActivity", "Error al eliminar receta", databaseError.toException());
                        }
                    });
        }
    }
}
