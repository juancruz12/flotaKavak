-- ========================================
-- Script de optimización para disponibilidad de vehículos
-- ========================================
-- Ejecutar DESPUÉS de que la aplicación haya creado la columna 'disponible'
-- con spring.jpa.hibernate.ddl-auto=update
--
-- Este script:
-- 1. Calcula los valores iniciales de disponibilidad
-- 2. Crea índices para optimizar consultas
-- ========================================

-- 1️⃣ Calcular valores iniciales de disponibilidad
-- Un vehículo NO está disponible si tiene al menos un mantenimiento PENDIENTE o EN_PROCESO
UPDATE vehiculos v
SET disponible = NOT EXISTS (
    SELECT 1 FROM mantenimiento m
    WHERE m.vehiculo_id = v.id
    AND m.estado IN ('PENDIENTE', 'EN_PROCESO')
);

-- 2️⃣ Crear índices para optimizar consultas
-- Índice para filtrar vehículos disponibles/no disponibles (consulta O(1))
CREATE INDEX IF NOT EXISTS idx_disponible ON vehiculos(disponible);

-- Índice compuesto para consultas de mantenimientos por vehículo y estado
CREATE INDEX IF NOT EXISTS idx_vehiculo_estado ON mantenimiento(vehiculo_id, estado);

-- ========================================
-- Verificación (opcional - comentado por defecto)
-- ========================================
-- Descomentar para verificar que todo funciona correctamente:

-- SELECT COUNT(*) as total_vehiculos FROM vehiculos;
-- SELECT COUNT(*) as vehiculos_disponibles FROM vehiculos WHERE disponible = true;
-- SELECT COUNT(*) as vehiculos_no_disponibles FROM vehiculos WHERE disponible = false;

-- Verificar índices creados:
-- SELECT indexname, tablename FROM pg_indexes WHERE tablename IN ('vehiculos', 'mantenimiento');

