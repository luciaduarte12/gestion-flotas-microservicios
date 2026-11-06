package com.flota.mantenimiento.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.flota.mantenimiento.domain.Alerta;
import com.flota.mantenimiento.persistence.AlertaRepository;

@Component
public class VehiculoEventListener {

    private static final Logger log = LoggerFactory.getLogger(VehiculoEventListener.class);

    private static final int INTERVALO_SERVICE_KM = 10_000;

    private final AlertaRepository alertaRepository;

    public VehiculoEventListener(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void alRecibirCambioDeEstado(VehiculoEstadoCambiadoEvent evento) {
        log.info("Vehículo {} ({}): {} -> {} a {} km",
                evento.vehiculoId(), evento.patente(),
                evento.estadoAnterior(), evento.estadoNuevo(), evento.kilometraje());

        if (evento.kilometraje() < INTERVALO_SERVICE_KM) {
            return;
        }

        // Una sola alerta abierta por vehículo: no se duplica en cada cambio de estado
        if (alertaRepository.existsByVehiculoIdAndAtendidaFalse(evento.vehiculoId())) {
            log.info("El vehículo {} ya tiene una alerta pendiente", evento.vehiculoId());
            return;
        }

        String mensaje = "El vehículo " + evento.patente() + " superó " + INTERVALO_SERVICE_KM
                + " km y requiere service preventivo";
        alertaRepository.save(new Alerta(evento.vehiculoId(), evento.patente(),
                evento.kilometraje(), mensaje));
        log.warn("ALERTA registrada: {}", mensaje);
    }
}
