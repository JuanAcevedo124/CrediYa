package com.crediya.model;

import java.time.LocalDate;

public class Pago {
    private int id;
    private int prestamoId;
    private LocalDate fechaPago;
    private double monto;

    public Pago() {}

    public Pago(int id, int prestamoId, LocalDate fechaPago, double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPrestamoId() { return prestamoId; }
    public void setPrestamoId(int prestamoId) { this.prestamoId = prestamoId; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String toFileString() {
        return id + ";" + prestamoId + ";" + fechaPago + ";" + monto;
    }

    public static Pago fromFileString(String linea) {
        String[] p = linea.split(";");
        return new Pago(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                LocalDate.parse(p[2]), Double.parseDouble(p[3]));
    }

    @Override
    public String toString() {
        return "Pago{id=" + id + ", prestamoId=" + prestamoId
                + ", fecha=" + fechaPago + ", monto=" + monto + "}";
    }
}
