package com.rodyandroid.clasesparticulares.Interfaces;

import com.rodyandroid.clasesparticulares.Model.Profesor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfesorAPI {
    @GET("/profesores")
    Call<List<Profesor>> obtenerTodosLosProfesores();
}
