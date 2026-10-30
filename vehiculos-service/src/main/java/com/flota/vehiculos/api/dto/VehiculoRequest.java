package com.flota.vehiculos.api.dto;

public record VehiculoRequest(String patente, String marca, String modelo, int kilometraje) {
}