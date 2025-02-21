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
import org.springframework.web.bind.annotation.RestController;

import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.service.ReservationService;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservation", description = "Enpoints para gestionar las reservas")
public class ReservationController {
	private final ReservationService reservationService;

	public ReservationController(ReservationService service) {
		this.reservationService = service;
	}

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

	@Operation(summary = "Obtener reserva por id", description = "Devuelve una reserva por su ID")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reserva encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findReservationById(@PathVariable("id") int id) {
		ReservationDTO foundReservation = reservationService.findReservationByID(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundReservation);
	}

	@Operation(summary = "Crear reserva", description = "Crea una nueva reserva con la informacion enviada, los FK deven ser valores existentes en la base de datos")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reserva creada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@PostMapping
	public ResponseEntity<DataResponse> createReservation(@RequestBody ReservationDTO reservation) {
		ReservationDTO createdReservation = reservationService.createReservation(reservation);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_CREATED, createdReservation);
	}

	@Operation(summary = "Actualizar reserva", description = "Actualiza una reserva con la informacion enviada")
	@SwaggerApiResponses
	@ApiResponse(responseCode = "200", description = "Reserva actualizada")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reserva actualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateReservation(@PathVariable("id") int id,
			@RequestBody ReservationDTO reservation) {
		ReservationDTO updatedReservation = reservationService.updateResevation(reservation, id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedReservation);
	}

	@Operation(summary = "Eliminar reserva", description = "Elimina una reserva por su ID")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Reserva eliminada", content = @Content)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
		reservationService.deleteReservation(id);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}
}