-- Ejectuar siguientes scripts en DB para agregar indices.

CREATE INDEX IF NOT EXISTS idx_disponible ON vehiculo(disponible);

CREATE INDEX IF NOT EXISTS idx_vehiculo_estado ON mantenimiento(vehiculo_id, estado);

