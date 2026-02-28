
UPDATE vehiculos v
SET disponible = NOT EXISTS (
    SELECT 1 FROM mantenimiento m
    WHERE m.vehiculo_id = v.id
    AND m.estado IN ('PENDIENTE', 'EN_PROCESO')
);

CREATE INDEX IF NOT EXISTS idx_disponible ON vehiculos(disponible);

CREATE INDEX IF NOT EXISTS idx_vehiculo_estado ON mantenimiento(vehiculo_id, estado);

