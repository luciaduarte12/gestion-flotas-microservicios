package com.flota.mantenimiento.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flota.mantenimiento.domain.UltimoService;

public interface UltimoServiceRepository extends JpaRepository<UltimoService, Long> {
}