package com.flota.mantenimiento.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flota.mantenimiento.domain.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    boolean existsByVehiculoIdAndAtendidaFalse(Long vehiculoId);
}