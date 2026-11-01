package com.flota.vehiculos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flota.vehiculos.domain.estado.Disponible;
import com.flota.vehiculos.domain.estado.EnMantenimiento;
import com.flota.vehiculos.domain.estado.EnRuta;

class VehiculoTest {

    private Vehiculo vehiculo;

    @BeforeEach
    void crearVehiculo() {
        vehiculo = new Vehiculo("AB123CD", "Renault", "Kangoo", 15000);
    }

    @Test
    void unVehiculoNuevoNaceDisponible() {
        assertThat(vehiculo.getEstado()).isSameAs(Disponible.INSTANCIA);
    }

    @Test
    void recorreElCicloDeRuta() {
        vehiculo.iniciarRuta();
        assertThat(vehiculo.getEstado()).isSameAs(EnRuta.INSTANCIA);

        vehiculo.finalizarRuta();
        assertThat(vehiculo.getEstado()).isSameAs(Disponible.INSTANCIA);
    }

    @Test
    void recorreElCicloDeMantenimiento() {
        vehiculo.enviarAMantenimiento();
        assertThat(vehiculo.getEstado()).isSameAs(EnMantenimiento.INSTANCIA);

        vehiculo.finalizarMantenimiento();
        assertThat(vehiculo.getEstado()).isSameAs(Disponible.INSTANCIA);
    }

    @Test
    void unaTransicionInvalidaNoCambiaElEstado() {
        vehiculo.iniciarRuta();

        assertThatThrownBy(() -> vehiculo.enviarAMantenimiento())
                .isInstanceOf(IllegalStateException.class);

        assertThat(vehiculo.getEstado()).isSameAs(EnRuta.INSTANCIA);
    }

    @Test
    void noSePuedeIniciarRutaEstandoEnMantenimiento() {
        vehiculo.enviarAMantenimiento();

        assertThatThrownBy(() -> vehiculo.iniciarRuta())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede iniciar ruta con el vehículo en estado EN_MANTENIMIENTO");
    }
}
