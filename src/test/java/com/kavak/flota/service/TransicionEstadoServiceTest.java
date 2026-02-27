package com.kavak.flota.service;

import com.kavak.flota.enums.Estado;
import com.kavak.flota.exception.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TransicionEstadoService Tests")
class TransicionEstadoServiceTest {

    private final TransicionEstadoService transicionEstadoService = new TransicionEstadoService();

    @Test
    @DisplayName("Transición válida: PENDIENTE -> EN_PROCESO")
    void testTransicionPendienteAEnProceso() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            transicionEstadoService.validarTransicion(Estado.PENDIENTE, Estado.EN_PROCESO);
        });
    }

    @Test
    @DisplayName("Transición válida: PENDIENTE -> CANCELADO")
    void testTransicionPendienteACancelado() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            transicionEstadoService.validarTransicion(Estado.PENDIENTE, Estado.CANCELADO);
        });
    }

    @Test
    @DisplayName("Transición válida: EN_PROCESO -> COMPLETADO")
    void testTransicionEnProcesoACompletado() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            transicionEstadoService.validarTransicion(Estado.EN_PROCESO, Estado.COMPLETADO);
        });
    }

    @Test
    @DisplayName("Transición válida: EN_PROCESO -> CANCELADO")
    void testTransicionEnProcesoACancelado() {
        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            transicionEstadoService.validarTransicion(Estado.EN_PROCESO, Estado.CANCELADO);
        });
    }

    @Test
    @DisplayName("Transición inválida: COMPLETADO -> EN_PROCESO (estado terminal)")
    void testTransicionCompletadoNoPermitida() {
        // Act & Assert
        assertThrows(TransicionEstadoInvalidaException.class, () -> {
            transicionEstadoService.validarTransicion(Estado.COMPLETADO, Estado.EN_PROCESO);
        });
    }

    @Test
    @DisplayName("Transición inválida: CANCELADO -> PENDIENTE (estado terminal)")
    void testTransicionCanceladoNoPermitida() {
        // Act & Assert
        assertThrows(TransicionEstadoInvalidaException.class, () -> {
            transicionEstadoService.validarTransicion(Estado.CANCELADO, Estado.PENDIENTE);
        });
    }

    @Test
    @DisplayName("Transición inválida: PENDIENTE -> COMPLETADO (no está permitida directamente)")
    void testTransicionPendienteACompletar() {
        // Act & Assert
        assertThrows(TransicionEstadoInvalidaException.class, () -> {
            transicionEstadoService.validarTransicion(Estado.PENDIENTE, Estado.COMPLETADO);
        });
    }

    @Test
    @DisplayName("Método esActivo() retorna true para PENDIENTE")
    void testEsActivoPendiente() {
        // Act & Assert
        assertTrue(Estado.PENDIENTE.esActivo());
    }

    @Test
    @DisplayName("Método esActivo() retorna true para EN_PROCESO")
    void testEsActivoEnProceso() {
        // Act & Assert
        assertTrue(Estado.EN_PROCESO.esActivo());
    }

    @Test
    @DisplayName("Método esActivo() retorna false para COMPLETADO")
    void testEsActivoCompletado() {
        // Act & Assert
        assertFalse(Estado.COMPLETADO.esActivo());
    }

    @Test
    @DisplayName("Método esActivo() retorna false para CANCELADO")
    void testEsActivoCancelado() {
        // Act & Assert
        assertFalse(Estado.CANCELADO.esActivo());
    }

    @Test
    @DisplayName("Método esTerminal() retorna true para COMPLETADO")
    void testEsTerminalCompletado() {
        // Act & Assert
        assertTrue(Estado.COMPLETADO.esTerminal());
    }

    @Test
    @DisplayName("Método esTerminal() retorna true para CANCELADO")
    void testEsTerminalCancelado() {
        // Act & Assert
        assertTrue(Estado.CANCELADO.esTerminal());
    }

    @Test
    @DisplayName("Método esTerminal() retorna false para PENDIENTE")
    void testEsTerminalPendiente() {
        // Act & Assert
        assertFalse(Estado.PENDIENTE.esTerminal());
    }

    @Test
    @DisplayName("getEstadosActivos() retorna lista con PENDIENTE y EN_PROCESO")
    void testGetEstadosActivos() {
        // Act
        var estadosActivos = Estado.getEstadosActivos();

        // Assert
        assertEquals(2, estadosActivos.size());
        assertTrue(estadosActivos.contains(Estado.PENDIENTE));
        assertTrue(estadosActivos.contains(Estado.EN_PROCESO));
        assertFalse(estadosActivos.contains(Estado.COMPLETADO));
        assertFalse(estadosActivos.contains(Estado.CANCELADO));
    }
}

