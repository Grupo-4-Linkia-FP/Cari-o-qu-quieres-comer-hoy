package com.example.cqqch.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * Actividad que permite a los usuarios filtrar y seleccionar restaurantes o recetas basados
 * en criterios como tipo, categoría, favoritos, nota y precio.
 */
public class EligeTuActivity extends BaseActivity {

    // Elementos de la interfaz
    private Spinner spTipo, spCategoria, spFavorito, spNota, spPrecio;
    private Button btnBuscar, btnVolverMenu;
    private RecyclerView rvResultadosFiltro;

    // Firebase
    private DatabaseReference database;
    private FirebaseUser user;

    // Adaptadores para los resultados
    private RestaurantAdapter restauranteAdapter;
    private RecetaAdapter recetaAdapter;

    // Listas para almacenar los resultados
    private List<Restaurant> listaRestaurantes;
    private List<Receta> listaRecetas;

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz, inicializa Firebase y gestiona las acciones del usuario.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Infla el diseño específico en el contenedor base
        getLayoutInflater().inflate(R.layout.activity_elige_tu, findViewById(R.id.content_frame));

        // Configura la navegación (heredada de BaseActivity)
        setupNavigation();

        // Inicializa las vistas
        setupEligeTuActivity();

        // Configura Firebase
        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        listaRestaurantes = new ArrayList<>();
        listaRecetas = new ArrayList<>();

        // Configura los spinners con datos de los recursos
        configurarSpinners();

        // Acciones de los botones
        btnBuscar.setOnClickListener(v -> buscarResultados());
        btnVolverMenu.setOnClickListener(v -> {
            Toast.makeText(this, "Volviendo al menú", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * Inicializa los elementos de la interfaz de usuario.
     */
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

    /**
     * Configura los spinners de la actividad con datos de los recursos XML.
     */
    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_restaurantes_recetas,
                android.R.layout.simple_spinner_item
        );
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapterTipo);

        ArrayAdapter<CharSequence> adapterCategorias = ArrayAdapter.createFromResource(
                this,
                R.array.categorias_todas,
                android.R.layout.simple_spinner_item
        );
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategorias);

        ArrayAdapter<CharSequence> adapterFavorito = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_si_no,
                android.R.layout.simple_spinner_item
        );
        adapterFavorito.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFavorito.setAdapter(adapterFavorito);

        ArrayAdapter<CharSequence> adapterNota = ArrayAdapter.createFromResource(
                this,
                R.array.notas,
                android.R.layout.simple_spinner_item
        );
        adapterNota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNota.setAdapter(adapterNota);

        ArrayAdapter<CharSequence> adapterPrecio = ArrayAdapter.createFromResource(
                this,
                R.array.rangos_precio,
                android.R.layout.simple_spinner_item
        );
        adapterPrecio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrecio.setAdapter(adapterPrecio);
    }

    /**
     * Realiza la búsqueda de resultados basada en los filtros seleccionados.
     */
    private void buscarResultados() {
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipoSeleccionado = spTipo.getSelectedItem().toString().trim();
        if (tipoSeleccionado.isEmpty()) {
            // Selección aleatoria entre "Ir", "Pedir" y "Recetas"
            String[] opciones = {"Ir", "Pedir", "Recetas"};
            tipoSeleccionado = opciones[(int) (Math.random() * opciones.length)];
        }

        String categoriaSeleccionada = spCategoria.getSelectedItem().toString().trim();
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

        String precioSeleccionado = spPrecio.getSelectedItem().toString().trim();
        if (precioSeleccionado.isEmpty()) {
            precioSeleccionado = null;
        }

        switch (tipoSeleccionado) {
            case "Ir":
                cargarOpcionesIr(categoriaSeleccionada, favoritoBool, notaSeleccionada, precioSeleccionado);
                break;
            case "Pedir":
                cargarOpcionesPedir(categoriaSeleccionada, favoritoBool, notaSeleccionada, precioSeleccionado);
                break;

            case "Recetas":
                cargarRecetas(categoriaSeleccionada, favoritoBool, notaSeleccionada, precioSeleccionado);
                break;

            default:
                Toast.makeText(this, "Opción no válida", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Carga restaurantes que cumplen los filtros para la opción "Ir".
     */
    private void cargarOpcionesIr(String categoria, Boolean favorito, int nota, String precio) {
        listaRestaurantes.clear();
        database.child("Restaurantes").child(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Restaurant> tempLista = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Restaurant restaurant = data.getValue(Restaurant.class);
                        if (restaurant != null) {
                            if (!restaurant.isCanGo()) continue; // Solo restaurantes para "Ir"
                            if (categoria != null && !restaurant.getCategory().equals(categoria)) continue;
                            if (favorito != null && restaurant.isFavorite() != favorito) continue;
                            if (nota != 0 && restaurant.getRating() != nota) continue;
                            if (precio != null && !cumpleRangoPrecio(restaurant.getPrice(), precio)) continue;
                            tempLista.add(restaurant);
                        }
                    }

                    if (!tempLista.isEmpty()) {
                        listaRestaurantes.add(tempLista.get((int) (Math.random() * tempLista.size())));
                    }

                    restauranteAdapter = new RestaurantAdapter(
                            listaRestaurantes,
                            this::onFavoriteClicked,
                            this::onDeleteClicked,
                            this::onEditClicked
                    );
                    rvResultadosFiltro.setAdapter(restauranteAdapter);

                    if (listaRestaurantes.isEmpty()) {
                        Toast.makeText(this, "No se encontraron resultados para 'Ir'", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Carga restaurantes que cumplen los filtros para la opción "Pedir".
     */
    private void cargarOpcionesPedir(String categoria, Boolean favorito, int nota, String precio) {
        listaRestaurantes.clear();
        database.child("Restaurantes").child(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Restaurant> tempLista = new ArrayList<>();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Restaurant restaurant = data.getValue(Restaurant.class);
                        if (restaurant != null) {
                            if (!restaurant.isCanOrder()) continue; // Solo restaurantes para "Pedir"
                            if (categoria != null && !restaurant.getCategory().equals(categoria)) continue;
                            if (favorito != null && restaurant.isFavorite() != favorito) continue;
                            if (nota != 0 && restaurant.getRating() != nota) continue;
                            if (precio != null && !cumpleRangoPrecio(restaurant.getPrice(), precio)) continue;
                            tempLista.add(restaurant);
                        }
                    }

                    if (!tempLista.isEmpty()) {
                        listaRestaurantes.add(tempLista.get((int) (Math.random() * tempLista.size())));
                    }

                    restauranteAdapter = new RestaurantAdapter(
                            listaRestaurantes,
                            this::onFavoriteClicked,
                            this::onDeleteClicked,
                            this::onEditClicked
                    );
                    rvResultadosFiltro.setAdapter(restauranteAdapter);

                    if (listaRestaurantes.isEmpty()) {
                        Toast.makeText(this, "No se encontraron resultados para 'Pedir'", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Carga recetas que cumplen los filtros seleccionados.
     */
    private void cargarRecetas(String categoria, Boolean favorito, int nota, String precio) {
        listaRecetas.clear();
        database.child("Recetas").child(user.getUid())
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Receta> tempLista = new ArrayList<>(); // Lista temporal
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Receta receta = data.getValue(Receta.class);
                        if (receta != null) {
                            // Filtro categoría
                            if (categoria != null && !receta.getCategory().equals(categoria)) {
                                continue;
                            }
                            // Filtro favorito
                            if (favorito != null && receta.isFavorite() != favorito) {
                                continue;
                            }
                            // Filtro nota
                            if (nota != 0 && receta.getRating() != nota) {
                                continue;
                            }
                            // Filtro precio
                            if (precio != null && !cumpleRangoPrecio(receta.getPrice(), precio)) {
                                continue;
                            }
                            tempLista.add(receta); // Agregar a la lista temporal
                        }
                    }

                    if (!tempLista.isEmpty()) {
                        Receta randomReceta = tempLista.get((int) (Math.random() * tempLista.size())); // Seleccionar uno aleatorio
                        listaRecetas.add(randomReceta);
                    }

                    recetaAdapter = new RecetaAdapter(
                            listaRecetas,
                            this::onFavoriteClickedReceta,
                            this::onDeleteClickedReceta,
                            this::onEditClickedReceta
                    );
                    rvResultadosFiltro.setAdapter(recetaAdapter);

                    if (listaRecetas.isEmpty()) {
                        Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Verifica si un precio está dentro de un rango.
     */
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

    /**
     * Marca un restaurante como favorito y muestra un mensaje al usuario.
     *
     * @param restaurant Restaurante que será marcado como favorito.
     */
    private void onFavoriteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " marcado como favorito", Toast.LENGTH_SHORT).show();
    }

    /**
     * Elimina un restaurante y muestra un mensaje al usuario.
     *
     * @param restaurant Restaurante que será eliminado.
     */
    private void onDeleteClicked(Restaurant restaurant) {
        Toast.makeText(this, restaurant.getName() + " eliminado", Toast.LENGTH_SHORT).show();
    }

    /**
     * Edita los detalles de un restaurante.
     * Abre la actividad de edición de restaurantes con los datos del restaurante seleccionado.
     *
     * @param restaurant Restaurante cuyos detalles serán editados.
     */
    private void onEditClicked(Restaurant restaurant) {
        Intent intent = new Intent(this, EditRestaurantActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }

    /**
     * Marca una receta como favorita y muestra un mensaje al usuario.
     *
     * @param receta Receta que será marcada como favorita.
     */
    private void onFavoriteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " marcado como favorito", Toast.LENGTH_SHORT).show();
    }

    /**
     * Elimina una receta y muestra un mensaje al usuario.
     *
     * @param receta Receta que será eliminada.
     */
    private void onDeleteClickedReceta(Receta receta) {
        Toast.makeText(this, receta.getName() + " eliminado", Toast.LENGTH_SHORT).show();
    }

    /**
     * Edita los detalles de una receta.
     * Abre la actividad de edición de recetas con los datos de la receta seleccionada.
     *
     * @param receta Receta cuyos detalles serán editados.
     */
    private void onEditClickedReceta(Receta receta) {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra("receta", receta);
        startActivity(intent);
    }
}
