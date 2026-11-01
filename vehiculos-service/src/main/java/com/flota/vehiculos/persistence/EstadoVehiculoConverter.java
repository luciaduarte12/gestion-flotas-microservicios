package com.flota.vehiculos.persistence;

import com.flota.vehiculos.domain.estado.Disponible;
import com.flota.vehiculos.domain.estado.EnMantenimiento;
import com.flota.vehiculos.domain.estado.EnRuta;
import com.flota.vehiculos.domain.estado.EstadoVehiculo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoVehiculoConverter implements AttributeConverter<EstadoVehiculo, String> {

    @Override
    public String convertToDatabaseColumn(EstadoVehiculo estado) {
        return estado == null ? null : estado.getNombre();
    }

    @Override
    public EstadoVehiculo convertToEntityAttribute(String nombre) {
        if (nombre == null) {
            return null;
        }
        return switch (nombre) {
            case "DISPONIBLE" -> Disponible.INSTANCIA;
            case "EN_RUTA" -> EnRuta.INSTANCIA;
            case "EN_MANTENIMIENTO" -> EnMantenimiento.INSTANCIA;
            default -> throw new IllegalArgumentException(
                    "Estado de vehículo desconocido en la base de datos: " + nombre);
        };
    }
}