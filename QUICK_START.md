# 🚀 Inicio Rápido - Docker

## 5 Pasos para Levantar la App KAVAK Flota

### **1. Asegúrate que Docker está corriendo**
```bash
docker ps
```

### **2. Navega al directorio del proyecto**
```bash
cd C:\Users\tu_usuario\ruta\del\proyecto\flota
```

### **3. Construye e inicia los contenedores**
```bash
docker-compose up --build
```

**Espera a que veas:**
```
kavak-flota-app | Started FlotaApplication in XX.XXX seconds
```

### **4. Accede a la aplicación**
```
http://localhost:8087/api/vehiculos
```

### **5. Detener cuando termines**
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

