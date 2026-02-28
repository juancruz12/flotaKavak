# 🚀 Inicio Rápido - Docker

## Pasos para Levantar la App KAVAK Flota (siendo Dev)

### **1. Instala Docker y Git en tu sistema**

### **2. Asegúrate que Docker está corriendo**
```bash
docker ps
```

### **3. Dentro de una carpeta abre una terminal y clona el repositorio**
```bash
git clone https://github.com/juancruz12/flotaKavak.git
```

### **4. Navega al directorio del proyecto**
```bash
cd C:\Users\tu_usuario\ruta\del\proyecto\flota
```

### **5. Construye e inicia los contenedores**
```bash
docker-compose up --build
```

**Espera a que veas:**
```
kavak-flota-app | Started FlotaApplication in XX.XXX seconds
```

### **6. Accede a la aplicación**
```
http://localhost:8087/api/vehiculos
```

### **7. Detener cuando termines**
```bash
docker-compose down
```

---
## Pasos para Levantar la App KAVAK Flota (si no es necesario editar codigo)

### **1. Instala Docker en tu sistema**

### **2. Asegúrate que Docker está corriendo**
```bash
docker ps
```

### **3. En una carpeta crea un archivo llamado docker-compose.yml y pega lo siguiente:**
```bash
version: '3.8'

services:
  db:
    image: postgres:15-alpine
    container_name: kavak-flota-db
    environment:
      POSTGRES_USER: kavak_user
      POSTGRES_PASSWORD: kavak_pass
      POSTGRES_DB: kavak_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - kavak-network

  app:
    image: ghcr.io/juancruz12/flotakavak:main
    container_name: kavak-flota-app
    ports:
      - "8087:8087"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/kavak_db
      SPRING_DATASOURCE_USERNAME: kavak_user
      SPRING_DATASOURCE_PASSWORD: kavak_pass
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    depends_on:
      - db
    networks:
      - kavak-network

volumes:
  postgres_data:

networks:
  kavak-network:
    driver: bridge
```

### **4. Inicia una terminal en la carpeta creada y ejecuta:**
```bash
docker-compose up
```

**Espera a que veas:**
```
kavak-flota-app | Started FlotaApplication in XX.XXX seconds
```

### **5. Accede a la aplicación**
```
http://localhost:8087/api/vehiculos
```

### **6. Detener cuando termines**
```bash
docker-compose down
```

---

## ✅ Verificación

✓ ¿Docker está corriendo?  
✓ ¿Contenedores iniciados correctamente?  
✓ ¿Puedes acceder a http://localhost:8087?  

Si todo es ✓, ¡está listo! 🎉

---

## 📋 Comandos Útiles

```bash
# Ver logs en tiempo real
docker-compose logs -f app

# Ver estado
docker-compose ps

# Acceder a la BD
docker-compose exec db psql -U kavak_user -d kavak_db

# Limpiar todo
docker-compose down -v
```

---

## 📚 Documentación Completa

Ver `DOCKER_GUIA.md` para guía detallada

