package com.kavak.flota.repository;

import com.kavak.flota.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    List<Mantenimiento> findByVehiculoId(Long vehiculoId);
    List<Mantenimiento> findByVehiculoPatente(String patente);
}

