package com.flota.vehiculos.messaging;

import java.time.Instant;

public record VehiculoEstadoCambiadoEvent(
        Long vehiculoId,
        String patente,
        String estadoAnterior,
        String estadoNuevo,
        int kilometraje,
        Instant ocurridoEn) {
}