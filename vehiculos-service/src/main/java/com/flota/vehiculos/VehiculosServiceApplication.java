package com.flota.vehiculos;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VehiculosServiceApplication {

    public static void main(String[] args) {
        // PostgreSQL no reconoce el alias "America/Buenos_Aires" que usa Java en Windows.
        // Se fija el nombre oficial antes de abrir cualquier conexión a la base.
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        SpringApplication.run(VehiculosServiceApplication.class, args);
    }
}