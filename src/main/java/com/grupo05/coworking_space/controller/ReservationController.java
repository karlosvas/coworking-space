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
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.service.ReservationService;

@RestController
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {
	private final ReservationService reservationService;

	public ReservationController(ReservationService service) {
		this.reservationService = service;
	}

	@GetMapping("/all")
	public ResponseEntity<List<ReservationDTO>> findAllReservations() {
		List<ReservationDTO> allReserves = reservationService.findAllReservations();
		return ResponseEntity.ok().body(allReserves);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservationDTO> findReservationById(@PathVariable("id") int id) {
		ReservationDTO foundReservation = reservationService.findReservationByID(id);
		return ResponseEntity.ok().body(foundReservation);
	}

	@PostMapping
	public ResponseEntity<ReservationDTO> createReservation(@RequestBody Reservation reservation) {
		ReservationDTO createdReservation = reservationService.createReservation(reservation);
		return ResponseEntity.ok().body(createdReservation);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReservationDTO> updateReservation(@PathVariable("id") int id,
			@RequestBody Reservation reservation) {
		ReservationDTO updatedReservation = reservationService.updateResevation(reservation, id);
		return ResponseEntity.ok().body(updatedReservation);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable("id") int id) {
		reservationService.deleteReservation(id);
		return ResponseEntity.noContent().build();
	}
}