package com.flota.mantenimiento.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flota.mantenimiento.domain.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    boolean existsByVehiculoIdAndAtendidaFalse(Long vehiculoId);

    List<Alerta> findAllByOrderByCreadaEnDesc();

    List<Alerta> findByAtendidaOrderByCreadaEnDesc(boolean atendida);
}