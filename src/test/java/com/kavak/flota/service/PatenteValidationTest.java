package com.kavak.flota.service;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.exception.PatenteYaExisteException;
import com.kavak.flota.mapper.Mapper;
import com.kavak.flota.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Validación de Patente Duplicada")
class PatenteValidationTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoDTO;
    private Vehiculo vehiculoExistente;

    @BeforeEach
    void setUp() {
        vehiculoDTO = VehiculoDTO.builder()
                .patente("ABC123")
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2023)
                .kilometraje(15000L)
                .build();

        vehiculoExistente = Vehiculo.builder()
                .id(1L)
                .patente("ABC123")
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2023)
                .kilometraje(15000L)
                .disponible(true)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Lanzar excepción cuando patente ya existe")
    void testCrearVehiculoConPatenteExistente() {
        // Arrange - Simular que la patente ya existe
        when(vehiculoRepository.findByPatente("ABC123"))
                .thenReturn(Optional.of(vehiculoExistente));

        // Act & Assert
        PatenteYaExisteException exception = assertThrows(PatenteYaExisteException.class, () -> {
            vehiculoService.crearVehiculo(vehiculoDTO);
        });

        // Verificar mensaje
        assertTrue(exception.getMessage().contains("ABC123"));

        // Verificar que nunca intentó guardar
        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crear vehículo exitosamente cuando patente no existe")
    void testCrearVehiculoConPatenteNoExistente() {
        // Arrange
        Vehiculo vehiculoNuevo = Vehiculo.builder()
                .id(2L)
                .patente("ABC123")
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2023)
                .kilometraje(15000L)
                .disponible(true)
                .build();

        when(vehiculoRepository.findByPatente("ABC123")).thenReturn(Optional.empty());
        when(mapper.vehiculoDtoToEntity(vehiculoDTO)).thenReturn(vehiculoNuevo);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculoNuevo);
        when(mapper.vehiculoToDto(vehiculoNuevo)).thenReturn(vehiculoDTO);

        // Act
        VehiculoDTO resultado = vehiculoService.crearVehiculo(vehiculoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.getPatente());
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }
}

