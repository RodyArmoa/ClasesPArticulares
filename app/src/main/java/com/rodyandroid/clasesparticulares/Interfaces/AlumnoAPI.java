package com.rodyandroid.clasesparticulares.Interfaces;

import com.rodyandroid.clasesparticulares.Login.AlumnoLoginRequest;
import com.rodyandroid.clasesparticulares.Model.Alumno;

import retrofit2.http.Body;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlumnoAPI {

    @POST("/alumnos")
    public Call<Alumno>  crearAlumno(@Body Alumno alumno);

    @POST("/alumnos/login") // Endpoint para iniciar sesión
    Call<Long> iniciarSesion(@Body AlumnoLoginRequest alumno);

    @GET("/alumnos/{id}")//peticion desde el cliente para ontener el objeto alumno
    Call<Alumno> obtenerAlumno(@Path("id") Long id);
    @PUT("/alumnos/{id}")//actualizamos el alumno por su id
    Call<Alumno> actualizarAlumno(@Path("id") Long id, @Body Alumno alumno);
    @DELETE("/alumnos/{id}")//eliminamos al alumno por su id
    Call<Void> eliminarAlumno(@Path("id") Long id);

}
