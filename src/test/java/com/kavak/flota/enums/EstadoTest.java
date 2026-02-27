package com.kavak.flota.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Estado Enum Tests")
class EstadoTest {

    @Test
    @DisplayName("PENDIENTE puede transicionar a EN_PROCESO")
    void testPendientePuedeTransicionarAEnProceso() {
        assertTrue(Estado.PENDIENTE.puedeTransicionarA(Estado.EN_PROCESO));
    }

    @Test
    @DisplayName("PENDIENTE puede transicionar a CANCELADO")
    void testPendientePuedeTransicionarACancelado() {
        assertTrue(Estado.PENDIENTE.puedeTransicionarA(Estado.CANCELADO));
    }

    @Test
    @DisplayName("PENDIENTE no puede transicionar a COMPLETADO")
    void testPendienteNoPuedeTransicionarACompletado() {
        assertFalse(Estado.PENDIENTE.puedeTransicionarA(Estado.COMPLETADO));
    }

    @Test
    @DisplayName("EN_PROCESO puede transicionar a COMPLETADO")
    void testEnProcesoPuedeTransicionarACompletado() {
        assertTrue(Estado.EN_PROCESO.puedeTransicionarA(Estado.COMPLETADO));
    }

    @Test
    @DisplayName("EN_PROCESO puede transicionar a CANCELADO")
    void testEnProcesoPuedeTransicionarACancelado() {
        assertTrue(Estado.EN_PROCESO.puedeTransicionarA(Estado.CANCELADO));
    }

    @Test
    @DisplayName("EN_PROCESO no puede transicionar a PENDIENTE")
    void testEnProcesoNoPuedeTransicionarAPendiente() {
        assertFalse(Estado.EN_PROCESO.puedeTransicionarA(Estado.PENDIENTE));
    }

    @Test
    @DisplayName("COMPLETADO no puede transicionar a ningún estado")
    void testCompletadoNoTransiciona() {
        assertFalse(Estado.COMPLETADO.puedeTransicionarA(Estado.PENDIENTE));
        assertFalse(Estado.COMPLETADO.puedeTransicionarA(Estado.EN_PROCESO));
        assertFalse(Estado.COMPLETADO.puedeTransicionarA(Estado.CANCELADO));
    }

    @Test
    @DisplayName("CANCELADO no puede transicionar a ningún estado")
    void testCanceladoNoTransiciona() {
        assertFalse(Estado.CANCELADO.puedeTransicionarA(Estado.PENDIENTE));
        assertFalse(Estado.CANCELADO.puedeTransicionarA(Estado.EN_PROCESO));
        assertFalse(Estado.CANCELADO.puedeTransicionarA(Estado.COMPLETADO));
    }

    @Test
    @DisplayName("getEstadosPermitidos() retorna lista correcta para PENDIENTE")
    void testGetEstadosPermitidosPendiente() {
        // Act
        var estadosPermitidos = Estado.PENDIENTE.getEstadosPermitidos();

        // Assert
        assertEquals(2, estadosPermitidos.size());
        assertTrue(estadosPermitidos.contains(Estado.EN_PROCESO));
        assertTrue(estadosPermitidos.contains(Estado.CANCELADO));
    }

    @Test
    @DisplayName("getEstadosPermitidos() retorna lista correcta para EN_PROCESO")
    void testGetEstadosPermitidosEnProceso() {
        // Act
        var estadosPermitidos = Estado.EN_PROCESO.getEstadosPermitidos();

        // Assert
        assertEquals(2, estadosPermitidos.size());
        assertTrue(estadosPermitidos.contains(Estado.COMPLETADO));
        assertTrue(estadosPermitidos.contains(Estado.CANCELADO));
    }

    @Test
    @DisplayName("getEstadosPermitidos() retorna lista vacía para COMPLETADO")
    void testGetEstadosPermitidosCompletado() {
        // Act
        var estadosPermitidos = Estado.COMPLETADO.getEstadosPermitidos();

        // Assert
        assertTrue(estadosPermitidos.isEmpty());
    }

    @Test
    @DisplayName("getEstadosPermitidos() retorna lista vacía para CANCELADO")
    void testGetEstadosPermitidosCancelado() {
        // Act
        var estadosPermitidos = Estado.CANCELADO.getEstadosPermitidos();

        // Assert
        assertTrue(estadosPermitidos.isEmpty());
    }
}

