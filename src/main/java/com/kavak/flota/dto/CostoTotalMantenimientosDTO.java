package com.kavak.flota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostoTotalMantenimientosDTO {
    private Long vehiculoId;
    private String patente;
    private Integer cantidadMantenimientos;
    private Double costoTotal;
}

