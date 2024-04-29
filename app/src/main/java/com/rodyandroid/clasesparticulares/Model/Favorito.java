package com.rodyandroid.clasesparticulares.Model;

public class Favorito {

    private Long id;
    private Alumno alumno;
    private Profesor profesor;

    public Favorito() {
    }

    public Favorito(Long id, Alumno alumno, Profesor profesor) {
        this.id = id;
        this.alumno = alumno;
        this.profesor = profesor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
