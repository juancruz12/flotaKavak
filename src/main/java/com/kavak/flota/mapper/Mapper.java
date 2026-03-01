package com.kavak.flota.mapper;

import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.entity.Mantenimiento;
import com.kavak.flota.entity.Vehiculo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public VehiculoDTO vehiculoToDto(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }

        List<MantenimientoDTO> mantenimientosDTO = vehiculo.getMantenimientos() != null
                ? vehiculo.getMantenimientos().stream()
                .map(this::mantenimientoToDTO)
                .collect(Collectors.toList())
                : null;

        return VehiculoDTO.builder()
                .id(vehiculo.getId())
                .patente(vehiculo.getPatente())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .kilometraje(vehiculo.getKilometraje())
                .disponible(vehiculo.getDisponible())
                .fechaCreacion(vehiculo.getFechaCreacion())
                .fechaActualizacion(vehiculo.getFechaActualizacion())
                .mantenimientos(mantenimientosDTO)
                .build();
    }

    public MantenimientoDTO mantenimientoToDTO(Mantenimiento mantenimiento) {
        if (mantenimiento == null) {
            return null;
        }

        return MantenimientoDTO.builder()
                .id(mantenimiento.getId())
                .tipoMantenimiento(mantenimiento.getTipoMantenimiento().toString())
                .descripcion(mantenimiento.getDescripcion())
                .kilometrajeEnMantenimiento(mantenimiento.getKilometrajeEnMantenimiento())
                .estado(mantenimiento.getEstado().toString())
                .costoEstimado(mantenimiento.getCostoEstimado())
                .costoFinal(mantenimiento.getCostoFinal())
                .fechaCreacion(mantenimiento.getFechaCreacion())
                .fechaActualizacion(mantenimiento.getFechaActualizacion())
                .vehiculoId(mantenimiento.getVehiculo().getId())
                .build();
    }

    public Vehiculo vehiculoDtoToEntity(VehiculoDTO dto) {
        if (dto == null) {
            return null;
        }

        return Vehiculo.builder()
                .id(dto.getId())
                .patente(dto.getPatente().toUpperCase().trim())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .kilometraje(dto.getKilometraje())
                .disponible(true)
                .fechaCreacion(dto.getFechaCreacion())
                .fechaActualizacion(dto.getFechaActualizacion())
                .build();
    }
}

