package com.rodyandroid.clasesparticulares.Interfaces;

import com.rodyandroid.clasesparticulares.Model.Profesor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoritosAPI {
    @POST("alumnos/{id}/favoritos/{profesorId}")
    Call<Void> agregarProfesorFavorito(@Path("id") Long idAlumno, @Path("profesorId") Long profesorId);

    //llamada al endpoint de listar profesores favoritos
    @GET("alumnos/{id}/favoritos")
    Call<List<Profesor>> obtenerProfesoresFavoritos(@Path("id") Long idAlumno);

    // Llamada al endpoint de eliminar profesor favorito
    @DELETE("alumnos/{id}/favoritos/{profesorId}")
    Call<Void> eliminarProfesorFavorito(@Path("id") Long idAlumno, @Path("profesorId") Long profesorId);

}

