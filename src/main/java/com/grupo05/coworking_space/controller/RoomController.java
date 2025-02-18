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

import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.service.RoomService;

@RestController
@RequestMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService service) {
		this.roomService = service;
	}

	@GetMapping
	public ResponseEntity<List<Room>> findAllRooms() {
		List<Room> allRooms = roomService.findAllRooms();
		return ResponseEntity.ok().body(allRooms);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Room> findRoomById(@PathVariable("id") String id) {
		Room foundRoom = roomService.findRoomById(Integer.parseInt(id));
		return ResponseEntity.ok().body(foundRoom);
	}

	@PostMapping
	public ResponseEntity<Room> createRoom(@RequestBody Room room) {
		Room createdRoom = roomService.createRoom(room);
		return ResponseEntity.ok().body(createdRoom);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Room> updateRoom(@PathVariable("id") String id, @RequestBody Room room) {
		Room updatedRoom = roomService.updateRoom(Integer.parseInt(id), room);
		return ResponseEntity.ok().body(updatedRoom);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoom(@PathVariable("id") String id) {
		roomService.deleteRoom(Integer.parseInt(id));
		return ResponseEntity.noContent().build();
	}
}
