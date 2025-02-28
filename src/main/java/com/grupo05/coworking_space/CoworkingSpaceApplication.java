package com.grupo05.coworking_space;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @CroosOrigin para permitir que "*" tenga acceso al backend
 * @SpringBootApplication para indicar que es una aplicacion de Spring Boot
 * @EnableAspectJAutoProxy para habilitar el uso de Aspectos
 */
@CrossOrigin
@SpringBootApplication
@EnableAspectJAutoProxy
public class CoworkingSpaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoworkingSpaceApplication.class, args);
	}
}
