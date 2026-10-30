package com.flota.vehiculos.domain.estado;

public class Disponible implements EstadoVehiculo {

    public static final Disponible INSTANCIA = new Disponible();

    private Disponible() {
    }

    @Override
    public String getNombre() {
        return "DISPONIBLE";
    }

    @Override
    public EstadoVehiculo iniciarRuta() {
        return EnRuta.INSTANCIA;
    }

    @Override
    public EstadoVehiculo enviarAMantenimiento() {
        return EnMantenimiento.INSTANCIA;
    }
}