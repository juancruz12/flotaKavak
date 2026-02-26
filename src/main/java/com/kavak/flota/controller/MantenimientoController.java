package com.kavak.flota.controller;

import com.kavak.flota.dto.MantenimientoDTO;
import com.kavak.flota.service.MantenimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    /**
     * Crear un nuevo mantenimiento para un vehículo
     * POST /api/mantenimientos?vehiculoId={vehiculoId}
     */
    @PostMapping
    public ResponseEntity<MantenimientoDTO> crearMantenimiento(
            @RequestParam Long idVehiculo,
            @RequestBody MantenimientoDTO mantenimientoDTO) {
        MantenimientoDTO mantenimientoCreado = mantenimientoService.crearMantenimiento(idVehiculo, mantenimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoCreado);
    }

    /**
     * Obtener todos los mantenimientos de un vehículo por ID
     * GET /api/mantenimientos/vehiculo/{vehiculoId}
     */
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<MantenimientoDTO>> obtenerPorVehiculoId(@PathVariable Long vehiculoId) {
        List<MantenimientoDTO> mantenimientos = mantenimientoService.obtenerPorVehiculoId(vehiculoId);
        return ResponseEntity.ok(mantenimientos);
    }

    /**
     * Actualizar estado de un mantenimiento
     * PUT /api/mantenimientos/{id}/estado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<MantenimientoDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        MantenimientoDTO mantenimientoActualizado = mantenimientoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(mantenimientoActualizado);
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

