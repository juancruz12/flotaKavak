package com.kavak.flota.exception;

public class VehiculoNotFoundException extends RuntimeException {
    public VehiculoNotFoundException(String mensaje) {
        super(mensaje);
    }
}

