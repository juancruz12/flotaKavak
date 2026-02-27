# 🚗 KAVAK Flota - Sistema de Gestión de Flota de Autos

Sistema completo de gestión de flota de automóviles desarrollado con **Spring Boot 4.0.3**, **PostgreSQL** y **Docker**.

---

## ✨ Características

### **Gestión de Vehículos**
- ✅ Crear, leer, actualizar y eliminar vehículos
- ✅ Búsqueda por ID o patente
- ✅ Actualización de kilometraje
- ✅ Cálculo automático de disponibilidad
- ✅ Listado de vehículos disponibles/no disponibles

### **Gestión de Mantenimientos**
- ✅ Crear mantenimientos asociados a vehículos
- ✅ Transiciones de estado con validación
- ✅ Obtener mantenimientos activos
- ✅ Cálculo de costo total de mantenimientos completados
- ✅ Priorización inteligente de costos (costoFinal > costoEstimado)

### **Transiciones de Estado**
- ✅ **PENDIENTE** → EN_PROCESO, CANCELADO
- ✅ **EN_PROCESO** → COMPLETADO, CANCELADO
- ✅ **COMPLETADO** → Estado terminal (inmutable)
- ✅ **CANCELADO** → Estado terminal (inmutable)

### **Validaciones**
- ✅ Validación de campos en DTOs (Bean Validation)
- ✅ Validación de parámetros de entrada
- ✅ Excepciones personalizadas
- ✅ Manejo centralizado de errores
- ✅ Respuestas estandarizadas con mensajes descriptivos

### **Testing**
- ✅ 48 test cases con JUnit 5 y Mockito
- ✅ Cobertura 100% de lógica crítica
- ✅ Tests de servicios, enums y excepciones

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────┐
│      Spring Boot 4.0.3 (JDK 21)     │
├─────────────────────────────────────┤
│          Controllers                 │
│     VehiculoController              │
│     MantenimientoController         │
├─────────────────────────────────────┤
│          Services                    │
│     VehiculoService                 │
│     MantenimientoService            │
│     TransicionEstadoService         │
├─────────────────────────────────────┤
│       Repositories (JPA)             │
│     VehiculoRepository              │
│     MantenimientoRepository         │
├─────────────────────────────────────┤
│    PostgreSQL 15 (Docker)            │
└─────────────────────────────────────┘
```

---

## 📊 Stack Tecnológico

| Componente | Versión | Propósito |
|-----------|---------|----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 4.0.3 | Framework web |
| **Spring Data JPA** | 3.2.3 | Persistencia de datos |
| **PostgreSQL** | 15 | Base de datos |
| **Maven** | 3.9 | Gestión de dependencias |
| **JUnit 5** | 5.10 | Testing |
| **Mockito** | 5.8 | Mocking en tests |
| **Docker** | 20.10+ | Containerización |
| **Lombok** | 1.18 | Reducir boilerplate |

---

## 🐳 Inicio Rápido con Docker

### **1. Asegúrate que Docker está corriendo**
```bash
docker ps
```

### **2. Levanta los contenedores**
```bash
docker-compose up --build
```

### **3. Accede a la API**
```
http://localhost:8087/api/vehiculos
```

### **4. Detén cuando termines**
```bash
docker-compose down
```

**Para guía completa, ver `QUICK_START.md`**

---

## 🚀 API Endpoints

### **Vehículos**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| **POST** | `/api/vehiculos` | Crear vehículo |
| **GET** | `/api/vehiculos?id={id}` | Obtener por ID |
| **GET** | `/api/vehiculos?patente={patente}` | Obtener por patente |
| **GET** | `/api/vehiculos/disponibles` | Listar disponibles |
| **GET** | `/api/vehiculos/no-disponibles` | Listar no disponibles |
| **GET** | `/api/vehiculos/disponibilidad?vehiculoId={id}` | Verificar disponibilidad |
| **PUT** | `/api/vehiculos/kilometraje?id={id}&nuevoKilometraje={km}` | Actualizar km |
| **DELETE** | `/api/vehiculos?id={id}` | Eliminar vehículo |

### **Mantenimientos**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| **POST** | `/api/mantenimientos?idVehiculo={id}` | Crear mantenimiento |
| **GET** | `/api/mantenimientos/vehiculo/{id}` | Listar por vehículo |
| **GET** | `/api/mantenimientos/vehiculo/{id}/activos` | Listar activos |
| **GET** | `/api/mantenimientos/vehiculo/{id}/costo-total` | Costo total |
| **PUT** | `/api/mantenimientos/{id}/transicionar?nuevoEstado={estado}` | Transicionar estado |
| **DELETE** | `/api/mantenimientos/{id}` | Eliminar mantenimiento |

---

## 📈 Optimizaciones Implementadas

### **Performance**
- ✅ Índices en tabla de vehículos (patente, disponible)
- ✅ Índice compuesto en mantenimientos (vehiculo_id, estado)
- ✅ Campo desnormalizado `disponible` para O(1) lookups
- ✅ Lazy loading en relaciones

### **Escalabilidad**
- ✅ State Pattern para transiciones de estado
- ✅ Service Pattern para lógica centralizada
- ✅ Repository Pattern para acceso a datos
- ✅ DTO Pattern para transferencia de datos
---

## 🎉 ¡Gracias por usar KAVAK Flota!