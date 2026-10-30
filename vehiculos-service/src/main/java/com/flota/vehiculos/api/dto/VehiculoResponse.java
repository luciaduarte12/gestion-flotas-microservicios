package com.flota.vehiculos.api.dto;

import com.flota.vehiculos.domain.Vehiculo;

public record VehiculoResponse(Long id, String patente, String marca, String modelo,
                               int kilometraje, String estado) {

    public static VehiculoResponse desde(Vehiculo v) {
        return new VehiculoResponse(v.getId(), v.getPatente(), v.getMarca(),
                v.getModelo(), v.getKilometraje(), v.getEstado().getNombre());
    }
}