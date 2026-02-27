package com.kavak.flota.repository;

import com.kavak.flota.entity.Mantenimiento;
import com.kavak.flota.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    List<Mantenimiento> findByVehiculoId(Long vehiculoId);
    List<Mantenimiento> findByVehiculoPatente(String patente);

    /**
     * Obtener mantenimientos activos (no finalizados) de un vehículo
     * Usa parámetros de tipo enum para seguridad de tipos
     */
    @Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
           "AND m.estado IN :estadosActivos")
    List<Mantenimiento> findMantenimientosActivosPorVehiculo(
            @Param("vehiculoId") Long vehiculoId,
            @Param("estadosActivos") List<Estado> estadosActivos);

    /**
     * Obtener mantenimientos activos (no finalizados) de un vehículo por patente
     * Usa parámetros de tipo enum para seguridad de tipos
     */
    @Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.patente = :patente " +
           "AND m.estado IN :estadosActivos")
    List<Mantenimiento> findMantenimientosActivosPorPatente(
            @Param("patente") String patente,
            @Param("estadosActivos") List<Estado> estadosActivos);

    /**
     * Obtener mantenimientos completados de un vehículo
     * Filtra solo los mantenimientos con estado COMPLETADO
     */
    @Query("SELECT m FROM Mantenimiento m WHERE m.vehiculo.id = :vehiculoId " +
           "AND m.estado = :estadoCompletado")
    List<Mantenimiento> findMantenimientosCompletadosPorVehiculo(
            @Param("vehiculoId") Long vehiculoId, @Param("estadoCompletado") Estado estadoCompletado);
}

