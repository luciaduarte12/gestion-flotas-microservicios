package com.flota.mantenimiento.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "flota.vehiculos";
    public static final String ROUTING_KEY = "vehiculo.estado.cambiado";
    public static final String QUEUE = "mantenimiento.vehiculo-estado";

    @Bean
    public TopicExchange vehiculosExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue colaEstadoVehiculo() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue colaEstadoVehiculo, TopicExchange vehiculosExchange) {
        return BindingBuilder.bind(colaEstadoVehiculo).to(vehiculosExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}