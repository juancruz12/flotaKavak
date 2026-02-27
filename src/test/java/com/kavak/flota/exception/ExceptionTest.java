package com.kavak.flota.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Classes Tests")
class ExceptionTest {

    @Test
    @DisplayName("VehiculoNotFoundException se lanza correctamente")
    void testVehiculoNotFoundException() {
        // Act & Assert
        VehiculoNotFoundException exception = assertThrows(VehiculoNotFoundException.class, () -> {
            throw new VehiculoNotFoundException("Vehículo no encontrado");
        });

        assertTrue(exception.getMessage().contains("Vehículo no encontrado"));
    }

    @Test
    @DisplayName("MantenimientoNotFoundException se lanza correctamente")
    void testMantenimientoNotFoundException() {
        // Act & Assert
        MantenimientoNotFoundException exception = assertThrows(MantenimientoNotFoundException.class, () -> {
            throw new MantenimientoNotFoundException("Mantenimiento no encontrado");
        });

        assertTrue(exception.getMessage().contains("Mantenimiento no encontrado"));
    }

    @Test
    @DisplayName("KilometrajeInvalidoException se lanza correctamente")
    void testKilometrajeInvalidoException() {
        // Act & Assert
        KilometrajeInvalidoException exception = assertThrows(KilometrajeInvalidoException.class, () -> {
            throw new KilometrajeInvalidoException("Kilometraje inválido");
        });

        assertTrue(exception.getMessage().contains("Kilometraje inválido"));
    }

    @Test
    @DisplayName("TransicionEstadoInvalidaException se lanza correctamente")
    void testTransicionEstadoInvalidaException() {
        // Act & Assert
        TransicionEstadoInvalidaException exception = assertThrows(TransicionEstadoInvalidaException.class, () -> {
            throw new TransicionEstadoInvalidaException("Transición no permitida");
        });

        assertTrue(exception.getMessage().contains("Transición no permitida"));
    }

    @Test
    @DisplayName("ErrorResponse se construye correctamente")
    void testErrorResponse() {
        // Arrange
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Parámetro inválido")
                .build();

        // Act & Assert
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Parámetro inválido", errorResponse.getMessage());
    }
}

