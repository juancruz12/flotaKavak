package com.kavak.flota.service;

import com.kavak.flota.enums.Estado;
import com.kavak.flota.exception.TransicionEstadoInvalidaException;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar transiciones de estados de mantenimientos.
 * Implementa el State Pattern para mantener la lógica de transiciones de forma escalable.
 */
@Service
public class TransicionEstadoService {

    /**
     * Valida y ejecuta la transición de un estado a otro
     * @param estadoActual Estado actual del mantenimiento
     * @param estadoNuevo Estado al que se desea transicionar
     * @throws TransicionEstadoInvalidaException si la transición no es válida
     */
    public void validarTransicion(Estado estadoActual, Estado estadoNuevo) {
        // Verificar si el estado actual es terminal
        if (estadoActual.esTerminal()) {
            throw new TransicionEstadoInvalidaException(
                    String.format("No se puede cambiar el estado de un mantenimiento %s. " +
                            "Los estados COMPLETADO y CANCELADO son finales.", estadoActual)
            );
        }

        // Verificar si la transición es permitida
        if (!estadoActual.puedeTransicionarA(estadoNuevo)) {
            throw new TransicionEstadoInvalidaException(
                    String.format("Transición inválida: no se puede pasar de %s a %s. " +
                            "Estados permitidos desde %s: %s",
                            estadoActual, estadoNuevo, estadoActual, estadoActual.getEstadosPermitidos())
            );
        }
    }
}

