package com.example.cqqch.actividades;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cqqch.R;
import com.example.cqqch.adaptadores.RecetaAdapter;
import com.example.cqqch.adaptadores.RestaurantAdapter;
import com.example.cqqch.base.BaseActivity;
import com.example.cqqch.modelos.Receta;
import com.example.cqqch.modelos.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EligeTuActivity extends BaseActivity {

    private Spinner spTipo, spCategoria, spFavorito, spNota, spPrecio;
    private Button btnBuscar, btnVolverMenu;
    private RecyclerView rvResultadosFiltro;

    private DatabaseReference database;
    private FirebaseUser user;

    private RestaurantAdapter restauranteAdapter;
    private RecetaAdapter recetaAdapter;

    private List<Restaurant> listaRestaurantes;
    private List<Receta> listaRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base);
        super.onCreate(savedInstanceState);

        setupNavigation();
        getLayoutInflater().inflate(R.layout.activity_elige_tu, findViewById(R.id.content_frame));

        // Inicializa las vistas
        setupEligeTuActivity();

        // Inicializa Firebase
        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        listaRestaurantes = new ArrayList<>();
        listaRecetas = new ArrayList<>();

        // Configurar spinners con Arrays del resources
        configurarSpinners();

        // Botón Buscar
        btnBuscar.setOnClickListener(v -> buscarResultados());

        // Botón Volver
        btnVolverMenu.setOnClickListener(v -> {
            Toast.makeText(this, "Volviendo al menú", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupEligeTuActivity() {
        spTipo = findViewById(R.id.spTipo);
        spCategoria = findViewById(R.id.spCategoria);
        spFavorito = findViewById(R.id.spFavorito);
        spNota = findViewById(R.id.spNota);
        spPrecio = findViewById(R.id.spPrecio);
        btnBuscar = findViewById(R.id.btnGuardarReceta);
        btnVolverMenu = findViewById(R.id.btnVolverMenuReceta);
        rvResultadosFiltro = findViewById(R.id.rvResultadosFiltro);

        rvResultadosFiltro.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.tipos_restaurantes_recetas, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterCategorias = ArrayAdapter.createFromResource(this, R.array.categorias_recetas, android.R.layout.simple_spinner_item);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategorias);

        ArrayAdapter<CharSequence> adapterFavorito = ArrayAdapter.createFromResource(this, R.array.opciones_si_no, android.R.layout.simple_spinner_item);
        adapterFavorito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFavorito.setAdapter(adapterFavorito);

        ArrayAdapter<CharSequence> adapterNota = ArrayAdapter.createFromResource(this, R.array.notas, android.R.layout.simple_spinner_item);
        adapterNota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNota.setAdapter(adapterNota);

        ArrayAdapter<CharSequence> adapterPrecio = ArrayAdapter.createFromResource(this, R.array.rangos_precio, android.R.layout.simple_spinner_item);
        adapterPrecio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrecio.setAdapter(adapterPrecio);
    }

    private void buscarResultados() {
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si el campo está vacío, significa sin filtro
        String tipoSeleccionado = spTipo.getSelectedItem().toString().trim();
        if (tipoSeleccionado.isEmpty()) {
            // Si tipo está vacío, escogemos uno por defecto, por ejemplo "Restaurantes"
            // o lo mantienes como es (random). Pero es mejor elegir uno fijo:
            tipoSeleccionado = "Restaurantes";
        }

        String categoriaSeleccionada = spCategoria.getSelectedItem().toString().trim();
        // Si categoría está vacía, sin filtro -> null
        if (categoriaSeleccionada.isEmpty()) {
            categoriaSeleccionada = null;
        }

        String favoritoSeleccionado = spFavorito.getSelectedItem().toString().trim();
        Boolean favoritoBool = null;
        if (!favoritoSeleccionado.isEmpty()) {
            favoritoBool = favoritoSeleccionado.equals("Si");
        }

        String notaStr = spNota.getSelectedItem().toString().trim();
        int notaSeleccionada = 0;
        if (!notaStr.isEmpty()) {
            try {
                notaSeleccionada = Integer.parseInt(notaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La nota seleccionada no es un número válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // Si está vacío, se queda en 0, indicando sin filtro de nota

        String precioSeleccionado = spPrecio.getSelectedItem().toString().trim();
        // Si precio está vacío -> sin filtro
        if (precioSeleccionado.isEmpty()) {
            precioSeleccionado = null;
        }

        if (tipoSeleccionado.equals("Restaurantes")) {
            cargarRestaurantes(categoriaSeleccionada, favoritoBool, notaSeleccionada, precioSeleccionado);
        } else {
            cargarRecetas(categoriaSeleccionada, favoritoBool, notaSeleccionada, precioSeleccionado);
        }
    }

    private void cargarRestaurantes(String categoria, Boolean favorito, int nota, String precio) {
        listaRestaurantes.clear();
        database.child("Restaurantes").child(user.getUid())
                .get().addOnSuccessListener(snapshot -> {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Restaurant restaurant = data.getValue(Restaurant.class);
                        if (restaurant != null) {
                            // Filtro de categoría
                            if (categoria != null && !restaurant.getCategory().equals(categoria)) {
                                continue;
                            }

                            // Filtro de favorito
                            if (favorito != null && restaurant.isFavorite() != favorito) {
                                continue;
                            }

                            // Filtro de nota, si nota != 0 filtra
                            if (nota != 0 && restaurant.getRating() != nota) {
                                continue;
                            }

                            // Filtro de precio
                            if (precio != null && !cumpleRangoPrecio(restaurant.getPrice(), precio)) {
                                continue;
                            }

                            listaRestaurantes.add(restaurant);
                        }
                    }

                    restauranteAdapter = new RestaurantAdapter(listaRestaurantes, this::onFavoriteClicked, this::onDeleteClicked);
                    rvResultadosFiltro.setAdapter(restauranteAdapter);

                    if (listaRestaurantes.isEmpty()) {
                        Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarRecetas(String categoria, Boolean favorito, int nota, String precio) {
        listaRecetas.clear();
        database.child("Recetas").child(user.getUid())
                .get().addOnSuccessListener(snapshot -> {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Receta receta = data.getValue(Receta.class);
                        if (receta != null) {
                            if (categoria != null && !receta.getCategory().equals(categoria)) {
                                continue;
                            }

                            if (favorito != null && receta.isFavorite() != favorito) {
                                continue;
                            }

                            if (nota != 0 && receta.getRating() != nota) {
                                continue;
                            }

                            if (precio != null && !cumpleRangoPrecio(receta.getPrice(), precio)) {
                                continue;
                            }

                            listaRecetas.add(receta);
                        }
                    }

                    recetaAdapter = new RecetaAdapter(listaRecetas, this::onFavoriteClickedReceta, this::onDeleteClickedReceta);
                    rvResultadosFiltro.setAdapter(recetaAdapter);

                    if (listaRecetas.isEmpty()) {
                        Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Comprueba si el precio está en el rango. E.g: "10 a 20"
    private boolean cumpleRangoPrecio(String precio, String rango) {
        try {
            double precioDouble = Double.parseDouble(precio);
            String[] limites = rango.split(" a ");
            double min = Double.parseDouble(limites[0]);
            double max = Double.parseDouble(limites[1]);

            return precioDouble >= min && precioDouble <= max;
        } catch (Exception e) {
            return false;
        }
    }

    // Métodos para Restaurantes (Restaurant)
    private void onFavoriteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " marcado como favorito", Toast.LENGTH_SHORT).show();

    }

    private void onDeleteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " eliminado", Toast.LENGTH_SHORT).show();

    }

    // Métodos para Recetas (Receta)
    private void onFavoriteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " marcado como favorito", Toast.LENGTH_SHORT).show();

    }

    private void onDeleteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " eliminado", Toast.LENGTH_SHORT).show();

    }
}
