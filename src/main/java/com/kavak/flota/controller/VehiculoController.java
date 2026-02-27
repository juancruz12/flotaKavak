package com.kavak.flota.controller;

import com.kavak.flota.dto.VehiculoDTO;
import com.kavak.flota.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    /**
     * Crear un nuevo vehículo
     * POST /api/vehiculos
     */
    @PostMapping
    public ResponseEntity<VehiculoDTO> crearVehiculo(@RequestBody VehiculoDTO vehiculoDTO) {
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
            VehiculoDTO vehiculo = vehiculoService.obtenerPorPatente(patente);
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
     * GET /api/vehiculos/{id}/disponibilidad
     */
    @GetMapping("/{id}/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable Long id) {
        boolean disponible = vehiculoService.verificarDisponibilidad(id);
        return ResponseEntity.ok(disponible);
    }

    /**
     * Actualizar kilometraje por ID o patente
     * PUT /api/vehiculos/kilometraje?id={id}&nuevoKilometraje={km}
     * o PUT /api/vehiculos/kilometraje?patente={patente}&nuevoKilometraje={km}
     */
    @PutMapping("/kilometraje")
    public ResponseEntity<VehiculoDTO> actualizarKilometraje(
            @RequestParam(required = true) Long id,
            @RequestParam Long nuevoKilometraje) {

        if (id != null) {
            VehiculoDTO vehiculoActualizado = vehiculoService.actualizarKilometraje(id, nuevoKilometraje);
            return ResponseEntity.ok(vehiculoActualizado);
        } else {
            throw new IllegalArgumentException("Debe proporcionar 'id' del vehiculo como parámetro");
        }
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
