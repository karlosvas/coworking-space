package com.grupo05.coworking_space.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.grupo05.coworking_space.enums.ApiSuccess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Objeto de respuesta de la API")
public class DataResponse {
	@Schema(description = "Datos de la respuesta reservationDTO", type = "object")
	private final Object data;

	@Schema(description = "Indica si hay un error", type = "boolean", example = "false")
	private final boolean hasError;

	@Schema(description = "Mensaje de la respuesta", type = "string", example = "Operación exitosa")
	private final String message;

	@Schema(description = "Código de estado HTTP", type = "integer")
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