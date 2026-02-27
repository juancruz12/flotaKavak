# ✅ Endpoint: Obtener Mantenimientos Activos

## 📌 Descripción

Nuevo endpoint para obtener todos los mantenimientos **activos** (no finalizados) de un vehículo a partir de su `idVehiculo`.

**Estados activos:** `PENDIENTE`, `EN_PROCESO`  
**Estados finales:** `COMPLETADO`, `CANCELADO`

---

## 🚀 Endpoint

```
GET /api/mantenimientos/vehiculo/{vehiculoId}/activos
```

---

## 📝 Parámetros

| Parámetro | Tipo | Ubicación | Requerido | Descripción |
|-----------|------|-----------|-----------|-------------|
| `vehiculoId` | Long | Path | ✅ Sí | ID del vehículo |

---

## 💡 Ejemplo de Uso

### **Request:**
```bash
curl http://localhost:8087/api/mantenimientos/vehiculo/1/activos
```

### **Response (200 OK):**
```json
[
  {
    "id": 1,
    "tipoMantenimiento": "CAMBIO_ACEITE",
    "descripcion": "Cambio de aceite programado",
    "kilometrajeEnMantenimiento": 15000,
    "estado": "PENDIENTE",
    "costoEstimado": 5000,
    "costoFinal": null,
    "fechaCreacion": "2026-02-27T10:00:00",
    "fechaActualizacion": "2026-02-27T10:00:00"
  },
  {
    "id": 3,
    "tipoMantenimiento": "FRENOS",
    "descripcion": "Revisión de frenos",
    "kilometrajeEnMantenimiento": 15000,
    "estado": "EN_PROCESO",
    "costoEstimado": 8000,
    "costoFinal": null,
    "fechaCreacion": "2026-02-27T11:30:00",
    "fechaActualizacion": "2026-02-27T12:00:00"
  }
]
```

---

## 📊 Casos de Uso

### **Caso 1: Vehículo sin mantenimientos activos**
```bash
GET /api/mantenimientos/vehiculo/5/activos
```

**Response (200 OK):**
```json
[]
```

---

### **Caso 2: Vehículo con múltiples mantenimientos activos**
```bash
GET /api/mantenimientos/vehiculo/1/activos
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "estado": "PENDIENTE",
    "tipoMantenimiento": "CAMBIO_ACEITE"
  },
  {
    "id": 2,
    "estado": "EN_PROCESO",
    "tipoMantenimiento": "MOTOR"
  }
]
```

---

### **Caso 3: Vehículo no existe**
```bash
GET /api/mantenimientos/vehiculo/999/activos
```

**Response (200 OK):**
```json
[]
```

*Nota: Devuelve lista vacía si el vehículo no existe o no tiene mantenimientos activos.*

---

## 🔄 Diferencia con otros endpoints

| Endpoint | Descripción | Estados incluidos |
|----------|-------------|------------------|
| `GET /api/mantenimientos/vehiculo/{id}` | Todos los mantenimientos | PENDIENTE, EN_PROCESO, COMPLETADO, CANCELADO |
| `GET /api/mantenimientos/vehiculo/{id}/activos` | **Solo activos** | ✅ PENDIENTE, EN_PROCESO |

---

## 🛠️ Implementación

### **Repository** (`MantenimientoRepository.java`)
```java
@Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
       "AND m.estado IN ('PENDIENTE', 'EN_PROCESO')")
List<Mantenimiento> findMantenimientosActivosPorVehiculo(Long vehiculoId);
```

### **Service** (`MantenimientoService.java`)
```java
@Transactional(readOnly = true)
public List<MantenimientoDTO> obtenerMantenimientosActivosPorVehiculo(Long vehiculoId) {
    return mantenimientoRepository.findMantenimientosActivosPorVehiculo(vehiculoId)
            .stream()
            .map(mapper::mantenimientoToDTO)
            .collect(Collectors.toList());
}
```

### **Controller** (`MantenimientoController.java`)
```java
@GetMapping("/vehiculo/{vehiculoId}/activos")
public ResponseEntity<List<MantenimientoDTO>> obtenerMantenimientosActivosPorVehiculo(
        @PathVariable Long vehiculoId) {
    return ResponseEntity.ok(
            mantenimientoService.obtenerMantenimientosActivosPorVehiculo(vehiculoId));
}
```

---

## ✨ Ventajas

✅ **Consulta optimizada:** Query específico con `@Query` para filtrar solo activos  
✅ **Performance:** Usa índice compuesto `idx_vehiculo_estado` en BD  
✅ **Transaccional:** `readOnly = true` optimiza la lectura  
✅ **Escalable:** Funciona eficientemente incluso con miles de mantenimientos  

---

## 🎯 Casos prácticos de uso

1. **Dashboard de vehículos:** Mostrar cuántos mantenimientos activos tiene cada auto
2. **Alerta de disponibilidad:** Verificar si un vehículo está disponible (sin activos = disponible)
3. **Planificación:** Ver qué mantenimientos están pendientes para un vehículo
4. **Reportes:** Contar mantenimientos en estado PENDIENTE vs EN_PROCESO

---

## 📌 Resumen

✅ **Nuevo endpoint:** `GET /api/mantenimientos/vehiculo/{vehiculoId}/activos`  
✅ **Filtra:** Solo mantenimientos PENDIENTE y EN_PROCESO  
✅ **Eficiente:** Usa query optimizado con índice en BD  
✅ **Práctico:** Útil para verificar si un vehículo está disponible  

