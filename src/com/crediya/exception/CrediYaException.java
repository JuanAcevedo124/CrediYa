package com.crediya.exception;

/** Excepción base del dominio. */
public class CrediYaException extends Exception {
    public CrediYaException(String mensaje) { super(mensaje); }
    public CrediYaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
