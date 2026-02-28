package com.kavak.flota.controller;

import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.dto.TransicionEstadoResponseDTO;
import com.kavak.flota.dto.CostoTotalMantenimientosDTO;
import com.kavak.flota.service.MantenimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
@Validated
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    /**
     * Crear un nuevo mantenimiento para un vehículo
     * POST /api/mantenimientos?idVehiculo={vehiculoId}
     */
    @PostMapping
    public ResponseEntity<MantenimientoDTO> crearMantenimiento(
            @RequestParam Long idVehiculo,
            @Valid @RequestBody MantenimientoDTO mantenimientoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                mantenimientoService.crearMantenimiento(idVehiculo, mantenimientoDTO));
    }

    /**
     * Obtener todos los mantenimientos de un vehículo por ID
     * GET /api/mantenimientos/vehiculo/{vehiculoId}
     */
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<MantenimientoDTO>> obtenerPorVehiculoId(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(mantenimientoService.obtenerPorVehiculoId(vehiculoId));
    }

    /**
     * Obtener todos los mantenimientos ACTIVOS de un vehículo por ID
     * Estados activos: PENDIENTE, EN_PROCESO
     * GET /api/mantenimientos/vehiculo/{vehiculoId}/activos
     */
    @GetMapping("/vehiculo/{vehiculoId}/activos")
    public ResponseEntity<List<MantenimientoDTO>> obtenerMantenimientosActivosPorVehiculo(
            @PathVariable Long vehiculoId) {
        return ResponseEntity.ok(mantenimientoService.obtenerMantenimientosActivosPorVehiculo(vehiculoId));
    }

    /**
     * Transicionar el estado de un mantenimiento
     * PUT /api/mantenimientos/{id}/transicionar?nuevoEstado={estado}
     *
     * Estados válidos: PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO
     *
     * Flujo permitido:
     * - PENDIENTE → EN_PROCESO o CANCELADO
     * - EN_PROCESO → COMPLETADO o CANCELADO
     * - COMPLETADO y CANCELADO son estados finales (no permiten transiciones)
     */
    @PutMapping("/{id}/transicionar")
    public ResponseEntity<TransicionEstadoResponseDTO> transicionarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam(required = false) Double costoFinal) {
        return ResponseEntity.ok(mantenimientoService.transicionarEstado(id, nuevoEstado, costoFinal));
    }

    /**
     * Calcular costo total de mantenimientos completados de un vehículo por ID
     * GET /api/mantenimientos/vehiculo/{vehiculoId}/costo-total
     */
    @GetMapping("/vehiculo/{vehiculoId}/costo-total")
    public ResponseEntity<CostoTotalMantenimientosDTO> calcularCostoTotalMantenimientosCompletados(
            @PathVariable Long vehiculoId) {
        return ResponseEntity.ok(
                mantenimientoService.calcularCostoTotalMantenimientosCompletados(vehiculoId));
    }

    /**
     * Eliminar mantenimiento
     * DELETE /api/mantenimientos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMantenimiento(@PathVariable Long id) {
        mantenimientoService.eliminarMantenimiento(id);
        return ResponseEntity.noContent().build();
    }
}

