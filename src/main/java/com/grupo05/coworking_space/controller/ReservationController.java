package com.grupo05.coworking_space.controller;

import java.util.List;
import java.time.LocalDateTime;
import com.grupo05.coworking_space.dto.RequestReservationDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.service.ReservationService;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;
import com.grupo05.coworking_space.utils.SwaggerExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Esta clase define los endpoints para gestionar las reservas, mas informacion en swagger
 * en la url localhost:8080/api/swagger-ui.html
 * @RestController para indicar que es un controlador de Spring
 * @RequestMapping para indicar la ruta de acceso a los endpoints
 * @Tag para documentar el controlador
 */
@Validated
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
     * Obtiene todas las reservas existentes, que pertenezcan al usuario logeado.
     *
     * @return ResponseEntity con la lista de reservas o mensaje de no contenido
     * @GetMapping Mapea solicitudes HTTP GET a este método
     */
	@Operation(summary = "Obtener todas las reservas del usuario actual", description = "Devuelve una lista con todas las reservas que pertenezcan unicamente al usuario logeado")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de reservas",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
			@ApiResponse(responseCode = "204", description = "No hay ninguna reserva",
			content = @Content)
	})
	@GetMapping
	public ResponseEntity<DataResponse> findAllReservationsFiltered() {
		List<ReservationDTO> allReserves = reservationService.findAllReservationsFiltered();

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
	@ApiResponse(responseCode = "200", description = "Reserva encontrada",
	content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findReservationById(@PathVariable("id") int id) {
		ReservationDTO foundReservation = reservationService.findReservationByID(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundReservation);
	}

	/**
     * Crea una nueva reserva.
     *
     * @param requestReservationDTO DTO con la información de la reserva a crear
     * @return ResponseEntity con la reserva creada
     * @throws RequestException si la fecha ya está ocupada
     * @RequestBody Vincula el cuerpo de la solicitud HTTP al parámetro del método
     * @PostMapping Mapea solicitudes HTTP POST a este método
     */
	@Operation(summary = "Crear reserva", description = "Crea una nueva reserva con la informacion enviada, los FK deven ser valores existentes en la base de datos. IMPORTANTE: las fechas de reserva no pueden ser menores a la fecha actual, y solo se pueden hacer reservas para el usaurio actualmente logeado")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "201", description = "Reserva creada",
			content = @Content(mediaType = "application/json", 
			schema = @Schema(implementation = DataResponse.class),
			examples = { @ExampleObject(value = SwaggerExamples.DataResponseExamples.CREATED_EXAMPLE) }))
	@PostMapping
	public ResponseEntity<DataResponse> createReservation(@RequestBody RequestReservationDTO requestReservationDTO) {
			List<ReservationDTO> dates = reservationService.findReservationsBetweenDates(requestReservationDTO.getReservationDTO().getDateInit(),
			requestReservationDTO.getReservationDTO().getDateEnd());
			if (!dates.isEmpty())
				throw new RequestException(ApiError.DATE_NOT_AVAILABLE);
			
			ReservationDTO createdReservation = reservationService.createReservation(requestReservationDTO);
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
	@Operation(summary = "Actualizar reserva", description = "Actualiza una reserva con la información enviada")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Reserva actualizada",
	content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class)))
	@PutMapping()
	public ResponseEntity<DataResponse> updateReservation(@Valid @RequestBody ReservationDTO reservation) {
		List<ReservationDTO> dates = reservationService.findReservationsBetweenDates(reservation.getDateInit(),
		reservation.getDateEnd());
			if (!dates.isEmpty())
				throw new RequestException(ApiError.DATE_NOT_AVAILABLE);
		ReservationDTO updatedReservation = reservationService.updateResevation(reservation);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedReservation);
	}

	/**
     * Busca reservas entre un rango de fechas.
     *
     * @param dateInit Fecha de inicio 
     * @param dateEnd Fecha final 
     * @return ResponseEntity con la lista de reservas encontradas
     * @throws IllegalArgumentException si la fecha inicial es posterior a la final
     * @RequestParam Vincula los parámetros de consulta de la URL a los parámetros del método
     * @GetMapping Mapea solicitudes HTTP GET a este método, en la ruta /filters
     * @DateTimeFormat Define el formato de la fecha recibida
     */
	@Operation(summary = "Obtener reservas entre fechas", description = "Devuelve una lista con todas las reservas entre dos fechas, los valores deven ser mayores a la fecha actual y la fecha inicial no puede ser mayor a la fecha final")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Lista de reservas entre fechas",
	content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class), examples = {
        @ExampleObject(
            value = "{\"statusCode\":200,\"message\":\"Recursos recuperados exitosamente\",\"data\":[{\"id\":1,\"dateInit\":\"2025-02-03T10:00:00\",\"dateEnd\":\"2025-02-03T12:00:00\",\"reserveStatus\":\"CONFIRMED\",\"description\":\"Reunión de equipo\",\"user\":{\"id\":1,\"username\":\"usuario1\"},\"rooms\":[{\"id\":101,\"name\":\"Sala A\"}]},{\"id\":2,\"dateInit\":\"2025-02-15T14:30:00\",\"dateEnd\":\"2025-02-15T16:00:00\",\"reserveStatus\":\"PENDING\",\"description\":\"Entrevista\",\"user\":{\"id\":1,\"username\":\"usuario1\"},\"rooms\":[{\"id\":102,\"name\":\"Sala B\"}]}]}"
        )
    }))
	@GetMapping("/filters")
	public ResponseEntity<DataResponse> findReservationsBetweenDates(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateInit,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateEnd) {
		LocalDateTime defaultDateInit = LocalDateTime.now().minusYears(1);
		LocalDateTime defaultDateEnd = LocalDateTime.now().plusYears(1);

		LocalDateTime start = dateInit != null ? dateInit : defaultDateInit;
		LocalDateTime end = dateEnd != null ? dateEnd : defaultDateEnd;

		if (start.isAfter(end)) {
			throw new RequestException(ApiError.DATE_NOT_AVAILABLE);
		}

		List<ReservationDTO> allReserves = reservationService.findReservationsBetweenDates(start, end);

		if (allReserves.isEmpty())
			return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, allReserves);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allReserves);
	}

	/**
     * Elimina una reserva por su ID.
     * 
     * @param id ID de la reserva a eliminar
     * @return ResponseEntity con mensaje de éxito
     * @DeleteMapping Mapea solicitudes HTTP DELETE a este método
     */
	@Operation(summary = "Eliminar reserva", description = "Elimina una reserva por su ID, enpoint solo para administradores")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "204", description = "Reserva eliminada", content = @Content)
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteReservation(@PathVariable("id") int id) {
		reservationService.deleteReservation(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}

	/**
	 * Elimina todas las reservas existentes de un usuario
	 * 
	 * @return ResponseEntity con mensaje de éxito
	 * @DeleteMapping Mapea solicitudes HTTP DELETE a este método
	 */
	@Operation(summary = "Eliminar todas las reservas de un usuario", description = "Elimina todas las reservas de un usuario pasandole el ID de este, enpoint solo para administradores")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "204", description = "Reservas eliminadas", content = @Content)
	@DeleteMapping("/user/{id}")
	public ResponseEntity<DataResponse> deleteAllReservationsByUser(@PathVariable("id") int id) {
		reservationService.deleteAllReservationsByUser(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}
}