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

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.service.ReservationService;

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

	@Operation(summary = "Obtener todas las reservas", description = "Devuelbe una lista con todas las reservas")
	@ApiResponse(responseCode = "200", description = "Lista de reservas")
	@GetMapping("/all")
	public ResponseEntity<List<ReservationDTO>> findAllReservations() {
		List<ReservationDTO> allReserves = reservationService.findAllReservations();
		return ResponseEntity.ok().body(allReserves);
	}

	@Operation(summary = "Obtener reserva por id", description = "Devuelve una reserva por su ID")
	@ApiResponse(responseCode = "200", description = "Reserva encontrada")
	@GetMapping("/{id}")
	public ResponseEntity<ReservationDTO> findReservationById(@PathVariable("id") int id) {
		ReservationDTO foundReservation = reservationService.findReservationByID(id);
		return ResponseEntity.ok().body(foundReservation);
	}

	@Operation(summary = "Crear reserva", description = "Crea una nueva reserva con la informacion enviada")
	@ApiResponse(responseCode = "200", description = "Reserva creada")
	@PostMapping
	public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservation) {
		ReservationDTO createdReservation = reservationService.createReservation(reservation);
		return ResponseEntity.ok().body(createdReservation);
	}

	@Operation(summary = "Actualizar reserva", description = "Actualiza una reserva con la informacion enviada")
	@ApiResponse(responseCode = "200", description = "Reserva actualizada")
	@PutMapping("/{id}")
	public ResponseEntity<ReservationDTO> updateReservation(@PathVariable("id") int id,
			@RequestBody ReservationDTO reservation) {
		ReservationDTO updatedReservation = reservationService.updateResevation(reservation, id);
		return ResponseEntity.ok().body(updatedReservation);
	}

	@Operation(summary = "Eliminar reserva", description = "Elimina una reserva por su ID")
	@ApiResponse(responseCode = "204", description = "Reserva eliminada")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable("id") int id) {
		reservationService.deleteReservation(id);
		return ResponseEntity.noContent().build();
	}
}