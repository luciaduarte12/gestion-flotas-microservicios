package com.flota.vehiculos.domain.estado;

public interface EstadoVehiculo {

    String getNombre();

    default EstadoVehiculo iniciarRuta() {
        throw transicionInvalida("iniciar ruta");
    }

    default EstadoVehiculo finalizarRuta() {
        throw transicionInvalida("finalizar ruta");
    }

    default EstadoVehiculo enviarAMantenimiento() {
        throw transicionInvalida("enviar a mantenimiento");
    }

    default EstadoVehiculo finalizarMantenimiento() {
        throw transicionInvalida("finalizar mantenimiento");
    }

    private IllegalStateException transicionInvalida(String accion) {
        return new IllegalStateException(
                "No se puede " + accion + " con el vehículo en estado " + getNombre());
    }
}