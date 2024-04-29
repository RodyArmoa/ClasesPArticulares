package com.rodyandroid.clasesparticulares.Registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rodyandroid.clasesparticulares.Interfaces.AlumnoAPI;
import com.rodyandroid.clasesparticulares.Login.LoginActivity;
import com.rodyandroid.clasesparticulares.Model.Alumno;
import com.rodyandroid.clasesparticulares.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextApellido;
    private EditText editTextEmailRegistro;
    private EditText editTextContraseñaRegistro;
    private EditText editTextRepetirContraseña;
    private Button btnRegistrar, btnVolver;
    private AlumnoAPI alumnoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.131:8082/")//Cambiar direccion de IP en caso de tener error en otro equipo que no sea el de desarrollo
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Obtener una instancia de la interfaz del servicio Alumno
        alumnoService = retrofit.create(AlumnoAPI.class);

        // Obtener referencias de los elementos de la interfaz
        editTextNombre = findViewById(R.id.editTextRegistroNombre);
        editTextApellido = findViewById(R.id.editTextregistroApellido);
        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextContraseñaRegistro = findViewById(R.id.editTextContraseñaRegistro);
        editTextRepetirContraseña = findViewById(R.id.editTextreptirContraseña);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolverLogin);

        // esto simplemente es para limpiar la interzaz al iniciar esta activity
        editTextNombre.setText("");
        editTextApellido.setText("");
        editTextEmailRegistro.setText("");
        editTextContraseñaRegistro.setText("");
        editTextRepetirContraseña.setText("");

        // Boton Registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        //vuelve al activity anterior
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registrarUsuario() {
        // Datos ingresados por el usuario
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String email = editTextEmailRegistro.getText().toString().trim();
        String contraseña = editTextContraseñaRegistro.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        // Validar que no haya campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || contraseña.isEmpty() || repetirContraseña.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validar que la contraseña tenga un mínimo de 8 caracteres y contenga números y letras
        if (contraseña.length() < 8 || !contraseña.matches(".*[a-zA-Z].*") || !contraseña.matches(".*[0-9].*")) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres y contener números y letras", Toast.LENGTH_LONG).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo objeto Alumno
        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setNombre(nombre);
        nuevoAlumno.setApellido(apellido);
        nuevoAlumno.setEmail(email);
        nuevoAlumno.setContraseña(contraseña);

        // Realizar la petición HTTP para crear el alumno
        Call<Alumno> call = alumnoService.crearAlumno(nuevoAlumno);
        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                if (response.isSuccessful()) {
                    // Alumno creado exitosamente
                    Alumno alumnoCreado = response.body();
                    // Manejar la respuesta según sea necesario
                    Toast.makeText(RegistroActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    // Error en la petición HTTP
                    Toast.makeText(RegistroActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {
                // Error en la comunicación
                Toast.makeText(RegistroActivity.this, "Error de comunicación", Toast.LENGTH_SHORT).show();
            }
        });
    }
}