package com.flota.vehiculos.domain.estado;

public class EnMantenimiento implements EstadoVehiculo {

    public static final EnMantenimiento INSTANCIA = new EnMantenimiento();

    private EnMantenimiento() {
    }

    @Override
    public String getNombre() {
        return "EN_MANTENIMIENTO";
    }

    @Override
    public EstadoVehiculo finalizarMantenimiento() {
        return Disponible.INSTANCIA;
    }
}