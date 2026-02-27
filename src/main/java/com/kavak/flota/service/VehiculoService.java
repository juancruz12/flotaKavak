package com.kavak.flota.service;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.exception.KilometrajeInvalidoException;
import com.kavak.flota.exception.VehiculoNotFoundException;
import com.kavak.flota.mapper.Mapper;
import com.kavak.flota.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final Mapper mapper;

    /**
     * Crear un nuevo vehículo
     */
    @Transactional
    public VehiculoDTO crearVehiculo(VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = mapper.vehiculoDtoToEntity(vehiculoDTO);
        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);
        return mapper.vehiculoToDto(vehiculoGuardado);
    }

    /**
     * Obtener vehículo por ID
     */
    public VehiculoDTO obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .map(mapper::vehiculoToDto)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con ID " + id + " no encontrado"));
    }

    /**
     * Obtener vehículo por patente
     */
    public VehiculoDTO obtenerPorPatente(String patente) {
        return vehiculoRepository.findByPatente(patente)
                .map(mapper::vehiculoToDto)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con patente " + patente + " no encontrado"));
    }

    /**
     * Actualizar kilometraje de un vehículo por patente
     */
    @Transactional
    public VehiculoDTO actualizarKilometraje(Long id, Long nuevoKilometraje) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(
                        "Vehículo con ID " + id + " no encontrado"));

        if (nuevoKilometraje < vehiculo.getKilometraje()) {
            throw new KilometrajeInvalidoException(
                    "El nuevo kilometraje (" + nuevoKilometraje +
                    ") no puede ser menor que el actual (" + vehiculo.getKilometraje() + ")");
        }

        vehiculo.setKilometraje(nuevoKilometraje);
        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);
        return mapper.vehiculoToDto(vehiculoActualizado);
    }

    /**
     * Obtener todos los vehículos disponibles (sin mantenimientos activos)
     */
    public List<VehiculoDTO> obtenerVehiculosDisponibles() {
        return vehiculoRepository.findByDisponibleTrue()
                .stream()
                .map(mapper::vehiculoToDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todos los vehículos no disponibles (con mantenimientos activos)
     */
    public List<VehiculoDTO> obtenerVehiculosNoDisponibles() {
        return vehiculoRepository.findByDisponibleFalse()
                .stream()
                .map(mapper::vehiculoToDto)
                .collect(Collectors.toList());
    }

    /**
     * Verificar si un vehículo está disponible por ID
     */
    public boolean verificarDisponibilidad(Long id) {
        return vehiculoRepository.findById(id)
                .map(Vehiculo::getDisponible)
                .orElse(false);
    }

    /**
     * Eliminar vehículo por ID
     */
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}

