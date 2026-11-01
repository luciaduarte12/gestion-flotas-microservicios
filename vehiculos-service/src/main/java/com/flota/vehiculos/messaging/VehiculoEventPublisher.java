package com.flota.vehiculos.messaging;

import java.time.Instant;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.flota.vehiculos.domain.Vehiculo;

@Component
public class VehiculoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public VehiculoEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarCambioDeEstado(Vehiculo vehiculo, String estadoAnterior) {
        VehiculoEstadoCambiadoEvent evento = new VehiculoEstadoCambiadoEvent(
                vehiculo.getId(),
                vehiculo.getPatente(),
                estadoAnterior,
                vehiculo.getEstado().getNombre(),
                vehiculo.getKilometraje(),
                Instant.now());

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, evento);
    }
}