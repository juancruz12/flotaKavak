package com.kavak.flota.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransicionEstadoResponseDTO {
    private Long mantenimientoId;
    private String estadoAnterior;
    private String estadoNuevo;
    private String mensaje;
}

