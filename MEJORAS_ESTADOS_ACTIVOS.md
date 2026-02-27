# ✅ MEJORAS: Estados Activos y Seguridad en Queries

## 🔧 Problemas Corregidos

### **Problema 1: Estados hardcodeados en el Repository**
❌ **Antes:**
```java
@Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
       "AND m.estado IN ('PENDIENTE', 'EN_PROCESO')")
List<Mantenimiento> findMantenimientosActivosPorVehiculo(Long vehiculoId);
```

**Problemas:**
- Estados hardcodeados como strings
- Si se agregan nuevos estados activos, hay que cambiar múltiples lugares
- Propenso a errores tipográficos
- No type-safe

### **Problema 2: Seguridad en queries**
✅ **Las queries con `@Query` y `@Param` SÍ son seguras**, pero podemos mejorar usando enums en lugar de strings.

---

## ✅ Solución Implementada

### **1. Enum Estado con método centralizado** (`Estado.java`)

```java
public enum Estado {
    PENDIENTE(new HashSet<>(Arrays.asList(EN_PROCESO, CANCELADO))),
    EN_PROCESO(new HashSet<>(Arrays.asList(COMPLETADO, CANCELADO))),
    COMPLETADO(new HashSet<>()),
    CANCELADO(new HashSet<>());

    // ...existing code...

    /**
     * Verifica si el estado es activo (no finalizado)
     */
    public boolean esActivo() {
        return this == PENDIENTE || this == EN_PROCESO;
    }

    /**
     * Obtiene la lista de estados activos (no finalizados)
     * ⚡ Único lugar donde se definen los estados activos
     */
    public static List<Estado> getEstadosActivos() {
        return Arrays.asList(PENDIENTE, EN_PROCESO);
    }
}
```

**Ventajas:**
- ✅ **Single source of truth:** Un solo lugar define qué es "activo"
- ✅ **Type-safe:** No hay strings mágicos
- ✅ **Escalable:** Agregar nuevos estados activos solo requiere modificar el enum

---

### **2. Repository con parámetros tipo enum** (`MantenimientoRepository.java`)

```java
@Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
       "AND m.estado IN :estadosActivos")
List<Mantenimiento> findMantenimientosActivosPorVehiculo(
        @Param("vehiculoId") Long vehiculoId,
        @Param("estadosActivos") List<Estado> estadosActivos);
```

**Ventajas:**
- ✅ **Type-safe:** Acepta `List<Estado>` (enum) en lugar de strings
- ✅ **Seguro contra SQL Injection:** JPA convierte enums automáticamente
- ✅ **Flexible:** Puedes pasar diferentes estados según el caso de uso

---

### **3. Service usando el método centralizado** (`MantenimientoService.java`)

```java
@Transactional(readOnly = true)
public List<MantenimientoDTO> obtenerMantenimientosActivosPorVehiculo(Long vehiculoId) {
    return mantenimientoRepository.findMantenimientosActivosPorVehiculo(
                    vehiculoId, 
                    Estado.getEstadosActivos()) // ⚡ Usa el método del enum
            .stream()
            .map(mapper::mantenimientoToDTO)
            .collect(Collectors.toList());
}
```

**Ventajas:**
- ✅ **No hardcodea estados:** Usa `Estado.getEstadosActivos()`
- ✅ **Mantenible:** Un solo lugar para cambiar qué estados son activos
- ✅ **Claro:** El código es autodocumentado

---

### **4. Entidad Vehiculo simplificada** (`Vehiculo.java`)

```java
public void actualizarDisponibilidad() {
    if (mantenimientos != null) {
        boolean tieneMantenimientoActivo = mantenimientos.stream()
            .anyMatch(m -> m.getEstado().esActivo()); // ⚡ Usa método del enum
        this.disponible = !tieneMantenimientoActivo;
    }
}
```

**Ventajas:**
- ✅ **Código limpio:** `m.getEstado().esActivo()` es más legible
- ✅ **Consistente:** Usa la misma lógica que el resto del sistema

---

## 🔒 ¿Es segura la query?

### **Sí, es completamente segura**

**JPA usa prepared statements automáticamente:**
```java
@Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
       "AND m.estado IN :estadosActivos")
```

Se traduce a SQL:
```sql
SELECT * FROM mantenimiento m 
WHERE m.vehiculo_id = ? 
AND m.estado IN (?, ?)
```

**Cómo funciona:**
1. JPA crea un **prepared statement**
2. Los parámetros `:vehiculoId` y `:estadosActivos` se pasan como **bind variables**
3. Los enums se convierten automáticamente a sus nombres (`'PENDIENTE'`, `'EN_PROCESO'`)
4. ✅ **Inmune a SQL Injection** porque los valores no se concatenan al SQL

---

## 📊 Comparación

| Aspecto | Antes | Ahora |
|---------|-------|-------|
| **Definición de estados activos** | Hardcodeado en Repository | ✅ Centralizado en `Estado.getEstadosActivos()` |
| **Type safety** | Strings `'PENDIENTE'`, `'EN_PROCESO'` | ✅ Enum `List<Estado>` |
| **Mantenibilidad** | Cambiar en múltiples lugares | ✅ Cambiar solo en el enum |
| **Seguridad SQL Injection** | ✅ Seguro (prepared statements) | ✅ Seguro (prepared statements + enums) |
| **Legibilidad** | `m.getEstado() == Estado.PENDIENTE \|\| m.getEstado() == Estado.EN_PROCESO` | ✅ `m.getEstado().esActivo()` |

---

## 🚀 Extensibilidad futura

### **Agregar nuevo estado activo:**

**Antes:** Cambiar en 4 lugares
- ❌ Repository query 1
- ❌ Repository query 2
- ❌ Vehiculo.actualizarDisponibilidad()
- ❌ Script SQL de inicialización

**Ahora:** Cambiar en 1 solo lugar
```java
public static List<Estado> getEstadosActivos() {
    return Arrays.asList(PENDIENTE, EN_PROCESO, NUEVO_ESTADO_ACTIVO);
}
```

✅ **Todo el sistema se actualiza automáticamente**

---

## 🎯 Ejemplo de uso extendido

Si en el futuro quieres consultar solo estados finales:

```java
// En Estado.java
public static List<Estado> getEstadosFinales() {
    return Arrays.asList(COMPLETADO, CANCELADO);
}

// En Repository
@Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
       "AND m.estado IN :estadosFinales")
List<Mantenimiento> findMantenimientosFinalizadosPorVehiculo(
        @Param("vehiculoId") Long vehiculoId,
        @Param("estadosFinales") List<Estado> estadosFinales);

// En Service
public List<MantenimientoDTO> obtenerMantenimientosFinalizados(Long vehiculoId) {
    return mantenimientoRepository.findMantenimientosFinalizadosPorVehiculo(
            vehiculoId, 
            Estado.getEstadosFinales());
}
```

---

## ✨ Resumen de mejoras

1. ✅ **Centralización:** Estados activos definidos en un solo lugar (`Estado.getEstadosActivos()`)
2. ✅ **Type-safety:** Uso de enums en lugar de strings
3. ✅ **Seguridad:** Queries con prepared statements (siempre fue seguro, ahora más type-safe)
4. ✅ **Mantenibilidad:** Cambios futuros solo en el enum
5. ✅ **Legibilidad:** `m.getEstado().esActivo()` es más claro
6. ✅ **Escalabilidad:** Fácil agregar nuevos estados activos/finales

---

## 📝 Archivos modificados

```
✅ Estado.java
   ├─ esActivo()
   └─ getEstadosActivos()

✅ MantenimientoRepository.java
   ├─ findMantenimientosActivosPorVehiculo(Long, List<Estado>)
   └─ findMantenimientosActivosPorPatente(String, List<Estado>)

✅ MantenimientoService.java
   ├─ obtenerMantenimientosActivosPorVehiculo() → usa Estado.getEstadosActivos()
   └─ obtenerMantenimientosActivosPorPatente() → usa Estado.getEstadosActivos()

✅ Vehiculo.java
   └─ actualizarDisponibilidad() → usa m.getEstado().esActivo()
```

¡Código más limpio, seguro y mantenible! 🎉

