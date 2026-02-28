package com.kavak.flota.repository;

import com.kavak.flota.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPatente(String patente);
    
    /**
     * Obtiene todos los vehículos disponibles (sin mantenimientos activos)
     * Consulta optimizada usando el campo desnormalizado
     */
    List<Vehiculo> findByDisponibleTrue();

    /**
     * Obtiene todos los vehículos no disponibles (con mantenimientos activos)
     */
    List<Vehiculo> findByDisponibleFalse();
}

