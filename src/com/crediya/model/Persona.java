package com.crediya.model;

/**
 * Clase abstracta base para Empleado y Cliente.
 * Aplica HERENCIA y ENCAPSULAMIENTO: atributos privados + getters/setters.
 */
public abstract class Persona {
    protected int id;
    protected String nombre;
    protected String documento;
    protected String correo;

    public Persona() {}

    public Persona(int id, String nombre, String documento, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    /** POLIMORFISMO: cada hija define cómo se muestra. */
    public abstract String toFileString();
}
