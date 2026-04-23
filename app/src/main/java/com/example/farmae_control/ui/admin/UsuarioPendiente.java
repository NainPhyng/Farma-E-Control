package com.example.farmae_control.ui.admin;

public class UsuarioPendiente {
    private String uid;
    private String nombre;
    private String correo;
    private String tipo;
    private String cedula;
    private boolean cedulaValidada;

    public UsuarioPendiente() {}

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public boolean isCedulaValidada() { return cedulaValidada; }
    public void setCedulaValidada(boolean cedulaValidada) { this.cedulaValidada = cedulaValidada; }
}