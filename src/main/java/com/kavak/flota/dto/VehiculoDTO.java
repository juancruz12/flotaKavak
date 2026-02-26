package com.kavak.flota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDTO {
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private Long kilometraje;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<MantenimientoDTO> mantenimientos;
}

