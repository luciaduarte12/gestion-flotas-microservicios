package com.flota.mantenimiento.api.dto;

import java.time.Instant;

import com.flota.mantenimiento.domain.Alerta;

public record AlertaResponse(Long id, Long vehiculoId, String patente, int kilometraje,
                             String mensaje, Instant creadaEn, boolean atendida) {

    public static AlertaResponse desde(Alerta a) {
        return new AlertaResponse(a.getId(), a.getVehiculoId(), a.getPatente(),
                a.getKilometraje(), a.getMensaje(), a.getCreadaEn(), a.isAtendida());
    }
}