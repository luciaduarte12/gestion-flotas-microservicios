package com.flota.mantenimiento.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class VehiculoEventListener {

    private static final Logger log = LoggerFactory.getLogger(VehiculoEventListener.class);

    private static final int INTERVALO_SERVICE_KM = 10_000;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void alRecibirCambioDeEstado(VehiculoEstadoCambiadoEvent evento) {
        log.info("Vehículo {} ({}): {} -> {} a {} km",
                evento.vehiculoId(), evento.patente(),
                evento.estadoAnterior(), evento.estadoNuevo(), evento.kilometraje());

        if (evento.kilometraje() >= INTERVALO_SERVICE_KM) {
            log.warn("ALERTA: el vehículo {} ({}) superó {} km y requiere service preventivo",
                    evento.vehiculoId(), evento.patente(), INTERVALO_SERVICE_KM);
        }
    }
}
