package com.kavak.flota.controller;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.service.VehiculoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@Validated
public class VehiculoController {

    private final VehiculoService vehiculoService;

    /**
     * Crear un nuevo vehículo
     * POST /api/vehiculos
     */
    @PostMapping
    public ResponseEntity<VehiculoDTO> crearVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO vehiculoCreado = vehiculoService.crearVehiculo(vehiculoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoCreado);
    }

    /**
     * Obtener vehículo por ID o patente
     * GET /api/vehiculos?id={id} o GET /api/vehiculos?patente={patente}
     */
    @GetMapping
    public ResponseEntity<VehiculoDTO> obtenerVehiculo(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String patente) {

        if (id != null) {
            VehiculoDTO vehiculo = vehiculoService.obtenerPorId(id);
            return ResponseEntity.ok(vehiculo);
        } else if (patente != null && !patente.isEmpty()) {
            VehiculoDTO vehiculo = vehiculoService.obtenerPorPatente(patente.toUpperCase().trim());
            return ResponseEntity.ok(vehiculo);
        } else {
            throw new IllegalArgumentException("Debe proporcionar 'id' o 'patente' como parámetro");
        }
    }

    /**
     * Obtener todos los vehículos disponibles (sin mantenimientos activos)
     * GET /api/vehiculos/disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<VehiculoDTO>> obtenerVehiculosDisponibles() {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerVehiculosDisponibles();
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Obtener todos los vehículos no disponibles (con mantenimientos activos)
     * GET /api/vehiculos/no-disponibles
     */
    @GetMapping("/no-disponibles")
    public ResponseEntity<List<VehiculoDTO>> obtenerVehiculosNoDisponibles() {
        List<VehiculoDTO> vehiculos = vehiculoService.obtenerVehiculosNoDisponibles();
        return ResponseEntity.ok(vehiculos);
    }

    /**
     * Verificar disponibilidad de un vehículo
     * GET /api/vehiculos/disponibilidad?vehiculoId={id}
     */
    @GetMapping("/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @RequestParam @Min(value = 1, message = "El ID del vehículo debe ser mayor a 0") Long vehiculoId) {
        boolean disponible = vehiculoService.verificarDisponibilidad(vehiculoId);
        return ResponseEntity.ok(disponible);
    }

    /**
     * Actualizar kilometraje por ID
     * PUT /api/vehiculos/kilometraje?id={id}&nuevoKilometraje={km}
     */
    @PutMapping("/kilometraje")
    public ResponseEntity<VehiculoDTO> actualizarKilometraje(
            @RequestParam Long id,
            @RequestParam @Min(value = 0, message = "El kilometraje no puede ser negativo") Long nuevoKilometraje) {

        VehiculoDTO vehiculoActualizado = vehiculoService.actualizarKilometraje(id, nuevoKilometraje);
        return ResponseEntity.ok(vehiculoActualizado);
    }

    /**
     * Eliminar vehículo
     * DELETE /api/vehiculos/{id}
     */
    @DeleteMapping
    public ResponseEntity<Void> eliminarVehiculo(@RequestParam Long id) {
        vehiculoService.eliminarVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
