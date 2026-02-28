# 📋 Guía de Transición de Estados de Mantenimientos

## 🎯 Endpoint de Transición

```
PUT /api/mantenimientos/{id}/transicionar?nuevoEstado={estado}
```

---

## 📊 Diagrama de Flujo de Estados

```
PENDIENTE (Estado inicial)
    ├─→ EN_PROCESO
    │     ├─→ COMPLETADO (Estado final)
    │     └─→ CANCELADO (Estado final)
    └─→ CANCELADO (Estado final)

COMPLETADO (No puede transicionar) ✋
CANCELADO (No puede transicionar) ✋
```

---

## ✅ Transiciones Permitidas

| Estado Actual | Estados Permitidos | Descripción |
|---------------|-------------------|-------------|
| **PENDIENTE** | EN_PROCESO, CANCELADO | Inicio de mantenimiento o cancelación |
| **EN_PROCESO** | COMPLETADO, CANCELADO | Finalizar o cancelar |
| **COMPLETADO** | ❌ Ninguno | Estado terminal |
| **CANCELADO** | ❌ Ninguno | Estado terminal |

---

## 📝 Ejemplos de Uso

### 1️⃣ Cambiar de PENDIENTE a EN_PROCESO
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/1/transicionar?nuevoEstado=EN_PROCESO"
```

**Respuesta exitosa (200 OK):**
```json
{
  "mantenimientoId": 1,
  "estadoAnterior": "PENDIENTE",
  "estadoNuevo": "EN_PROCESO",
  "mensaje": "Transición exitosa de PENDIENTE a EN_PROCESO"
}
```

---

### 2️⃣ Cambiar de EN_PROCESO a COMPLETADO
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/1/transicionar?nuevoEstado=COMPLETADO"
```

**Respuesta exitosa (200 OK):**
```json
{
  "mantenimientoId": 1,
  "estadoAnterior": "EN_PROCESO",
  "estadoNuevo": "COMPLETADO",
  "mensaje": "Transición exitosa de EN_PROCESO a COMPLETADO"
}
```

---

### 3️⃣ Cancelar mantenimiento (desde PENDIENTE)
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/2/transicionar?nuevoEstado=CANCELADO"
```

**Respuesta exitosa (200 OK):**
```json
{
  "mantenimientoId": 2,
  "estadoAnterior": "PENDIENTE",
  "estadoNuevo": "CANCELADO",
  "mensaje": "Transición exitosa de PENDIENTE a CANCELADO"
}
```

---

## ❌ Errores Comunes

### 1️⃣ Transición inválida: COMPLETADO → EN_PROCESO
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/1/transicionar?nuevoEstado=EN_PROCESO"
```

**Respuesta de error (400 Bad Request):**
```json
{
  "timestamp": "2026-02-26T10:30:45.123456",
  "status": 400,
  "error": "Transición de Estado Inválida",
  "mensaje": "No se puede cambiar el estado de un mantenimiento COMPLETADO. Los estados COMPLETADO y CANCELADO son finales.",
  "path": "/api/mantenimientos/1/transicionar"
}
```

---

### 2️⃣ Estado inválido: PENDIENTE → INEXISTENTE
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/1/transicionar?nuevoEstado=INEXISTENTE"
```

**Respuesta de error (400 Bad Request):**
```json
{
  "timestamp": "2026-02-26T10:30:45.123456",
  "status": 400,
  "error": "Argumento Inválido",
  "mensaje": "No valid enum constant for value INEXISTENTE",
  "path": "/api/mantenimientos/1/transicionar"
}
```

---

### 3️⃣ Mantenimiento no encontrado
```bash
curl -X PUT "http://localhost:8087/api/mantenimientos/999/transicionar?nuevoEstado=EN_PROCESO"
```

**Respuesta de error (500 Internal Server Error):**
```json
{
  "timestamp": "2026-02-26T10:30:45.123456",
  "status": 500,
  "error": "Error Interno",
  "mensaje": "Mantenimiento no encontrado con ID: 999",
  "path": "/api/mantenimientos/999/transicionar"
}
```

---

## 🔒 Reglas de Negocio Implementadas

✅ **PENDIENTE** puede pasar a:
- EN_PROCESO (comenzar mantenimiento)
- CANCELADO (cancelar antes de empezar)

✅ **EN_PROCESO** puede pasar a:
- COMPLETADO (finalizar exitosamente)
- CANCELADO (cancelar durante la ejecución)

✅ **COMPLETADO** NO puede:
- Cambiar a ningún estado (es final)

✅ **CANCELADO** NO puede:
- Cambiar a ningún estado (es final)

---

## 🏗️ Arquitectura

### Patrón: State Pattern
La solución implementa el **State Pattern** para permitir agregar nuevos estados y transiciones fácilmente en el futuro sin modificar código existente.

### Componentes:

1. **Enum Estado** (`Estado.java`)
   - Define transiciones permitidas
   - Métodos para validar transiciones
   - Detecta estados terminales

2. **Servicio de Transición** (`TransicionEstadoService.java`)
   - Valida reglas de transición
   - Proporciona mensajes descriptivos

3. **Controlador** (`MantenimientoController.java`)
   - Expone endpoint REST
   - Maneja solicitudes HTTP

4. **Manejador Global de Excepciones** (`GlobalExceptionHandler.java`)
   - Captura errores de transición
   - Devuelve respuestas consistentes

---

## 🚀 Para Agregar Nuevos Estados Futuros

Solo necesitas modificar el **Enum Estado**:

```java
public enum Estado {
    // ... estados existentes ...
    MANTENIMIENTO_DIFERIDO(new HashSet<>(Arrays.asList(Estado.EN_PROCESO, Estado.CANCELADO))),
    // nueva transición
    EN_ESPERA(new HashSet<>(Arrays.asList(Estado.EN_PROCESO, Estado.CANCELADO)));
    
    // El resto del código sigue igual
}
```
