package com.flota.vehiculos.domain.estado;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EstadoVehiculoTest {

    // ---------- Disponible ----------

    @Test
    void disponible_iniciarRuta_pasaAEnRuta() {
        assertThat(Disponible.INSTANCIA.iniciarRuta()).isSameAs(EnRuta.INSTANCIA);
    }

    @Test
    void disponible_enviarAMantenimiento_pasaAEnMantenimiento() {
        assertThat(Disponible.INSTANCIA.enviarAMantenimiento()).isSameAs(EnMantenimiento.INSTANCIA);
    }

    @Test
    void disponible_finalizarRuta_lanzaExcepcion() {
        assertThatThrownBy(() -> Disponible.INSTANCIA.finalizarRuta())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede finalizar ruta con el vehículo en estado DISPONIBLE");
    }

    @Test
    void disponible_finalizarMantenimiento_lanzaExcepcion() {
        assertThatThrownBy(() -> Disponible.INSTANCIA.finalizarMantenimiento())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede finalizar mantenimiento con el vehículo en estado DISPONIBLE");
    }

    // ---------- EnRuta ----------

    @Test
    void enRuta_finalizarRuta_vuelveADisponible() {
        assertThat(EnRuta.INSTANCIA.finalizarRuta()).isSameAs(Disponible.INSTANCIA);
    }

    @Test
    void enRuta_iniciarRuta_lanzaExcepcion() {
        assertThatThrownBy(() -> EnRuta.INSTANCIA.iniciarRuta())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede iniciar ruta con el vehículo en estado EN_RUTA");
    }

    @Test
    void enRuta_enviarAMantenimiento_lanzaExcepcion() {
        assertThatThrownBy(() -> EnRuta.INSTANCIA.enviarAMantenimiento())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede enviar a mantenimiento con el vehículo en estado EN_RUTA");
    }

    @Test
    void enRuta_finalizarMantenimiento_lanzaExcepcion() {
        assertThatThrownBy(() -> EnRuta.INSTANCIA.finalizarMantenimiento())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede finalizar mantenimiento con el vehículo en estado EN_RUTA");
    }

    // ---------- EnMantenimiento ----------

    @Test
    void enMantenimiento_finalizarMantenimiento_vuelveADisponible() {
        assertThat(EnMantenimiento.INSTANCIA.finalizarMantenimiento()).isSameAs(Disponible.INSTANCIA);
    }

    @Test
    void enMantenimiento_iniciarRuta_lanzaExcepcion() {
        assertThatThrownBy(() -> EnMantenimiento.INSTANCIA.iniciarRuta())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede iniciar ruta con el vehículo en estado EN_MANTENIMIENTO");
    }

    @Test
    void enMantenimiento_finalizarRuta_lanzaExcepcion() {
        assertThatThrownBy(() -> EnMantenimiento.INSTANCIA.finalizarRuta())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede finalizar ruta con el vehículo en estado EN_MANTENIMIENTO");
    }

    @Test
    void enMantenimiento_enviarAMantenimiento_lanzaExcepcion() {
        assertThatThrownBy(() -> EnMantenimiento.INSTANCIA.enviarAMantenimiento())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede enviar a mantenimiento con el vehículo en estado EN_MANTENIMIENTO");
    }
}