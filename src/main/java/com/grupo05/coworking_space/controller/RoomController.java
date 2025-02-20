package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.service.RoomService;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService service) {
		this.roomService = service;
	}

	@GetMapping
	public ResponseEntity<DataResponse> findAllRooms() {
		List<RoomDTO> allRooms = roomService.findAllRooms();

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allRooms);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findRoomById(@PathVariable("id") int id) {
		RoomDTO foundRoom = roomService.findRoomById(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundRoom);
	}

	@PostMapping
	public ResponseEntity<DataResponse> createRoom(@RequestBody RoomDTO room) {
		RoomDTO createdRoom = roomService.createRoom(room);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_CREATED, createdRoom);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateRoom(
		@PathVariable("id") int id,
		@RequestBody RoomDTO room
	)
	{
		RoomDTO updatedRoom = roomService.updateRoom(id, room);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedRoom);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
		roomService.deleteRoom(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}
}
