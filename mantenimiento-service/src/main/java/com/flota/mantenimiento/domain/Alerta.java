package com.flota.mantenimiento.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehiculoId;

    @Column(nullable = false, length = 10)
    private String patente;

    @Column(nullable = false)
    private int kilometraje;

    @Column(nullable = false, length = 200)
    private String mensaje;

    @Column(nullable = false)
    private Instant creadaEn;

    @Column(nullable = false)
    private boolean atendida;

    // JPA necesita un constructor sin argumentos
    protected Alerta() {
    }

    public Alerta(Long vehiculoId, String patente, int kilometraje, String mensaje) {
        this.vehiculoId = vehiculoId;
        this.patente = patente;
        this.kilometraje = kilometraje;
        this.mensaje = mensaje;
        this.creadaEn = Instant.now();
        this.atendida = false;
    }

    public void atender() {
        this.atendida = true;
    }

    public Long getId() {
        return id;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public String getPatente() {
        return patente;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Instant getCreadaEn() {
        return creadaEn;
    }

    public boolean isAtendida() {
        return atendida;
    }
}