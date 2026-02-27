package com.kavak.flota.service;

import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.dto.TransicionEstadoResponseDTO;
import com.kavak.flota.entity.Mantenimiento;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.enums.Estado;
import com.kavak.flota.enums.TipoMantenimiento;
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
    public MantenimientoDTO crearMantenimiento(Long idVehiculo, MantenimientoDTO mantenimientoDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + idVehiculo));

        Mantenimiento mantenimiento = Mantenimiento.builder()
                .tipoMantenimiento(TipoMantenimiento.valueOf(mantenimientoDTO.getTipoMantenimiento()))
                .descripcion(mantenimientoDTO.getDescripcion())
                .kilometrajeEnMantenimiento(vehiculo.getKilometraje())
                .estado(Estado.PENDIENTE)
                .costoEstimado(mantenimientoDTO.getCostoEstimado())
                .costoReal(mantenimientoDTO.getCostoReal())
                .vehiculo(vehiculo)
                .build();

        Mantenimiento mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);
        return mapper.mantenimientoToDTO(mantenimientoGuardado);
    }

    /**
     * Obtener todos los mantenimientos de un vehículo por ID
     */
    @Transactional(readOnly = true)
    public List<MantenimientoDTO> obtenerPorVehiculoId(Long vehiculoId) {
        return mantenimientoRepository.findByVehiculoId(vehiculoId)
                .stream()
                .map(mapper::mantenimientoToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los mantenimientos de un vehículo por patente
     */
    @Transactional(readOnly = true)
    public List<MantenimientoDTO> obtenerPorVehiculoPatente(String patente) {
        return mantenimientoRepository.findByVehiculoPatente(patente)
                .stream()
                .map(mapper::mantenimientoToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un mantenimiento por ID
     */
    @Transactional(readOnly = true)
    public MantenimientoDTO obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .map(mapper::mantenimientoToDTO)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + id));
    }


    /**
     * Transicionar el estado de un mantenimiento validando las reglas
     */
    public TransicionEstadoResponseDTO transicionarEstado(Long id, String nuevoEstadoStr) {

        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + id));

        String anteriorEstado = mantenimiento.getEstado().toString();
        Estado nuevoEstado = Estado.valueOf(nuevoEstadoStr);

        // Validar la transición usando el servicio especializado
        transicionEstadoService.validarTransicion(mantenimiento.getEstado(), nuevoEstado);

        // Si la validación pasó, actualizar el estado
        mantenimiento.setEstado(nuevoEstado);
        Mantenimiento mantenimientoActualizado = mantenimientoRepository.save(mantenimiento);

        return TransicionEstadoResponseDTO.builder()
                .mantenimientoId(id)
                .estadoAnterior(anteriorEstado)
                .estadoNuevo(nuevoEstadoStr)
                .mensaje("Transición exitosa de " + anteriorEstado +
                        " a " + nuevoEstadoStr)
                .build();
    }

    /**
     * Actualizar costo real de un mantenimiento
     */
    public MantenimientoDTO actualizarCostoReal(Long id, Double costoReal) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado con ID: " + id));

        mantenimiento.setCostoReal(costoReal);
        Mantenimiento mantenimientoActualizado = mantenimientoRepository.save(mantenimiento);
        return mapper.mantenimientoToDTO(mantenimientoActualizado);
    }

    /**
     * Eliminar mantenimiento por ID
     */
    public void eliminarMantenimiento(Long id) {
        mantenimientoRepository.deleteById(id);
    }
}

