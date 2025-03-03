package com.grupo05.coworking_space.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.grupo05.coworking_space.enums.ApiError;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Excepción personalizada para representar errores de petición HTTP.
 * Esta clase extiende RuntimeException para permitir excepciones no verificadas
 * y proporciona información estructurada sobre los errores que ocurren durante
 * el procesamiento de las peticiones.
 * 
 * La anotación @Getter de Lombok genera automáticamente métodos getter para todos los campos.
 * La anotación @Slf4j proporciona un logger para registrar información sobre las excepciones.
 */
@Getter
@Slf4j
public class RequestException extends RuntimeException {
	private final boolean hasError;
	private final String title;
	private final String detail;
	private final HttpStatus statusCode;
	private Map<String, String> reasons;
	private ApiError apiError;

	public RequestException(ApiError apiError) {
		this.hasError = true;
		this.title = apiError.getTitle();
		this.detail = apiError.getDetail();
		this.statusCode = apiError.getStatus();
		this.apiError = apiError;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}

	public RequestException(
			String title,
			String detail,
			HttpStatus statusCode,
			Map<String, String> reasons) {
		this.hasError = true;
		this.title = title;
		this.detail = detail;
		this.statusCode = statusCode;
		this.reasons = reasons;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}

	public RequestException(ApiError apiError, String title, String detail) {
		this.hasError = true;
		this.title = title;
		this.detail = detail;
		this.statusCode = apiError.getStatus();
		this.apiError = apiError;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}
}
