package com.crediya.model;

/** Empleado hereda de Persona. Atributos extra: rol, salario. */
public class Empleado extends Persona {
    private String rol;
    private double salario;

    public Empleado() {}

    public Empleado(int id, String nombre, String documento, String correo, String rol, double salario) {
        super(id, nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    @Override
    public String toFileString() {
        return id + ";" + nombre + ";" + documento + ";" + correo + ";" + rol + ";" + salario;
    }

    public static Empleado fromFileString(String linea) {
        String[] p = linea.split(";");
        return new Empleado(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4], Double.parseDouble(p[5]));
    }

    @Override
    public String toString() {
        return "Empleado{id=" + id + ", nombre='" + nombre + "', documento='" + documento
                + "', rol='" + rol + "', correo='" + correo + "', salario=" + salario + "}";
    }
}
