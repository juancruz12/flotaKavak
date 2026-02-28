# 🚗 KAVAK Flota - Sistema de Gestión de Flota de Autos

Sistema completo de gestión de flota de automóviles desarrollado con **Spring Boot 4.0.3**, **PostgreSQL** y **Docker**.

---
## 📋 Criterios y decisiones de diseño
- ✅ **Busqueda en DB**: Excepto que se indicara explícitamente que fuera necesario realizar la búsqueda por patente, se implementaron las búsquedas a partir del idVehiculo ya que si se hiciera por patente resultaría ser menos eficiente y mas costoso. Incluso agregando el índice a la columna 'patente'.
---
- ✅ **Campo 'disponible' EAGER**: Se agregó un campo booleano 'disponible' en la entidad Vehiculo para optimizar las consultas de disponibilidad, evitando cálculos costosos en tiempo real. Ya que de esta forma se actualiza el campo cuando se hace una transicion de sus mantenimientos.
---
- ✅ **State Pattern**: Se implementó el State Pattern para manejar las transiciones de estado de los mantenimientos, asegurando que solo se permitan transiciones válidas y centralizando la lógica de negocio, permitiendo asi una fácil escalabilidad y mantenimiento del código.
---
- ✅ **Costo total**: Para realizar el calculo del costo total de mantenimientos de un auto, se toman los mantenimientos que están en estado "COMPLETADO" y se prioriza el costoFinal, en caso de no estar presente se utiliza el costoEstimado. Se podria conversar con negocio si hay casos en los cuales un mantenimiento "EN_PROCESO" que fue "CANCELADO" genera gastos, si es asi, también habría que contemplarlos en el calculo del costo total.
---
- ✅ **Validaciones**: Se implementaron validaciones exhaustivas tanto a nivel de DTOs utilizando Bean Validation, como a nivel de servicios para asegurar la integridad de los datos y el correcto flujo de la aplicación. Además, se crearon excepciones personalizadas para manejar errores específicos y se implementó un manejo centralizado de errores para proporcionar respuestas estandarizadas y mensajes descriptivos a los clientes de la API.
---
- ✅ **Sanitizacion de datos**: Se sanitizo el dato 'patente' tanto en inputs como en outputs para que no sea sensible a mayúsculas/minúsculas/espacios. Permitiendo asi estandarizacion y consistencia de datos.
---
- ✅ **Dockerizacion**: Se opto por dockerizar la app permitiendo portabilidad y facilidad de despliegue, evitando problemas de configuración en diferentes entornos. Se incluyó un archivo `docker-compose.yml` para levantar tanto la aplicación como la base de datos PostgreSQL de manera sencilla y rápida.
---

## ✨ Características y funcionalidades

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
│    PostgreSQL 15            │
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