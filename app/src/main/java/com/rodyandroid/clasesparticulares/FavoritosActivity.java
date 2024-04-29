package com.rodyandroid.clasesparticulares;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rodyandroid.clasesparticulares.Interfaces.AlumnoAPI;
import com.rodyandroid.clasesparticulares.Interfaces.FavoritosAPI;
import com.rodyandroid.clasesparticulares.Interfaces.ProfesorAPI;
import com.rodyandroid.clasesparticulares.Login.LoginActivity;
import com.rodyandroid.clasesparticulares.Model.Alumno;
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

public class FavoritosActivity extends AppCompatActivity {

    BottomNavigationView mbottonnavigation;

    List<Profesor> listaProfesor;
    ListaProfesorAdapter listaProfesorAdapter;

    RetrofitClient retrofitClient;
    private Long alumnoId;

    ListaProfeActivity.RetrofitClientfavorito retrofitClientfavorito;

    private DBHelper dbHelper;

    ImageButton imageButtonEliminar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        View listElementView1 = getLayoutInflater().inflate(R.layout.lista_element_favoritos, null);

        // Después de inicializar el RecyclerView y el adaptador
        View perfilLayout = getLayoutInflater().inflate(R.layout.layout_alumno_details, null);


        //Obtenemos el ID del alumno que ha iniciado session
        alumnoId = obtenerIdAlumno();

        listaProfesor = new ArrayList<>();
        listaProfesorAdapter = new ListaProfesorAdapter(listaProfesor, this);
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listaProfesorAdapter);


        imageButtonEliminar = listElementView1.findViewById(R.id.imageButFavEliminar);

        imageButtonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition((View) v.getParent());
                if (position != RecyclerView.NO_POSITION) {
                    // Llamar al método para eliminar el profesor de la lista
                    listaProfesorAdapter.eliminarProfesor(position);

                    // Obtener el profesor que se va a eliminar
                    Profesor profesor = listaProfesor.get(position);

                    // Llamar al método para eliminar al profesor favorito del servidor
                    eliminarProfesorFavorito(alumnoId, profesor.getId());
                }
            }

            private void eliminarProfesorFavorito(Long idAlumno, Long profesorId) {
                // Obtener la instancia de Retrofit desde RetrofitClientfavorito
                Retrofit retrofit = retrofitClientfavorito.getClient();

                // Crear la interfaz de la API de favoritos
                FavoritosAPI favoritoAPI = retrofit.create(FavoritosAPI.class);

                // Realizar la llamada a la API para eliminar al profesor favorito
                Call<Void> call = favoritoAPI.eliminarProfesorFavorito(idAlumno, profesorId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(FavoritosActivity.this, "Profesor eliminado de favoritos", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(FavoritosActivity.this, "Error al eliminar profesor de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


      //Boton actualizar Alumno
        Button btnActualizar = perfilLayout.findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos actualizados del alumno de las vistas en el perfilLayout
                EditText editNombre = perfilLayout.findViewById(R.id.editNombre);
                EditText editApellido = perfilLayout.findViewById(R.id.editApellido);
                EditText editEmail = perfilLayout.findViewById(R.id.editEmail);
                EditText editContraseña = perfilLayout.findViewById(R.id.editContraseña);

                String nombre = editNombre.getText().toString();
                String apellido = editApellido.getText().toString();
                String email = editEmail.getText().toString();
                String contraseña = editContraseña.getText().toString();

                // Crear una instancia de Retrofit para hacer la solicitud al servidor
                String BASE_URL = "http://192.168.1.131:8082/";//cambiar la direccion Ip en caso de error en otros equipos que no sea de desarrollo
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Crear una instancia de la interfaz AlumnoAPI para realizar la solicitud
                AlumnoAPI alumnoAPI = retrofit.create(AlumnoAPI.class);

                // Realizar la llamada a la API para obtener los datos actuales del alumno
                Call<Alumno> obtenerAlumnoCall = alumnoAPI.obtenerAlumno(alumnoId);
                obtenerAlumnoCall.enqueue(new Callback<Alumno>() {
                    @Override
                    public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Obtener el objeto Alumno de la respuesta
                            Alumno alumnoActual = response.body();

                            // Actualizar solo los campos que el alumno ha modificado
                            if (!nombre.isEmpty()) {
                                alumnoActual.setNombre(nombre);
                            }
                            if (!apellido.isEmpty()) {
                                alumnoActual.setApellido(apellido);
                            }
                            if (!email.isEmpty()) {
                                alumnoActual.setEmail(email);
                            }
                            if (!contraseña.isEmpty()) {
                                alumnoActual.setContraseña(contraseña);
                            }

                            // Realizar la llamada a la API para actualizar el alumno
                            Call<Alumno> actualizarAlumnoCall = alumnoAPI.actualizarAlumno(alumnoId, alumnoActual);
                            actualizarAlumnoCall.enqueue(new Callback<Alumno>() {
                                @Override
                                public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        // Manejar la respuesta exitosa
                                        Toast.makeText(FavoritosActivity.this, "Alumno actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Manejar la respuesta no exitosa o cualquier incidencia
                                        Toast.makeText(FavoritosActivity.this, "Error al actualizar el alumno", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Alumno> call, Throwable t) {
                                    // Manejar el fallo de la llamada
                                    Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Manejar el caso en el que la respuesta para obtener los datos del alumno no sea exitosa
                            Toast.makeText(FavoritosActivity.this, "Error al obtener los datos del alumno", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Alumno> call, Throwable t) {
                        // Manejar el fallo de la llamada para obtener los datos del alumno
                        Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        //Vuelve al anterior LAyout
        Button btnVolver = perfilLayout.findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, FavoritosActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //boton que llama al metodo eliminarAlumno()
        Button btnEliminarAlumno = perfilLayout.findViewById(R.id.btnEliminarAlumno);
        btnEliminarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarAlumno(alumnoId);
            }
        });


        //Inicio de menu navegaciion

        mbottonnavigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mbottonnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_nav_perfil) {

                    setContentView(perfilLayout);

                    // Obtener los datos del alumno
                    obtenerDatosAlumno(alumnoId, (ConstraintLayout) perfilLayout);

                }
                if (menuItem.getItemId() == R.id.menu_nav_favorito) {

                }
                if (menuItem.getItemId() == R.id.menu_nav_inicio) {

                    Intent intent = new Intent(FavoritosActivity.this, ListaProfeActivity.class);
                    startActivity(intent);
                    finish();

                }
                if (menuItem.getItemId() == R.id.menu_nav_salir) {
                    Intent intent = new Intent(FavoritosActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


                return true;
            }
        });
        obtenerProfesoresFavoritos(alumnoId);
        //Fin de menu navegaciion

        alumnoId = obtenerIdAlumno();
    }

    private void eliminarAlumno(Long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.131:8082/") // Asegúrate de que la URL es correcta
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AlumnoAPI alumnoAPI = retrofit.create(AlumnoAPI.class);
        Call<Void> call = alumnoAPI.eliminarAlumno(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FavoritosActivity.this, "Alumno eliminado con éxito", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(FavoritosActivity.this, "Error al eliminar el alumno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FavoritosActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(FavoritosActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void obtenerDatosAlumno(Long alumnoId, ConstraintLayout perfilLayout) {
        // Crear una instancia de Retrofit para hacer la solicitud al servidor
        String BASE_URL = "http://192.168.1.131:8082/";//cambiar IP en caso de error en otro equipo
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear una instancia de la interfaz AlumnoAPI para realizar la solicitud
        AlumnoAPI alumnoAPI = retrofit.create(AlumnoAPI.class);

        // Realizar la llamada a la API para obtener los datos del alumno
        Call<Alumno> call = alumnoAPI.obtenerAlumno(alumnoId);

        // Manejar la respuesta de la llamada asíncrona
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Obtener el objeto Alumno de la respuesta
                    Alumno alumno = response.body();

                    // Obtener referencias a las vistas en el perfilLayout
                    TextView nombreTextView = perfilLayout.findViewById(R.id.txtNombre);
                    TextView apellidoTextView = perfilLayout.findViewById(R.id.txtApellidoPerfil);
                    TextView emailTextView = perfilLayout.findViewById(R.id.txtEmailPerfil);


                    nombreTextView.setText(alumno.getNombre());
                    apellidoTextView.setText(alumno.getApellido());
                    emailTextView.setText(alumno.getEmail());


                } else {

                    Toast.makeText(FavoritosActivity.this, "Error al obtener los datos del alumno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {

                Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Inicio de la llamada de listar a profesoresfavoritos

    private void obtenerProfesoresFavoritos(Long alumnoId) {
        // Inicializa Retrofit y crea la interfaz de la API
        RetrofitClient retrofitClient = new RetrofitClient();
        FavoritosAPI apiInterface = retrofitClient.getRetrofitInstance().create(FavoritosAPI.class);

        // Realiza la solicitud GET para obtener los profesores favoritos
        Call<List<Profesor>> call = apiInterface.obtenerProfesoresFavoritos(alumnoId);
        call.enqueue(new Callback<List<Profesor>>() {
            @Override
            public void onResponse(Call<List<Profesor>> call, Response<List<Profesor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProfesor.clear();
                    listaProfesor.addAll(response.body());
                    listaProfesorAdapter.notifyDataSetChanged();

                    // Ahora que la lista de profesores favoritos está cargada, podemos obtener el ID del profesor favorito
                    Long profesorId = obtenerIdProfesorFavorito();
                    if (profesorId != null) {
                        // Llamar al método para eliminar al profesor favorito
                        eliminarProfesorFavorito(alumnoId, profesorId);
                    } else {
                        Toast.makeText(FavoritosActivity.this, "Error: No se pudo obtener el ID del profesor favorito", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FavoritosActivity.this, "Error al obtener los profesores favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Profesor>> call, Throwable t) {
                Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarProfesorFavorito(Long idAlumno, Long profesorId) {

    }



    private Long obtenerIdProfesorFavorito() {
        // Verificar si la lista de profesores no está vacía
        if (!listaProfesor.isEmpty()) {
            // Obtener el ID del primer profesor en la lista de profesores favoritos
            return listaProfesor.get(0).getId();
        } else {
            // Retornar null si la lista de profesores favoritos está vacía
            return null;
        }
    }


    public static class RetrofitClient {
        private static final String BASE_URL = "http://192.168.1.131:8082/";
        private static Retrofit retrofit = null;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        public static RetrofitClient getInstance() {
            return null;
        }

    }


    private Long obtenerIdAlumno() {
        return SharedPreferencesUtils.obtenerIdAlumno(this);
    }


}