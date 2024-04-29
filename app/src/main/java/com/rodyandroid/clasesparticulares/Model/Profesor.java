package com.rodyandroid.clasesparticulares.Model;

import java.io.Serializable;

public class Profesor  implements Serializable {
    private Long id;

    private String nombre;
    private String apellido;
    private String ubicacion;
    private String asignaturas;
    private String telefono;
    private String imagen;

    public Profesor() {
    }

    public Profesor(Long id, String nombre, String apellido, String ubicacion, String asignaturas, String telefono, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.ubicacion = ubicacion;
        this.asignaturas = asignaturas;
        this.telefono = telefono;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(String asignaturas) {
        this.asignaturas = asignaturas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
