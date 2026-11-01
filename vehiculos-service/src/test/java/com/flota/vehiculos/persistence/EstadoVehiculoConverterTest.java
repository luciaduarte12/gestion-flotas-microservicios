package com.flota.vehiculos.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.flota.vehiculos.domain.estado.Disponible;
import com.flota.vehiculos.domain.estado.EnMantenimiento;
import com.flota.vehiculos.domain.estado.EnRuta;
import com.flota.vehiculos.domain.estado.EstadoVehiculo;

class EstadoVehiculoConverterTest {

    private final EstadoVehiculoConverter converter = new EstadoVehiculoConverter();

    @Test
    void convierteCadaEstadoASuTexto() {
        assertThat(converter.convertToDatabaseColumn(Disponible.INSTANCIA)).isEqualTo("DISPONIBLE");
        assertThat(converter.convertToDatabaseColumn(EnRuta.INSTANCIA)).isEqualTo("EN_RUTA");
        assertThat(converter.convertToDatabaseColumn(EnMantenimiento.INSTANCIA)).isEqualTo("EN_MANTENIMIENTO");
    }

    @Test
    void convierteCadaTextoASuEstado() {
        assertThat(converter.convertToEntityAttribute("DISPONIBLE")).isSameAs(Disponible.INSTANCIA);
        assertThat(converter.convertToEntityAttribute("EN_RUTA")).isSameAs(EnRuta.INSTANCIA);
        assertThat(converter.convertToEntityAttribute("EN_MANTENIMIENTO")).isSameAs(EnMantenimiento.INSTANCIA);
    }

    @Test
    void guardarYLeerDevuelveElMismoEstado() {
        for (EstadoVehiculo estado : new EstadoVehiculo[] {
                Disponible.INSTANCIA, EnRuta.INSTANCIA, EnMantenimiento.INSTANCIA }) {
            String guardado = converter.convertToDatabaseColumn(estado);
            assertThat(converter.convertToEntityAttribute(guardado)).isSameAs(estado);
        }
    }

    @Test
    void elNullSeConvierteEnNullEnAmbasDirecciones() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    void unTextoDesconocidoLanzaExcepcion() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute("ROTO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ROTO");
    }
}