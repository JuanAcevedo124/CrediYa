package com.crediya.exception;

/** Error de validación (datos inválidos). La usa Validador y el Service. */
public class ValidacionException extends CrediYaException {
    public ValidacionException(String mensaje) { super(mensaje); }
}
