package com.kavak.flota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "El tipo de mantenimiento es obligatorio")
    private String tipoMantenimiento;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 5, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long kilometrajeEnMantenimiento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estado;

    @NotNull(message = "El costo estimado es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo estimado debe ser mayor a 0")
    private Double costoEstimado;

    @DecimalMin(value = "0.0", inclusive = false, message = "El costo final debe ser mayor a 0")
    private Double costoFinal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaActualizacion;
}

