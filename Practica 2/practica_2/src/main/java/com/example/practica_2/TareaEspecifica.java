package com.example.practica_2;

public class TareaEspecifica {

    private final String numero;
    private final String nombre;
    private final String descripcion;
    private final String fecha;

    public TareaEspecifica(String numero, String nombre, String descripcion, String fecha) {
        this.numero = numero;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String toString() {
        return "Numero: " + numero + ", Nombre: " + nombre + ", Descripcion: " + descripcion + ", Fecha: " + fecha;
    }
}
