package com.flota.vehiculos.domain;

import com.flota.vehiculos.domain.estado.Disponible;
import com.flota.vehiculos.domain.estado.EstadoVehiculo;
import com.flota.vehiculos.persistence.EstadoVehiculoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String patente;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private int kilometraje;

    @Convert(converter = EstadoVehiculoConverter.class)
    @Column(nullable = false, length = 20)
    private EstadoVehiculo estado;

    // JPA necesita un constructor sin argumentos
    protected Vehiculo() {
    }

    public Vehiculo(String patente, String marca, String modelo, int kilometraje) {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.estado = Disponible.INSTANCIA;
    }

    // --- Transiciones: delegan en el estado actual ---

    public void iniciarRuta() {
        this.estado = estado.iniciarRuta();
    }

    public void finalizarRuta() {
        this.estado = estado.finalizarRuta();
    }

    public void enviarAMantenimiento() {
        this.estado = estado.enviarAMantenimiento();
    }

    public void finalizarMantenimiento() {
        this.estado = estado.finalizarMantenimiento();
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }
}