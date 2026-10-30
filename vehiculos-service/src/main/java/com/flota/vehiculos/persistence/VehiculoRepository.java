package com.flota.vehiculos.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flota.vehiculos.domain.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
