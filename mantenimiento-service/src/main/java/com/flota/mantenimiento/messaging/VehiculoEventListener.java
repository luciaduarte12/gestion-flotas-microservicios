package com.flota.mantenimiento.messaging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.flota.mantenimiento.domain.Alerta;
import com.flota.mantenimiento.domain.UltimoService;
import com.flota.mantenimiento.persistence.AlertaRepository;
import com.flota.mantenimiento.persistence.UltimoServiceRepository;

@Component
public class VehiculoEventListener {

    private static final Logger log = LoggerFactory.getLogger(VehiculoEventListener.class);

    private static final int INTERVALO_SERVICE_KM = 10_000;

    private final AlertaRepository alertaRepository;
    private final UltimoServiceRepository ultimoServiceRepository;

    public VehiculoEventListener(AlertaRepository alertaRepository,
                                 UltimoServiceRepository ultimoServiceRepository) {
        this.alertaRepository = alertaRepository;
        this.ultimoServiceRepository = ultimoServiceRepository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    @Transactional
    public void alRecibirCambioDeEstado(VehiculoEstadoCambiadoEvent evento) {
        log.info("Vehículo {} ({}): {} -> {} a {} km",
                evento.vehiculoId(), evento.patente(),
                evento.estadoAnterior(), evento.estadoNuevo(), evento.kilometraje());

        // Salió de mantenimiento: el service se hizo, así que se registra y se cierran las alertas
        if (terminoMantenimiento(evento)) {
            registrarService(evento);
            return;
        }

        // Se alerta solo si pasaron 10.000 km desde el último service
        int kmUltimoService = ultimoServiceRepository.findById(evento.vehiculoId())
                .map(UltimoService::getKilometraje)
                .orElse(0);
        if (evento.kilometraje() - kmUltimoService < INTERVALO_SERVICE_KM) {
            return;
        }

        // Una sola alerta abierta por vehículo
        if (alertaRepository.existsByVehiculoIdAndAtendidaFalse(evento.vehiculoId())) {
            log.info("El vehículo {} ya tiene una alerta pendiente", evento.vehiculoId());
            return;
        }

        String mensaje = "El vehículo " + evento.patente() + " superó " + INTERVALO_SERVICE_KM
                + " km desde su último service y requiere service preventivo";
        alertaRepository.save(new Alerta(evento.vehiculoId(), evento.patente(),
                evento.kilometraje(), mensaje));
        log.warn("ALERTA registrada: {}", mensaje);
    }

    private boolean terminoMantenimiento(VehiculoEstadoCambiadoEvent evento) {
        return "EN_MANTENIMIENTO".equals(evento.estadoAnterior())
                && "DISPONIBLE".equals(evento.estadoNuevo());
    }

    private void registrarService(VehiculoEstadoCambiadoEvent evento) {
        ultimoServiceRepository.save(new UltimoService(evento.vehiculoId(), evento.kilometraje()));

        List<Alerta> abiertas = alertaRepository.findByVehiculoIdAndAtendidaFalse(evento.vehiculoId());
        abiertas.forEach(Alerta::atender);
        alertaRepository.saveAll(abiertas);

        log.info("Service registrado para el vehículo {} a {} km; alertas cerradas: {}",
                evento.vehiculoId(), evento.kilometraje(), abiertas.size());
    }
}
