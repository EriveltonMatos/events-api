package com.example.eventsapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API de Eventos", version = "1.0", description = "API para gerenciar eventos"))
public class EventsApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventsApiApplication.class, args);
	}
}