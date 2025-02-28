package com.grupo05.coworking_space.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.service.ReservationService;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Esta clase define los endpoints para gestionar las reservas, mas informacion en swagger
 * en la url localhost:8080/api/swagger-ui.html
 * @RestController para indicar que es un controlador de Spring
 * @RequestMapping para indicar la ruta de acceso a los endpoints
 * @Tag para documentar el controlador
 */
@RestController
@RequestMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservation", description = "Enpoints para gestionar las reservas")
public class ReservationController {

	/**
	 * Servicio de reservas
	 * @Operation para documentar el endpoint
	 * @SwaggerApiResponses para documentar las respuestas de error la API
	 * @ApiResponses para documentar una respuestas de la API
	 * @ApiResponse para documentar una respuesta de la API
	 * @Content para documentar el tipo de contenido de la respuesta
	 * @Schema para documentar el esquema de la respuesta
	 * 
	 * @param reservationService servicio de reservas
	 */
	private final ReservationService reservationService;

	public ReservationController(ReservationService service) {
		this.reservationService = service;
	}

	/**
     * Obtiene todas las reservas existentes.
     * 
     * @return ResponseEntity con la lista de reservas o mensaje de no contenido
     * @GetMapping Mapea solicitudes HTTP GET a este método
     */
	@Operation(summary = "Obtener todas las reservas", description = "Devuelve una lista con todas las reservas de tipo ReservationDTO")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de reservas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
			@ApiResponse(responseCode = "204", description = "No hay ninguna reserva", content = @Content)
	})
	@GetMapping
	public ResponseEntity<DataResponse> findAllReservations() {
		List<ReservationDTO> allReserves = reservationService.findAllReservations();

		if (allReserves.isEmpty())
			return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, allReserves);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allReserves);
	}

	 /**
     * Busca una reserva por su identificador único.
     *
     * @param id ID de la reserva a buscar
     * @return ResponseEntity con la reserva encontrada
     * @PathVariable Vincula el parámetro de la URL al parámetro del método
     * @GetMapping Mapea solicitudes HTTP GET a este método, con una variable en la ruta
     */
	@Operation(summary = "Obtener reserva por id", description = "Devuelve una reserva por su ID")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Reserva encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findReservationById(@PathVariable("id") int id) {
		ReservationDTO foundReservation = reservationService.findReservationByID(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundReservation);
	}

	/**
     * Crea una nueva reserva.
     * 
     * @param reservation DTO con la información de la reserva a crear
     * @return ResponseEntity con la reserva creada
     * @throws RequestException si la fecha ya está ocupada
     * @RequestBody Vincula el cuerpo de la solicitud HTTP al parámetro del método
     * @PostMapping Mapea solicitudes HTTP POST a este método
     */
	@Operation(summary = "Crear reserva", description = "Crea una nueva reserva con la informacion enviada, los FK deven ser valores existentes en la base de datos")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Reserva creada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@PostMapping
	public ResponseEntity<DataResponse> createReservation(@RequestBody ReservationDTO reservation) {
		List<ReservationDTO> dates = reservationService.findReservationsBetweenDates(reservation.getDateInit(),
				reservation.getDateEnd());
		if (!dates.isEmpty())
			throw new RequestException(ApiError.DATE_NOT_AVAILABLE);

		ReservationDTO createdReservation = reservationService.createReservation(reservation);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_CREATED, createdReservation);
	}

	/**
     * Actualiza una reserva existente.
     * 
     * @param id ID de la reserva a actualizar
     * @param reservation DTO con la nueva información
     * @return ResponseEntity con la reserva actualizada
     * @PutMapping Mapea solicitudes HTTP PUT a este método
     */
	@Operation(summary = "Actualizar reserva", description = "Actualiza una reserva con la informacion enviada")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Reserva actualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateReservation(@PathVariable("id") int id,
			@RequestBody ReservationDTO reservation) {
		ReservationDTO updatedReservation = reservationService.updateResevation(reservation, id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedReservation);
	}

	/**
     * Elimina una reserva por su ID.
     * 
     * @param id ID de la reserva a eliminar
     * @return ResponseEntity con mensaje de éxito
     * @DeleteMapping Mapea solicitudes HTTP DELETE a este método
     */
	@Operation(summary = "Eliminar reserva", description = "Elimina una reserva por su ID")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "204", description = "Reserva eliminada", content = @Content)
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
		reservationService.deleteReservation(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}

	/**
     * Busca reservas entre un rango de fechas.
     * 
     * @param dateInit Fecha de inicio (opcional, por defecto 1 año atrás)
     * @param dateEnd Fecha final (opcional, por defecto 1 año adelante)
     * @return ResponseEntity con la lista de reservas encontradas
     * @throws IllegalArgumentException si la fecha inicial es posterior a la final
     * @RequestParam Vincula los parámetros de consulta de la URL a los parámetros del método
     * @GetMapping Mapea solicitudes HTTP GET a este método, en la ruta /filters
     * @DateTimeFormat Define el formato de la fecha recibida
     */
	@Operation(summary = "Obtener reservas entre fechas", description = "Devuelve una lista con todas las reservas entre dos fechas")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Lista de reservas entre fechas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@GetMapping("/filters")
	public ResponseEntity<DataResponse> findReservationsBetweenDates(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateInit,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateEnd) {
		LocalDateTime defaultDateInit = LocalDateTime.now().minusYears(1);
		LocalDateTime defaultDateEnd = LocalDateTime.now().plusYears(1);

		LocalDateTime start = dateInit != null ? dateInit : defaultDateInit;
		LocalDateTime end = dateEnd != null ? dateEnd : defaultDateEnd;

		if (start.isAfter(end)) {
			throw new IllegalArgumentException("La fecha inicial no puede ser posterior a la fecha final");
		}

		List<ReservationDTO> allReserves = reservationService.findReservationsBetweenDates(start, end);

		if (allReserves.isEmpty())
			return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, allReserves);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allReserves);
	}
}