package com.kavak.flota.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(regexp = "^[A-Z0-9]{6,7}$", message = "Formato de patente inválido (debe tener 6-7 caracteres alfanuméricos en mayúsculas)")
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 1, max = 50, message = "La marca debe tener entre 1 y 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(min = 1, max = 50, message = "El modelo debe tener entre 1 y 50 caracteres")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    @Max(value = 2100, message = "El año no puede ser superior a 3000")
    private Integer anio;

    @NotNull(message = "El kilometraje es obligatorio")
    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Long kilometraje;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean disponible;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MantenimientoDTO> mantenimientos;
}

