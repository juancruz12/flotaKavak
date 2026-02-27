package com.kavak.flota.service;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.exception.KilometrajeInvalidoException;
import com.kavak.flota.exception.VehiculoNotFoundException;
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
@DisplayName("VehiculoService Tests")
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoDTO;
    private Vehiculo vehiculo;

    @BeforeEach
    void setUp() {
        vehiculoDTO = VehiculoDTO.builder()
                .patente("ABC123")
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2023)
                .kilometraje(15000L)
                .build();

        vehiculo = Vehiculo.builder()
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
    @DisplayName("Crear vehículo exitosamente")
    void testCrearVehiculo() {
        // Arrange
        when(mapper.vehiculoDtoToEntity(vehiculoDTO)).thenReturn(vehiculo);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(mapper.vehiculoToDto(vehiculo)).thenReturn(vehiculoDTO);

        // Act
        VehiculoDTO resultado = vehiculoService.crearVehiculo(vehiculoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.getPatente());
        assertEquals("Toyota", resultado.getMarca());
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Obtener vehículo por ID exitosamente")
    void testObtenerPorId() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(mapper.vehiculoToDto(vehiculo)).thenReturn(vehiculoDTO);

        // Act
        VehiculoDTO resultado = vehiculoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.getPatente());
        verify(vehiculoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Lanzar excepción cuando vehículo no existe (ID)")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(vehiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehiculoNotFoundException.class, () -> {
            vehiculoService.obtenerPorId(999L);
        });
    }

    @Test
    @DisplayName("Obtener vehículo por patente exitosamente")
    void testObtenerPorPatente() {
        // Arrange
        when(vehiculoRepository.findByPatente("ABC123")).thenReturn(Optional.of(vehiculo));
        when(mapper.vehiculoToDto(vehiculo)).thenReturn(vehiculoDTO);

        // Act
        VehiculoDTO resultado = vehiculoService.obtenerPorPatente("ABC123");

        // Assert
        assertNotNull(resultado);
        assertEquals("ABC123", resultado.getPatente());
    }

    @Test
    @DisplayName("Actualizar kilometraje exitosamente")
    void testActualizarKilometraje() {
        // Arrange
        Long nuevoKilometraje = 20000L;
        vehiculo.setKilometraje(nuevoKilometraje);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(mapper.vehiculoToDto(vehiculo)).thenReturn(vehiculoDTO);

        // Act
        VehiculoDTO resultado = vehiculoService.actualizarKilometraje(1L, nuevoKilometraje);

        // Assert
        assertNotNull(resultado);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando nuevo kilometraje es menor al actual")
    void testActualizarKilometrajeInvalido() {
        // Arrange
        Long nuevoKilometraje = 10000L; // Menor al actual (15000)
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act & Assert
        assertThrows(KilometrajeInvalidoException.class, () -> {
            vehiculoService.actualizarKilometraje(1L, nuevoKilometraje);
        });
    }

    @Test
    @DisplayName("Verificar disponibilidad de vehículo")
    void testVerificarDisponibilidad() {
        // Arrange
        vehiculo.setDisponible(true);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act
        boolean resultado = vehiculoService.verificarDisponibilidad(1L);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Verificar que vehículo no disponible retorna false")
    void testVerificarDisponibilidadFalse() {
        // Arrange
        vehiculo.setDisponible(false);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));

        // Act
        boolean resultado = vehiculoService.verificarDisponibilidad(1L);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Eliminar vehículo exitosamente")
    void testEliminarVehiculo() {
        // Act
        vehiculoService.eliminarVehiculo(1L);

        // Assert
        verify(vehiculoRepository, times(1)).deleteById(1L);
    }
}

