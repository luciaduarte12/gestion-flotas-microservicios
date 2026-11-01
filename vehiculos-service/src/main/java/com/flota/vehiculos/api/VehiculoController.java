package com.flota.vehiculos.api;

import java.util.List;
import java.util.function.Consumer;

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
import com.flota.vehiculos.messaging.VehiculoEventPublisher;
import com.flota.vehiculos.persistence.VehiculoRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoRepository repository;
    private final VehiculoEventPublisher publisher;

    public VehiculoController(VehiculoRepository repository, VehiculoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
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
        return transicionar(id, Vehiculo::iniciarRuta);
    }

    @PostMapping("/{id}/finalizar-ruta")
    public VehiculoResponse finalizarRuta(@PathVariable Long id) {
        return transicionar(id, Vehiculo::finalizarRuta);
    }

    @PostMapping("/{id}/enviar-a-mantenimiento")
    public VehiculoResponse enviarAMantenimiento(@PathVariable Long id) {
        return transicionar(id, Vehiculo::enviarAMantenimiento);
    }

    @PostMapping("/{id}/finalizar-mantenimiento")
    public VehiculoResponse finalizarMantenimiento(@PathVariable Long id) {
        return transicionar(id, Vehiculo::finalizarMantenimiento);
    }

    // Aplica la transición, guarda y publica el evento con el estado anterior y el nuevo
    private VehiculoResponse transicionar(Long id, Consumer<Vehiculo> accion) {
        Vehiculo vehiculo = buscar(id);
        String estadoAnterior = vehiculo.getEstado().getNombre();
        accion.accept(vehiculo);
        Vehiculo guardado = repository.save(vehiculo);
        publisher.publicarCambioDeEstado(guardado, estadoAnterior);
        return VehiculoResponse.desde(guardado);
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