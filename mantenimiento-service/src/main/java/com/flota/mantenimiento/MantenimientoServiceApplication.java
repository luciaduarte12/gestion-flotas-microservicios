package com.flota.mantenimiento;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MantenimientoServiceApplication {

    public static void main(String[] args) {
        // PostgreSQL no reconoce el alias "America/Buenos_Aires" que usa Java en Windows.
        TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        SpringApplication.run(MantenimientoServiceApplication.class, args);
    }
}
