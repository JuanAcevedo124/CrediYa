package com.crediya.model;

/** Cliente hereda de Persona. Atributo extra: telefono. */
public class Cliente extends Persona {
    private String telefono;

    public Cliente() {}

    public Cliente(int id, String nombre, String documento, String correo, String telefono) {
        super(id, nombre, documento, correo);
        this.telefono = telefono;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toFileString() {
        return id + ";" + nombre + ";" + documento + ";" + correo + ";" + telefono;
    }

    public static Cliente fromFileString(String linea) {
        String[] p = linea.split(";");
        return new Cliente(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]);
    }

    @Override
    public String toString() {
        return "  Cliente ID: " + id + "\n"
             + "  Nombre: " + nombre + "\n"
             + "  Documento: " + documento + "\n"
             + "  Correo: " + correo + "\n"
             + "  Telefono: " + telefono + "\n"
             + "  ------------------------------";
    }
}
