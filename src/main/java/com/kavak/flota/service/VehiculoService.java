package com.kavak.flota.service;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.entity.Vehiculo;
import com.kavak.flota.mapper.Mapper;
import com.kavak.flota.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public VehiculoDTO crearVehiculo(VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = mapper.vehiculoDtoToEntity(vehiculoDTO);
        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);
        return mapper.vehiculoToDto(vehiculoGuardado);
    }

    /**
     * Obtener vehículo por ID
     */
    @Transactional(readOnly = true)
    public VehiculoDTO obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .map(mapper::vehiculoToDto)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    /**
     * Obtener vehículo por patente
     */
    @Transactional(readOnly = true)
    public VehiculoDTO obtenerPorPatente(String patente) {
        return vehiculoRepository.findByPatente(patente)
                .map(mapper::vehiculoToDto)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con patente: " + patente));
    }

    /**
     * Actualizar kilometraje de un vehículo por patente
     */
    public VehiculoDTO actualizarKilometraje(Long id, Long nuevoKilometraje) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));

        if (nuevoKilometraje < vehiculo.getKilometraje()) {
            throw new RuntimeException("El nuevo kilometraje no puede ser menor que el actual");
        }

        vehiculo.setKilometraje(nuevoKilometraje);
        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);
        return mapper.vehiculoToDto(vehiculoActualizado);
    }

    /**
     * Eliminar vehículo por ID
     */
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}

