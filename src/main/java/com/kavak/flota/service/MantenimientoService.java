package com.kavak.flota.service;

import com.kavak.flota.dto.CostoTotalMantenimientosDTO;
import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.dto.TransicionEstadoResponseDTO;
import com.kavak.flota.entity.Mantenimiento;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.enums.Estado;
import com.kavak.flota.enums.TipoMantenimiento;
import com.kavak.flota.exception.MantenimientoNotFoundException;
import com.kavak.flota.exception.VehiculoNotFoundException;
import com.kavak.flota.mapper.Mapper;
import com.kavak.flota.repository.MantenimientoRepository;
import com.kavak.flota.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final Mapper mapper;
    private final TransicionEstadoService transicionEstadoService;

    /**
     * Crear un nuevo mantenimiento para un vehículo
     */
    @Transactional
    public MantenimientoDTO crearMantenimiento(Long idVehiculo, MantenimientoDTO mantenimientoDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con ID " + idVehiculo + " no encontrado"));

        Mantenimiento mantenimiento = Mantenimiento.builder()
                .tipoMantenimiento(TipoMantenimiento.valueOf(mantenimientoDTO.getTipoMantenimiento()))
                .descripcion(mantenimientoDTO.getDescripcion())
                .kilometrajeEnMantenimiento(vehiculo.getKilometraje())
                .estado(Estado.PENDIENTE)
                .costoEstimado(mantenimientoDTO.getCostoEstimado())
                .costoFinal(mantenimientoDTO.getCostoFinal())
                .vehiculo(vehiculo)
                .build();

        Mantenimiento mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);

        return mapper.mantenimientoToDTO(mantenimientoGuardado);
    }

    /**
     * Obtener todos los mantenimientos de un vehículo por ID
     */
    public List<MantenimientoDTO> obtenerPorVehiculoId(Long vehiculoId) {
        return mantenimientoRepository.findByVehiculoId(vehiculoId)
                .stream()
                .map(mapper::mantenimientoToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los mantenimientos activos de un vehículo por ID
     * Mantenimientos activos: PENDIENTE, EN_PROCESO (definidos en Estado.getEstadosActivos())
     */
    public List<MantenimientoDTO> obtenerMantenimientosActivosPorVehiculo(Long vehiculoId) {
        return mantenimientoRepository.findMantenimientosActivosPorVehiculo(
                        vehiculoId,
                        Estado.getEstadosActivos())
                .stream()
                .map(mapper::mantenimientoToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un mantenimiento por ID
     */
    public MantenimientoDTO obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .map(mapper::mantenimientoToDTO)
                .orElseThrow(() -> new MantenimientoNotFoundException(
                        "Mantenimiento con ID " + id + " no encontrado"));
    }


    /**
     * Transicionar el estado de un mantenimiento validando las reglas
     */
    @Transactional
    public TransicionEstadoResponseDTO transicionarEstado(Long id, String nuevoEstadoStr, Double costoFinal) {

        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new MantenimientoNotFoundException(
                        "Mantenimiento con ID " + id + " no encontrado"));

        String anteriorEstado = mantenimiento.getEstado().toString();
        Estado nuevoEstado = Estado.valueOf(nuevoEstadoStr);

        // Validar la transición usando el servicio especializado
        transicionEstadoService.validarTransicion(mantenimiento.getEstado(), nuevoEstado);

        // Si la validación pasó, actualizar el estado
        mantenimiento.setEstado(nuevoEstado);

        if(nuevoEstado.equals(Estado.COMPLETADO)) {
            mantenimiento.setCostoFinal(costoFinal);
        }

        mantenimientoRepository.save(mantenimiento);
        mantenimientoRepository.flush();

        return TransicionEstadoResponseDTO.builder()
                .mantenimientoId(id)
                .estadoAnterior(anteriorEstado)
                .estadoNuevo(nuevoEstadoStr)
                .mensaje("Transición exitosa de " + anteriorEstado +
                        " a " + nuevoEstadoStr)
                .build();
    }

    /**
    /**
     * Eliminar mantenimiento por ID
     */
    @Transactional
    public void eliminarMantenimiento(Long id) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new MantenimientoNotFoundException(
                        "Mantenimiento con ID " + id + " no encontrado"));

        Vehiculo vehiculo = mantenimiento.getVehiculo();
        mantenimientoRepository.deleteById(id);
    }

    /**
     * Calcular costo total de mantenimientos completados de un vehículo
     * Prioriza costoFinal, si no existe usa costoEstimado
     */
    public CostoTotalMantenimientosDTO calcularCostoTotalMantenimientosCompletados(Long vehiculoId) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con ID " + vehiculoId + " no encontrado"));

        List<Mantenimiento> mantenimientosCompletados =
                mantenimientoRepository.findMantenimientosCompletadosPorVehiculo(vehiculoId, Estado.COMPLETADO);

        Double costoTotal = mantenimientosCompletados.stream()
                .mapToDouble(m -> m.getCostoFinal() != null ? m.getCostoFinal() : m.getCostoEstimado())
                .sum();

        return CostoTotalMantenimientosDTO.builder()
                .vehiculoId(vehiculoId)
                .patente(vehiculo.getPatente())
                .cantidadMantenimientos(mantenimientosCompletados.size())
                .costoTotal(costoTotal)
                .build();
    }

}
