package com.flota.mantenimiento.messaging;

import java.time.Instant;

public record VehiculoEstadoCambiadoEvent(
        Long vehiculoId,
        String patente,
        String estadoAnterior,
        String estadoNuevo,
        int kilometraje,
        Instant ocurridoEn) {
}