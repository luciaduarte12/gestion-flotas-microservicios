package com.flota.mantenimiento.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.flota.mantenimiento.api.dto.AlertaResponse;
import com.flota.mantenimiento.domain.Alerta;
import com.flota.mantenimiento.persistence.AlertaRepository;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaRepository repository;

    public AlertaController(AlertaRepository repository) {
        this.repository = repository;
    }

    // GET /alertas            -> todas, la más reciente primero
    // GET /alertas?atendida=false -> solo las pendientes
    @GetMapping
    public List<AlertaResponse> listar(@RequestParam(required = false) Boolean atendida) {
        List<Alerta> alertas = (atendida == null)
                ? repository.findAllByOrderByCreadaEnDesc()
                : repository.findByAtendidaOrderByCreadaEnDesc(atendida);
        return alertas.stream().map(AlertaResponse::desde).toList();
    }

    @PostMapping("/{id}/atender")
    public AlertaResponse atender(@PathVariable Long id) {
        Alerta alerta = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta no encontrada: " + id));
        alerta.atender();
        return AlertaResponse.desde(repository.save(alerta));
    }
}