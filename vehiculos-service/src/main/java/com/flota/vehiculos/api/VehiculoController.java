package com.flota.vehiculos.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.flota.vehiculos.api.dto.VehiculoRequest;
import com.flota.vehiculos.api.dto.VehiculoResponse;
import com.flota.vehiculos.domain.Vehiculo;
import com.flota.vehiculos.persistence.VehiculoRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoRepository repository;

    public VehiculoController(VehiculoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculoResponse crear(@RequestBody VehiculoRequest request) {
        Vehiculo vehiculo = new Vehiculo(request.patente(), request.marca(),
                request.modelo(), request.kilometraje());
        return VehiculoResponse.desde(repository.save(vehiculo));
    }

    @GetMapping
    public List<VehiculoResponse> listar() {
        return repository.findAll().stream().map(VehiculoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public VehiculoResponse obtener(@PathVariable Long id) {
        return VehiculoResponse.desde(buscar(id));
    }

    @PostMapping("/{id}/iniciar-ruta")
    public VehiculoResponse iniciarRuta(@PathVariable Long id) {
        Vehiculo vehiculo = buscar(id);
        vehiculo.iniciarRuta();
        return VehiculoResponse.desde(repository.save(vehiculo));
    }

    @PostMapping("/{id}/finalizar-ruta")
    public VehiculoResponse finalizarRuta(@PathVariable Long id) {
        Vehiculo vehiculo = buscar(id);
        vehiculo.finalizarRuta();
        return VehiculoResponse.desde(repository.save(vehiculo));
    }

    @PostMapping("/{id}/enviar-a-mantenimiento")
    public VehiculoResponse enviarAMantenimiento(@PathVariable Long id) {
        Vehiculo vehiculo = buscar(id);
        vehiculo.enviarAMantenimiento();
        return VehiculoResponse.desde(repository.save(vehiculo));
    }

    @PostMapping("/{id}/finalizar-mantenimiento")
    public VehiculoResponse finalizarMantenimiento(@PathVariable Long id) {
        Vehiculo vehiculo = buscar(id);
        vehiculo.finalizarMantenimiento();
        return VehiculoResponse.desde(repository.save(vehiculo));
    }

    // Una transición inválida (por ejemplo, iniciar ruta estando en mantenimiento) devuelve 409
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> transicionInvalida(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    private Vehiculo buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado: " + id));
    }
}