package com.flota.mantenimiento.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ultimos_services")
public class UltimoService {

    // Un registro por vehículo: el id es el del vehículo
    @Id
    private Long vehiculoId;

    @Column(nullable = false)
    private int kilometraje;

    @Column(nullable = false)
    private Instant realizadoEn;

    // JPA necesita un constructor sin argumentos
    protected UltimoService() {
    }

    public UltimoService(Long vehiculoId, int kilometraje) {
        this.vehiculoId = vehiculoId;
        this.kilometraje = kilometraje;
        this.realizadoEn = Instant.now();
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public Instant getRealizadoEn() {
        return realizadoEn;
    }
}