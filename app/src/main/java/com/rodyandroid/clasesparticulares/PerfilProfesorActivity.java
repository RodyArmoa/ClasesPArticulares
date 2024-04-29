package com.rodyandroid.clasesparticulares;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.rodyandroid.clasesparticulares.Model.Profesor;

public class PerfilProfesorActivity extends AppCompatActivity {

    Button volveraLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_profesor);


        EdgeToEdge.enable(this);

        // Obtener los datos del profesor desde el Intent
        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Profesor profesor = bundle.getParcelable("profesor");
                // Agrega un mensaje de log para verificar si el objeto Profesor se recibe correctamente
                if (profesor != null) {
                    Log.d("PerfilProfesorActivity", "Se recibió el objeto Profesor correctamente: " + profesor.getNombre());
                } else {
                    Log.d("PerfilProfesorActivity", "No se recibió el objeto Profesor o es nulo");
                }
                mostrarDatosDelProfesor(profesor);
            } else {
                Log.d("PerfilProfesorActivity", "El Bundle es nulo");
            }
        } else {
            Log.d("PerfilProfesorActivity", "El Intent es nulo o no tiene la acción ACTION_VIEW");
        }

        volveraLista = findViewById(R.id.btnPerfilVolver);
        volveraLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilProfesorActivity.this, ListaProfeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Función para mostrar los datos del profesor en la interfaz de usuario
    private void mostrarDatosDelProfesor(Profesor profesor) {
        // Obtener las referencias a los elementos de la interfaz de usuario
        ImageView imageView = findViewById(R.id.imageView);
        TextView nombreTextView = findViewById(R.id.nombrePerfilTextView);
        TextView apellidoTextView = findViewById(R.id.apellidoPerfilTextView);
        TextView ubicacionTextView = findViewById(R.id.ubicacionPerfilTextView);
        TextView asignaturasTextView = findViewById(R.id.asignaturasPerfilTextView);
        TextView telefonoTextView = findViewById(R.id.telefonoPerfilTextView);

        // Establecer los datos del profesor en los elementos de la interfaz de usuario
        if (profesor != null) {

            // Picasso.get().load(profesor.getImagen()).placeholder(R.drawable.default_profile_image).into(imageView);
            nombreTextView.setText(profesor.getNombre());
            apellidoTextView.setText(profesor.getApellido());
            ubicacionTextView.setText(profesor.getUbicacion());
            asignaturasTextView.setText(profesor.getAsignaturas());
            telefonoTextView.setText(profesor.getTelefono());
        }
    }
}
