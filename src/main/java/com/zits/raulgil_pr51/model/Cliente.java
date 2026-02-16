package com.zits.raulgil_pr51.model;

public class Cliente {
    private int id;
    private String nombre;
    private String apellidos;
    private String nif;
    private String direccion;

    public Cliente(int id, String nombre, String apellidos, String nif, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        this.direccion = direccion;
    }

    public Cliente(String nombre, String apellidos, String nif, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getNif() {
        return nif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (" + nif + ")";
    }
}
