package com.kavak.flota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MantenimientoDTO {
    private Long id;
    private String tipoMantenimiento;
    private String descripcion;
    private Long kilometrajeEnMantenimiento;
    private String estado;
    private Double costoEstimado;
    private Double costoReal;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}

