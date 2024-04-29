package com.rodyandroid.clasesparticulares.Login;

public class AlumnoLoginRequest {
    private String email;
    private String contraseña;

    // Constructor con parámetros para inicializar los campos
    public AlumnoLoginRequest(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
