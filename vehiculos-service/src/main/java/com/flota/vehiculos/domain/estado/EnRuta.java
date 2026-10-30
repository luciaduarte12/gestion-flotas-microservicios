package com.flota.vehiculos.domain.estado;

public class EnRuta implements EstadoVehiculo {

    public static final EnRuta INSTANCIA = new EnRuta();

    private EnRuta() {
    }

    @Override
    public String getNombre() {
        return "EN_RUTA";
    }

    @Override
    public EstadoVehiculo finalizarRuta() {
        return Disponible.INSTANCIA;
    }
}