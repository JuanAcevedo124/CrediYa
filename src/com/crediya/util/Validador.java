package com.crediya.util;

import com.crediya.exception.ValidacionException;

/** Validaciones centralizadas que el profe estricto sí prueba. */
public final class Validador {
    private Validador() {}

    public static void noVacio(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException(campo + " no puede estar vacio.");
    }

    public static void positivo(double valor, String campo) throws ValidacionException {
        if (valor <= 0) throw new ValidacionException(campo + " debe ser mayor a 0.");
    }

    public static void positivo(int valor, String campo) throws ValidacionException {
        if (valor <= 0) throw new ValidacionException(campo + " debe ser mayor a 0.");
    }

    public static void noNegativo(double valor, String campo) throws ValidacionException {
        if (valor < 0) throw new ValidacionException(campo + " no puede ser negativo.");
    }
}
