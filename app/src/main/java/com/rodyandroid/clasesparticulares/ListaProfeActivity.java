package com.rodyandroid.clasesparticulares;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodyandroid.clasesparticulares.Interfaces.FavoritosAPI;
import com.rodyandroid.clasesparticulares.Interfaces.ProfesorAPI;
import com.rodyandroid.clasesparticulares.Login.LoginActivity;
import com.rodyandroid.clasesparticulares.Model.Profesor;


import java.util.ArrayList;
import java.util.List;

import BasesDeDatosLocal.DBHelper;
import Utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.database.sqlite.SQLiteDatabase;
import android.Manifest;


public class ListaProfeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    BottomNavigationView mbottonnavigation;

    List<Profesor> listaProfesor;
    ListaProfesorAdapter listaProfesorAdapter;
    RetrofitClient retrofitClient;
    RetrofitClientfavorito retrofitClientfavorito;
    private DBHelper dbHelper;
    ImageButton imageButton;
    private String telefonoActual;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    SearchView txtBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_profe);

        View listElementView = getLayoutInflater().inflate(R.layout.list_element, null);

        init();
        retrofitClient = new RetrofitClient();
        retrofitClientfavorito = new RetrofitClientfavorito();
        obtenerProfesores();

        txtBuscar = findViewById(R.id.buscarProfe);
        txtBuscar.setOnQueryTextListener(this);


        txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar la lista de profesores según el texto de búsqueda
                List<Profesor> profesoresFiltrados = new ArrayList<>();
                String query = newText.toLowerCase().trim();

                for (Profesor profesor : listaProfesor) {
                    // Convertir las asignaturas a minúsculas para una comparación sin distinción entre mayúsculas y minúsculas
                    String asignaturasProfesor = profesor.getAsignaturas().toLowerCase();

                    // Verificar si alguna de las asignaturas del profesor contiene el texto de búsqueda
                    if (asignaturasProfesor.contains(query)) {
                        profesoresFiltrados.add(profesor);
                    }
                }

                // Actualizar el adaptador con la lista filtrada
                listaProfesorAdapter.setProfesores(profesoresFiltrados);
                listaProfesorAdapter.notifyDataSetChanged();

                // Verificar si el texto de búsqueda está vacío
                if (newText.isEmpty()) {
                    // Si está vacío, muestra la barra de navegación
                    mbottonnavigation.setVisibility(View.VISIBLE);
                } else {
                    // Si hay texto en el campo de búsqueda, oculta la barra de navegación
                    mbottonnavigation.setVisibility(View.GONE);
                }

                return true;
            }
        });

        listaProfesorAdapter.setOnItemDoubleClickListener(new ListaProfesorAdapter.OnItemDoubleClickListener() {
            @Override
            public void onItemDoubleClick(Profesor profesor) {
                realizarLlamadaTelefonica(profesor.getTelefono());
            }
        });


        // Encuentra el ImageButton dentro de la vista inflada
        imageButton = listElementView.findViewById(R.id.imageButtonFavoritos);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el ID del alumno
                Long idAlumno = obtenerIdAlumno();

                // Verificar si la lista de profesores no está vacía
                if (!listaProfesor.isEmpty()) {
                    // Obtener el profesor en la primera posición de la lista (puedes ajustar esto según tus necesidades)
                    Profesor profesor = listaProfesor.get(0);

                    // Verificar si el profesor no es nulo
                    if (profesor != null) {
                        // Crear un Intent para iniciar la actividad PerfilProfesorActivity
                        Intent intent = new Intent(ListaProfeActivity.this, PerfilProfesorActivity.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("profesor", (Parcelable) profesor);
                        intent.putExtras(bundle);
                        startActivity(intent);


                        // Llamar al método para agregar el profesor como favorito
                        agregarProfesorFavorito(idAlumno, profesor.getId());
                    } else {
                        Toast.makeText(ListaProfeActivity.this, "Error: El profesor es nulo", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ListaProfeActivity.this, "Error: La lista de profesores está vacía", Toast.LENGTH_LONG).show();
                }

            }
        });


        mbottonnavigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mbottonnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_nav_perfil) {

                }
                if (menuItem.getItemId() == R.id.menu_nav_favorito) {
                    Intent intent = new Intent(ListaProfeActivity.this, FavoritosActivity.class);
                    startActivity(intent);
                    finish();

                }
                if (menuItem.getItemId() == R.id.menu_nav_inicio) {

                    Intent intent = new Intent(ListaProfeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

                return true;
            }
        });
    }

    public void init() {
        listaProfesor = new ArrayList<>();
        listaProfesorAdapter = new ListaProfesorAdapter(listaProfesor, this);
        listaProfesorAdapter.setOnProfesorFavoritoClickListener(this); // Establece el listener personalizado
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaProfesorAdapter);

        listaProfesorAdapter = new ListaProfesorAdapter(listaProfesor, this);
        listaProfesorAdapter.setOnItemDoubleClickListener(new ListaProfesorAdapter.OnItemDoubleClickListener() {
            @Override
            public void onItemDoubleClick(Profesor profesor) {
                realizarLlamadaTelefonica(profesor.getTelefono());
            }
        });
        recyclerView.setAdapter(listaProfesorAdapter);
        cargarProfesores();
        recyclerView.setAdapter(listaProfesorAdapter);


        // Agregar el listener de favoritos al adaptador
        setOnFavoritosClickListener(new ListaProfesorAdapter.OnFavoritosClickListener() {
            @Override
            public void onFavoritosClick(Profesor profesor) {
                Long idAlumno = obtenerIdAlumno();
                if (idAlumno != null) {
                    agregarProfesorFavorito(idAlumno, profesor.getId());
                } else {
                    Toast.makeText(ListaProfeActivity.this, "Error: No se pudo obtener el ID del alumno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Metodo creado para realizar pruebas de llamada directa, no será utilizado
    private void cargarProfesores() {
        // Simular la carga de profesores
        listaProfesor.add(new Profesor(1L, "John", "Doe", "Madrid", "Matemáticas", "123456789", ""));
        listaProfesor.add(new Profesor(2L, "Jane", "Doe", "Barcelona", "Física", "987654321", ""));
        listaProfesorAdapter.notifyDataSetChanged();
    }

    private void realizarLlamadaTelefonica(String telefono) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, solicitarlo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Tenemos permiso, iniciar la llamada
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telefono));
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                realizarLlamadaTelefonica(telefonoActual);
            } else {
                Toast.makeText(this, "Permiso para realizar llamadas no concedido", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Inicio de los metodos para buscar Al profesor por asignaturas


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        return true;
    }


    //Fin de los metodos para buscar Al profesor por asignaturas


    public class RetrofitClient {
        private static final String BASE_URL = "http://192.168.1.131:8082/";//cambiar el Ip en caso de fallo

        private Retrofit retrofit = null;

        public Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    private void obtenerProfesores() {
        retrofitClient = new RetrofitClient();
        ProfesorAPI apiInterface = retrofitClient.getRetrofitInstance().create(ProfesorAPI.class);
        Call<List<Profesor>> call = apiInterface.obtenerTodosLosProfesores();

        call.enqueue(new Callback<List<Profesor>>() {
            @Override
            public void onResponse(Call<List<Profesor>> call, Response<List<Profesor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProfesor.clear();
                    listaProfesor.addAll(response.body());
                    listaProfesorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Profesor>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void agregarProfesorFavorito(Long idAlumno, Long profesorId) {
        // Obtener la instancia de Retrofit desde RetrofitClientfavorito
        Retrofit retrofit = retrofitClientfavorito.getClient();

        // Crear la interfaz de la API de favoritos
        FavoritosAPI favoritoAPI = retrofit.create(FavoritosAPI.class);

        // Realizar la llamada a la API para agregar el profesor favorito
        Call<Void> call = favoritoAPI.agregarProfesorFavorito(idAlumno, profesorId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListaProfeActivity.this, "Profesor agregado a favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListaProfeActivity.this, "Error al agregar profesor a favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ListaProfeActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class RetrofitClientfavorito {

        private static final String BASE_URL = "http://192.168.1.131:8082/";

        private Retrofit retrofit = null;

        public Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }

    // Método para obtener la ID del alumno que ha iniciado sesión
    private Long obtenerIdAlumno() {
        return SharedPreferencesUtils.obtenerIdAlumno(this);
    }


    //pruebas


    public interface OnProfesorFavoritoClickListener {
        void onProfesorFavoritoClick(int position);
    }


    private OnProfesorFavoritoClickListener profesorFavoritoClickListener;

    public void setOnFavoritosClickListener(ListaProfesorAdapter.OnFavoritosClickListener listener) {
        this.listaProfesorAdapter.setOnFavoritosClickListener(listener);
    }


    public void onBindViewHolder(final ListaProfesorAdapter.ViewHolder holder, final int position) {

        holder.imageButtonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método onProfesorFavoritoClick del listener cuando se haga clic en el botón
                if (profesorFavoritoClickListener != null) {
                    profesorFavoritoClickListener.onProfesorFavoritoClick(position);
                }
            }
        });
    }


    public void onProfesorFavoritoClick(int position) {
        // Obtener el profesor de la lista utilizando la posición seleccionada
        Profesor profesor = listaProfesor.get(position);

        // Obtener el ID del alumno que ha iniciado sesión
        Long idAlumno = obtenerIdAlumno();

        if (idAlumno != null) {
            // Llamar al método para agregar el profesor como favorito
            agregarProfesorFavorito(idAlumno, profesor.getId());
        } else {
            Toast.makeText(this, "Error: No se pudo obtener el ID del alumno", Toast.LENGTH_SHORT).show();
        }
    }
}
