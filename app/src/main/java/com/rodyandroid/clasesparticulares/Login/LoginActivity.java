package com.rodyandroid.clasesparticulares.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rodyandroid.clasesparticulares.Interfaces.AlumnoAPI;
import com.rodyandroid.clasesparticulares.ListaProfeActivity;
import com.rodyandroid.clasesparticulares.Model.Alumno;
import com.rodyandroid.clasesparticulares.R;
import com.rodyandroid.clasesparticulares.Registro.RegistroActivity;

import Utils.SharedPreferencesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private AlumnoAPI alumnoService;

    private Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.131:8082/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Obtener una instancia de la interfaz del servicio Alumno
        alumnoService = retrofit.create(AlumnoAPI.class);

        // Obtener referencias de los elementos de la interfaz
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistro);


        // al pulsar el boton login llama al metodo iniciarSesion()
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });


        // al pulsar el boton registrar pasa de un activity a otra
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    // ...

    private void iniciarSesion() {
        // Obtener los valores ingresados por el usuario
        String email = editTextEmail.getText().toString().trim();
        String contraseña = editTextPassword.getText().toString().trim();

        // Validar que no haya campos vacíos
        if (email.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo objeto AlumnoLoginRequest con email y contraseña
        AlumnoLoginRequest request = new AlumnoLoginRequest(email, contraseña);


        // Realizar la petición HTTP para iniciar sesión
        Call<Long> call = alumnoService.iniciarSesion(request);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    // Inicio de sesión exitoso
                    Long alumnoId = response.body();
                    // Guardar el ID del alumno en SharedPreferences,esto es util para navegar por la interfaz
                    SharedPreferencesUtils.guardarIdAlumno(LoginActivity.this, alumnoId);
                    // Manejar la respuesta según sea necesario
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    //si el usuario ha iniciado correctamente sus credenciales se pasa a la siguiente activity
                    Intent intent = new Intent(LoginActivity.this, ListaProfeActivity.class);
                    startActivity(intent);
                    // Cerrar LoginActivity para reducir el consumo
                    finish();
                } else {
                    // Error en el inicio de sesión
                    Toast.makeText(LoginActivity.this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // Error de comunicación
                Toast.makeText(LoginActivity.this, "Error de comunicación", Toast.LENGTH_SHORT).show();
            }
        });


    }
}






