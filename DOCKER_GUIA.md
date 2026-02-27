# 🐳 Guía de Dockerización - KAVAK Flota

## 📋 Descripción

Esta guía explica paso a paso cómo **dockerizar y ejecutar** la aplicación KAVAK Flota usando Docker y Docker Compose.

---

## 📦 Requisitos Previos

Necesitas tener instalado:

- **Docker** (versión 20.10+)
  - [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop)
- **Docker Compose** (versión 1.29+)
  - Incluido en Docker Desktop

### **Verificar instalación:**

```bash
docker --version
# Docker version 20.10+ expected

docker-compose --version
# Docker Compose version 1.29+ expected
```

---

## 🚀 Pasos para Levantar la Aplicación

### **Paso 1: Clonar/Descargar el repositorio**

```bash
cd C:\Users\tu_usuario\ruta\del\proyecto
```

---

### **Paso 2: Asegurarse que Docker está corriendo**

```bash
docker ps
# Debería mostrarte los contenedores en ejecución (vacío si no hay ninguno)
```

Si Docker no está corriendo, abre **Docker Desktop**.

---

### **Paso 3: Construir e iniciar los contenedores**

```bash
docker-compose up --build
```

**Explicación:**
- `--build`: Compila la imagen de la aplicación antes de iniciar
- Sin `--build`: Solo inicia contenedores existentes

**Salida esperada:**
```
kavak-db        | 2026-02-27 16:45:00.000 UTC [1] LOG:  database system is ready to accept connections
kavak-flota-app | 2026-02-27T16:45:30.123-03:00  INFO 1 --- [main] com.kavak.flota.FlotaApplication : Started FlotaApplication in 45.234 seconds
```

---

### **Paso 4: Verificar que la aplicación está corriendo**

```bash
curl http://localhost:8087/api/vehiculos
# O simplemente abre en el navegador:
# http://localhost:8087/api/vehiculos
```

**Respuesta esperada:**
```json
[]
```

---

## 🔧 Comandos Útiles

### **Detener los contenedores:**

```bash
docker-compose down
```

Esto detiene y elimina los contenedores, pero **mantiene los datos** en el volumen PostgreSQL.

---

### **Detener y eliminar todo (incluidos datos):**

```bash
docker-compose down -v
```

**⚠️ Cuidado:** Esto elimina la base de datos.

---

### **Ver logs de la aplicación:**

```bash
docker-compose logs -f app
```

`-f` mantiene los logs en tiempo real. Presiona `Ctrl+C` para salir.

---

### **Ver logs de la BD:**

```bash
docker-compose logs -f db
```

---

### **Ver estado de los contenedores:**

```bash
docker-compose ps
```

---

### **Ejecutar comandos dentro del contenedor:**

```bash
# Acceder a la BD PostgreSQL
docker-compose exec db psql -U kavak_user -d kavak_db

# Ver las tablas
\dt

# Salir
\q
```

---

## 📂 Estructura de Archivos

```
flota/
├── Dockerfile              # Definición de la imagen Docker
├── docker-compose.yml      # Orquestación de contenedores
├── .dockerignore           # Archivos a ignorar en la imagen
├── .env.example            # Variables de entorno (ejemplo)
├── pom.xml                 # Dependencias Maven
├── src/                    # Código fuente
├── scripts/
│   └── 01_disponibilidad_indices.sql  # Script de BD
└── target/                 # Binarios compilados
```

---

## 🐳 Contenedores en Ejecución

### **1. Contenedor de Base de Datos (db)**

```yaml
Image: postgres:15-alpine
Container: kavak-db
Puerto: 5432 (interno), 5432 (externo)
Usuario: kavak_user
Contraseña: kavak_pass
Base de datos: kavak_db
```

**Acceder a la BD desde tu máquina:**

```bash
psql -h localhost -U kavak_user -d kavak_db
# Contraseña: kavak_pass
```

---

### **2. Contenedor de Aplicación (app)**

```yaml
Image: flota:latest (construida desde Dockerfile)
Container: kavak-flota-app
Puerto: 8087 (interno), 8087 (externo)
Dependencias: db (espera a que esté healthy)
Zona horaria: America/Argentina/Buenos_Aires
```

**Acceder a la aplicación:**

```
http://localhost:8087/api/vehiculos
http://localhost:8087/api/mantenimientos
```

---

## 🌐 URLs de Acceso

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **API (Vehículos)** | http://localhost:8087/api/vehiculos | Endpoints de vehículos |
| **API (Mantenimientos)** | http://localhost:8087/api/mantenimientos | Endpoints de mantenimientos |
| **Base de datos** | localhost:5432 | PostgreSQL (desde tu máquina) |
| **Base de datos (BD interna)** | db:5432 | PostgreSQL (desde contenedor app) |

---

## 📊 Volúmenes

### **postgres_data**

```yaml
Driver: local
Ubicación: Docker Desktop (gestionado por Docker)
Propósito: Persistencia de datos de PostgreSQL
```

**Verificar volúmenes:**

```bash
docker volume ls
docker volume inspect flota_postgres_data
```

---

## 🔍 Healthchecks

La aplicación tiene dos healthchecks:

### **1. Base de Datos**
```yaml
Comando: pg_isready -U kavak_user -d kavak_db
Intervalo: 10 segundos
Timeout: 5 segundos
Reintentos: 5
```

### **2. Aplicación**
```yaml
Comando: wget http://localhost:8087/api/vehiculos
Intervalo: 30 segundos
Timeout: 10 segundos
Reintentos: 3
Inicio diferido: 40 segundos
```

**Ver estado de healthchecks:**

```bash
docker-compose ps
# Muestra "healthy" cuando todo está bien
```

---

## 🔧 Configuración de Entorno

### **Variables de Entorno (docker-compose.yml)**

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/kavak_db
SPRING_DATASOURCE_USERNAME: kavak_user
SPRING_DATASOURCE_PASSWORD: kavak_pass
SPRING_JPA_HIBERNATE_DDL_AUTO: update
SPRING_JACKSON_TIME_ZONE: America/Argentina/Buenos_Aires
TZ: America/Argentina/Buenos_Aires
```

**Nota:** El nombre del host es `db` (nombre del servicio), no `localhost`.

---

## ⚙️ Configuración de Hibernate

```yaml
SPRING_JPA_HIBERNATE_DDL_AUTO: update
```

**Valores posibles:**
- `create-drop`: Crea esquema al iniciar, elimina al cerrar (desarrollo)
- `update`: Actualiza esquema existente (recomendado para desarrollo)
- `validate`: Solo valida sin cambios (producción)
- `none`: No hace nada (producción)

---

## 🛡️ Seguridad

### **En Desarrollo (actual)**
- BD con contraseña simple
- DDL automático habilitado
- SQL logging habilitado

### **En Producción**
Cambiar en `docker-compose.yml`:

```yaml
environment:
  POSTGRES_PASSWORD: ${DB_PASSWORD}  # Variable de entorno
  SPRING_JPA_HIBERNATE_DDL_AUTO: validate
  SPRING_JPA_SHOW_SQL: "false"
```

Usar archivo `.env`:
```bash
DB_PASSWORD=tu_contraseña_segura_aqui
```

---

## 📝 Ejemplos de Uso

### **Crear un vehículo**

```bash
curl -X POST http://localhost:8087/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{
    "patente": "ABC123",
    "marca": "Toyota",
    "modelo": "Corolla",
    "anio": 2023,
    "kilometraje": 15000
  }'
```

---

### **Crear un mantenimiento**

```bash
curl -X POST http://localhost:8087/api/mantenimientos?idVehiculo=1 \
  -H "Content-Type: application/json" \
  -d '{
    "tipoMantenimiento": "CAMBIO_ACEITE",
    "descripcion": "Cambio de aceite y filtros",
    "costoEstimado": 5000
  }'
```

---

### **Obtener vehículos disponibles**

```bash
curl http://localhost:8087/api/vehiculos/disponibles
```

---

## 🐛 Troubleshooting

### **Problema: "Cannot connect to Docker daemon"**

**Solución:** Abre Docker Desktop o inicia el daemon:

```bash
sudo systemctl start docker  # Linux
# macOS/Windows: Abre Docker Desktop
```

---

### **Problema: Puerto 5432 ya está en uso**

**Solución:** Cambiar puerto en `docker-compose.yml`:

```yaml
db:
  ports:
    - "5433:5432"  # Cambiar 5432 a 5433
```

---

### **Problema: Contenedor de app no inicia**

**Solución:** Ver logs:

```bash
docker-compose logs app
```

---

### **Problema: BD no está lista**

**Solución:** Verificar que la BD esté healthy:

```bash
docker-compose ps
# Si db no está "healthy", espera 30 segundos más
```

---

## 🔄 Workflow Típico

### **Desarrollo Local (sin Docker)**

```bash
# Levantar BD en Docker
docker-compose up db

# Ejecutar app en IDE
# Acceder a localhost:8087
```

---

### **Desarrollo Full en Docker**

```bash
# Levantar BD y app
docker-compose up --build

# Hacer cambios en el código
# Rebuildar solo la app
docker-compose up --build app
```

---

### **Producción**

```bash
# Usar archivo separado docker-compose.prod.yml
docker-compose -f docker-compose.prod.yml up -d

# Monitorear logs
docker-compose logs -f app
```

---

## 📊 Monitoreo

### **Ver uso de recursos:**

```bash
docker stats
```

### **Ver historial de eventos:**

```bash
docker-compose events
```

### **Inspeccionar contenedor:**

```bash
docker inspect kavak-flota-app
```

---

## 🎯 Resumen de Comandos Básicos

| Comando | Descripción |
|---------|-------------|
| `docker-compose up --build` | Construir e iniciar todo |
| `docker-compose down` | Detener todo |
| `docker-compose ps` | Ver estado de contenedores |
| `docker-compose logs -f app` | Ver logs en tiempo real |
| `docker-compose exec db psql -U kavak_user -d kavak_db` | Acceder a BD |
| `docker volume ls` | Ver volúmenes |
| `docker images` | Ver imágenes |

---

## ✅ Checklist de Verificación

- [ ] Docker Desktop instalado y corriendo
- [ ] Ejecutaste `docker-compose up --build`
- [ ] Ambos contenedores están "healthy"
- [ ] Puedes acceder a http://localhost:8087/api/vehiculos
- [ ] Respuesta es `[]` (lista vacía de vehículos)
- [ ] Logs de app no tienen errores

Si todo está ✅, ¡la aplicación está lista para usar! 🎉

---

## 📚 Documentación Adicional

- [Docker Docs](https://docs.docker.com/)
- [Docker Compose Docs](https://docs.docker.com/compose/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
- [OpenJDK Docker Images](https://hub.docker.com/_/openjdk)

---

## 🆘 Soporte

Si tienes problemas:

1. Revisa los logs: `docker-compose logs`
2. Verifica puertos: `docker-compose ps`
3. Reinicia todo: `docker-compose down && docker-compose up --build`
4. Limpia todo: `docker-compose down -v && docker-compose up --build`

