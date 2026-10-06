package com.crediya.model;

import java.time.LocalDate;

/**
 * Préstamo: calcula automáticamente total con interés y cuota mensual.
 * total = monto * (1 + interes/100) | cuota = total / cuotas
 */
public class Prestamo {
    private int id;
    private int clienteId;
    private int empleadoId;
    private double monto;
    private double interes; // % ej: 10 = 10%
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado; // pendiente | pagado

    public Prestamo() {}

    public Prestamo(int id, int clienteId, int empleadoId, double monto,
                    double interes, int cuotas, LocalDate fechaInicio, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    // Lógica de negocio encapsulada
    public double getMontoTotal() {
        return monto * (1 + interes / 100.0);
    }

    public double getValorCuota() {
        if (cuotas == 0) return 0;
        return getMontoTotal() / cuotas;
    }

    /** Vencido = pendiente y fechaInicio + 30*cuotas días ya pasó. Simple para el proyecto. */
    public boolean isVencido() {
        if (!"pendiente".equalsIgnoreCase(estado)) return false;
        LocalDate vencimiento = fechaInicio.plusMonths(cuotas);
        return LocalDate.now().isAfter(vencimiento);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public double getInteres() { return interes; }
    public void setInteres(double interes) { this.interes = interes; }
    public int getCuotas() { return cuotas; }
    public void setCuotas(int cuotas) { this.cuotas = cuotas; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toFileString() {
        return id + ";" + clienteId + ";" + empleadoId + ";" + monto + ";"
                + interes + ";" + cuotas + ";" + fechaInicio + ";" + estado;
    }

    public static Prestamo fromFileString(String linea) {
        String[] p = linea.split(";");
        return new Prestamo(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                Integer.parseInt(p[2]), Double.parseDouble(p[3]),
                Double.parseDouble(p[4]), Integer.parseInt(p[5]),
                LocalDate.parse(p[6]), p[7]);
    }

    @Override
    public String toString() {
        return "  Prestamo ID: " + id + "\n"
             + "  Cliente ID: " + clienteId + "\n"
             + "  Empleado ID: " + empleadoId + "\n"
             + "  Monto: " + String.format("%.2f", monto) + "\n"
             + "  Interes: " + interes + "%\n"
             + "  Cuotas: " + cuotas + "\n"
             + "  Total con interes: " + String.format("%.2f", getMontoTotal()) + "\n"
             + "  Valor cuota: " + String.format("%.2f", getValorCuota()) + "\n"
             + "  Fecha inicio: " + fechaInicio + "\n"
             + "  Estado: " + estado + "\n"
             + "  ------------------------------";
    }
}
