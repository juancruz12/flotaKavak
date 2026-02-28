package com.kavak.flota.exception;

public class TransicionEstadoInvalidaException extends RuntimeException {
    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }

    public TransicionEstadoInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

