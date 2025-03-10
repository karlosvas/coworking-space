package com.grupo05.coworking_space.exception;

import com.grupo05.coworking_space.dto.ExceptionDTO;
import com.grupo05.coworking_space.enums.ApiError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador centralizado de excepciones para la API.
 * Esta clase intercepta las excepciones lanzadas durante el procesamiento de las peticiones
 * y las convierte en respuestas HTTP estructuradas y consistentes.
 * 
 * La anotación @RestControllerAdvice permite que los métodos de esta clase
 * se apliquen globalmente a todos los controladores de la aplicación.
 */
@RestControllerAdvice
public class APIExceptionHandler {
	 /**
     * Maneja excepciones de validación de argumentos de métodos.
     * Se activa cuando los datos de entrada no cumplen con las reglas de validación
     * especificadas por anotaciones como @Valid, @NotNull, etc.
     * 
     * @param ex La excepción de validación capturada
     * @return ResponseEntity con detalles estructurados sobre los errores de validación
     */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(
				error.getField(),
				error.getDefaultMessage()));
		return new ResponseEntity<>(
				new ExceptionDTO(
						ApiError.INVALID_REQUEST_DATA.getTitle(),
						ApiError.INVALID_REQUEST_DATA.getDetail(),
						ApiError.INVALID_REQUEST_DATA.getStatus().value(),
						errors,
						ZonedDateTime.now().toLocalDateTime()),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RequestException.class)
	public ResponseEntity<ExceptionDTO> handleApiRequestException(RequestException ex) {
		ExceptionDTO apiException = new ExceptionDTO(
			ex.getTitle(),
			ex.getDetail(),
			ex.getStatusCode().value(),
			ex.getReasons(),
			ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime());

		return new ResponseEntity<>(apiException, ex.getStatusCode());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionDTO> handleNotFoundException(NoHandlerFoundException ex) {
		return new ResponseEntity<>(
				new ExceptionDTO(
						ApiError.ENDPOINT_NOT_FOUND.getTitle(),
						ApiError.ENDPOINT_NOT_FOUND.getDetail(),
						ApiError.ENDPOINT_NOT_FOUND.getStatus().value(),
						null,
						ZonedDateTime.now().toLocalDateTime()),
				ApiError.ENDPOINT_NOT_FOUND.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDTO> handleAllExceptions(Exception ex) {
		return new ResponseEntity<>(
				new ExceptionDTO(
						ApiError.INTERNAL_SERVER_ERROR.getTitle(),
						ApiError.INTERNAL_SERVER_ERROR.getDetail(),
						ApiError.INTERNAL_SERVER_ERROR.getStatus().value(),
						null,
						ZonedDateTime.now().toLocalDateTime()),
				ApiError.INTERNAL_SERVER_ERROR.getStatus());
	}
}
