package com.grupo05.coworking_space.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.grupo05.coworking_space.enums.ApiSuccess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Clase que representa la respuesta de la API.
 * Esta clase encapsula los datos de la respuesta, incluyendo
 * el objeto de respuesta, si hay un error, el mensaje de la respuesta,
 * el código de estado HTTP y la marca de tiempo de la respuesta.
 * @Getter es una anotación de Lombok que genera automáticamente los métodos getter.
 * @Schema es una anotación de Swagger que permite documentar la clase.
 * Para más información sobre la respuesta de la API,
 * ver la documentación de Swagger en: localhost:8080/api/swagger-ui.html
 */
@Getter
@Schema(description = "Objeto de respuesta de la API")
public class DataResponse {
	@Schema(description = "Datos de la respuesta reservationDTO", type = "object")
	private final Object data;

	@Schema(description = "Indica si hay un error", type = "boolean", example = "false")
	private final boolean hasError;

	@Schema(description = "Mensaje de la respuesta", type = "string")
	private final String message;

	@Schema(description = "Código de estado HTTP", type = "integer", example = "200")
	private final int statusCode;

	@Schema(description = "Marca de tiempo de la respuesta", format = "date-time")
	private final LocalDateTime timestamp;

	public DataResponse(ApiSuccess apiSuccess, Object data) {
		this.data = data;
		this.hasError = false;
		this.message = apiSuccess.getMessage();
		this.statusCode = apiSuccess.getStatus().value();
		this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
	}
}