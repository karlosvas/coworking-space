package com.grupo05.coworking_space.dto;

import java.time.LocalDateTime;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "DTO para manejar excepciones")
public class ExceptionDTO {
	@Schema(description = "Indica si hay un error", type = "boolean", example = "true")
	private final boolean hasError = true;

	@Schema(description = "Mensaje del error", type = "string")
	private final String message;

	@Schema(description = "Descripción detallada del error", type = "string")
	private final String description;

	@Schema(description = "Código de error", type = "integer")
	private final int code;

	@Schema(description = "Razones adicionales del error", type = "map")
	private final Map<String, String> reasons;

	@Schema(description = "Marca de tiempo del error", format = "date-time")
	private final LocalDateTime timestamp;
}