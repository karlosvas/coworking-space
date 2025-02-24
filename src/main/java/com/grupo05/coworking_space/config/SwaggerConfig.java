package com.grupo05.coworking_space.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Coworking Space API",
		version = "1.0",
		description = "Documentación de la API para gestionar usuarios, reservas y salas, para espacios de coworking",
		termsOfService = "http://swagger.io/terms/"
	), security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	paramName = HttpHeaders.AUTHORIZATION,
	in = SecuritySchemeIn.HEADER,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class SwaggerConfig {}