package com.kavak.flota.service;

import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.dto.CostoTotalMantenimientosDTO;
import com.kavak.flota.entity.Mantenimiento;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.enums.Estado;
import com.kavak.flota.enums.TipoMantenimiento;
import com.kavak.flota.exception.MantenimientoNotFoundException;
import com.kavak.flota.exception.TransicionEstadoInvalidaException;
import com.kavak.flota.exception.VehiculoNotFoundException;
import com.kavak.flota.mapper.Mapper;
import com.kavak.flota.repository.MantenimientoRepository;
import com.kavak.flota.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MantenimientoService Tests")
class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private Mapper mapper;

    @Mock
    private TransicionEstadoService transicionEstadoService;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    private Vehiculo vehiculo;
    private Mantenimiento mantenimiento;
    private MantenimientoDTO mantenimientoDTO;

    @BeforeEach
    void setUp() {
        vehiculo = Vehiculo.builder()
                .id(1L)
                .patente("ABC123")
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2023)
                .kilometraje(15000L)
                .disponible(true)
                .mantenimientos(new ArrayList<>())
                .build();

        mantenimientoDTO = MantenimientoDTO.builder()
                .tipoMantenimiento("CAMBIO_ACEITE")
                .descripcion("Cambio de aceite programado")
                .costoEstimado(5000.0)
                .build();

        mantenimiento = Mantenimiento.builder()
                .id(1L)
                .tipoMantenimiento(TipoMantenimiento.CAMBIO_ACEITE)
                .descripcion("Cambio de aceite programado")
                .estado(Estado.PENDIENTE)
                .costoEstimado(5000.0)
                .vehiculo(vehiculo)
                .kilometrajeEnMantenimiento(15000L)
                .build();
    }

    @Test
    @DisplayName("Crear mantenimiento exitosamente")
    void testCrearMantenimiento() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(mantenimientoRepository.save(any(Mantenimiento.class))).thenReturn(mantenimiento);
        when(mapper.mantenimientoToDTO(mantenimiento)).thenReturn(mantenimientoDTO);

        // Act
        MantenimientoDTO resultado = mantenimientoService.crearMantenimiento(1L, mantenimientoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("CAMBIO_ACEITE", resultado.getTipoMantenimiento());
        verify(vehiculoRepository, times(1)).findById(1L);
        verify(mantenimientoRepository, times(1)).save(any(Mantenimiento.class));
    }

    @Test
    @DisplayName("Lanzar excepción cuando vehículo no existe")
    void testCrearMantenimientoVehiculoNoEncontrado() {
        // Arrange
        when(vehiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehiculoNotFoundException.class, () -> {
            mantenimientoService.crearMantenimiento(999L, mantenimientoDTO);
        });
    }

    @Test
    @DisplayName("Obtener mantenimiento por ID exitosamente")
    void testObtenerPorId() {
        // Arrange
        when(mantenimientoRepository.findById(1L)).thenReturn(Optional.of(mantenimiento));
        when(mapper.mantenimientoToDTO(mantenimiento)).thenReturn(mantenimientoDTO);

        // Act
        MantenimientoDTO resultado = mantenimientoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("CAMBIO_ACEITE", resultado.getTipoMantenimiento());
    }

    @Test
    @DisplayName("Lanzar excepción cuando mantenimiento no existe")
    void testObtenerPorIdNoEncontrado() {
        // Arrange
        when(mantenimientoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MantenimientoNotFoundException.class, () -> {
            mantenimientoService.obtenerPorId(999L);
        });
    }

    @Test
    @DisplayName("Transicionar estado de PENDIENTE a EN_PROCESO exitosamente")
    void testTransicionarEstado() {
        // Arrange
        when(mantenimientoRepository.findById(1L)).thenReturn(Optional.of(mantenimiento));
        doNothing().when(transicionEstadoService).validarTransicion(Estado.PENDIENTE, Estado.EN_PROCESO);
        when(mantenimientoRepository.save(any(Mantenimiento.class))).thenReturn(mantenimiento);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        var resultado = mantenimientoService.transicionarEstado(1L, "EN_PROCESO", null);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getMantenimientoId());
        verify(transicionEstadoService, times(1)).validarTransicion(Estado.PENDIENTE, Estado.EN_PROCESO);
    }

    @Test
    @DisplayName("Lanzar excepción cuando transición es inválida")
    void testTransicionarEstadoInvalido() {
        // Arrange
        when(mantenimientoRepository.findById(1L)).thenReturn(Optional.of(mantenimiento));
        doThrow(new TransicionEstadoInvalidaException("Transición inválida"))
                .when(transicionEstadoService).validarTransicion(any(), any());

        // Act & Assert
        assertThrows(TransicionEstadoInvalidaException.class, () -> {
            mantenimientoService.transicionarEstado(1L, "CANCELADO", null);
        });
    }

    @Test
    @DisplayName("Calcular costo total de mantenimientos completados")
    void testCalcularCostoTotalMantenimientosCompletados() {
        // Arrange
        Mantenimiento m1 = Mantenimiento.builder()
                .id(1L)
                .tipoMantenimiento(TipoMantenimiento.CAMBIO_ACEITE)
                .estado(Estado.COMPLETADO)
                .costoEstimado(5000.0)
                .costoFinal(5200.0)
                .vehiculo(vehiculo)
                .build();

        Mantenimiento m2 = Mantenimiento.builder()
                .id(2L)
                .tipoMantenimiento(TipoMantenimiento.FRENOS)
                .estado(Estado.COMPLETADO)
                .costoEstimado(8000.0)
                .costoFinal(null) // Usa costoEstimado
                .vehiculo(vehiculo)
                .build();

        List<Mantenimiento> mantenimientos = List.of(m1, m2);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(mantenimientoRepository.findMantenimientosCompletadosPorVehiculo(1L, Estado.COMPLETADO))
                .thenReturn(mantenimientos);

        // Act
        CostoTotalMantenimientosDTO resultado =
                mantenimientoService.calcularCostoTotalMantenimientosCompletados(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getVehiculoId());
        assertEquals(2, resultado.getCantidadMantenimientos());
        assertEquals(13200.0, resultado.getCostoTotal()); // 5200 + 8000
    }

    @Test
    @DisplayName("Calcular costo total sin mantenimientos completados")
    void testCalcularCostoTotalSinMantenimientos() {
        // Arrange
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(mantenimientoRepository.findMantenimientosCompletadosPorVehiculo(1L, Estado.COMPLETADO))
                .thenReturn(new ArrayList<>());

        // Act
        CostoTotalMantenimientosDTO resultado =
                mantenimientoService.calcularCostoTotalMantenimientosCompletados(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getCantidadMantenimientos());
        assertEquals(0.0, resultado.getCostoTotal());
    }

    @Test
    @DisplayName("Eliminar mantenimiento exitosamente")
    void testEliminarMantenimiento() {
        // Arrange
        when(mantenimientoRepository.findById(1L)).thenReturn(Optional.of(mantenimiento));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // Act
        mantenimientoService.eliminarMantenimiento(1L);

        // Assert
        verify(mantenimientoRepository, times(1)).deleteById(1L);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Obtener mantenimientos activos por vehículo")
    void testObtenerMantenimientosActivos() {
        // Arrange
        List<Mantenimiento> mantenimientos = List.of(mantenimiento);
        when(mantenimientoRepository.findMantenimientosActivosPorVehiculo(1L, Estado.getEstadosActivos()))
                .thenReturn(mantenimientos);
        when(mapper.mantenimientoToDTO(mantenimiento)).thenReturn(mantenimientoDTO);

        // Act
        List<MantenimientoDTO> resultado =
                mantenimientoService.obtenerMantenimientosActivosPorVehiculo(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }
}

