package com.crediya.model;

/** Enum en vez de String suelto "pendiente/pagado". Más seguro y profesional. */
public enum EstadoPrestamo {
    PENDIENTE("pendiente"),
    PAGADO("pagado");

    private final String valor;
    EstadoPrestamo(String valor) { this.valor = valor; }
    public String getValor() { return valor; }

    public static EstadoPrestamo desde(String s) {
        for (EstadoPrestamo e : values()) {
            if (e.valor.equalsIgnoreCase(s)) return e;
        }
        return PENDIENTE;
    }
}
