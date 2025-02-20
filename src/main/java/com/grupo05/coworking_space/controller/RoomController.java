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

		ResponseEntity<DataResponse> response = ResponseHandler.handleApiResponse(
			ApiSuccess.RESOURCE_RETRIEVED,
			allRooms
		);

		return new ResponseEntity<>(
			response.getBody(),
			ApiSuccess.RESOURCE_RETRIEVED.getStatus()
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findRoomById(@PathVariable("id") String id) {
		RoomDTO foundRoom = roomService.findRoomById(Integer.parseInt(id));

		ResponseEntity<DataResponse> response = ResponseHandler.handleApiResponse(
			ApiSuccess.RESOURCE_RETRIEVED,
			foundRoom
		);

		return new ResponseEntity<>(
			response.getBody(),
			ApiSuccess.RESOURCE_RETRIEVED.getStatus()
		);
	}

	@PostMapping
	public ResponseEntity<DataResponse> createRoom(@RequestBody RoomDTO room) {
		RoomDTO createdRoom = roomService.createRoom(room);

		ResponseEntity<DataResponse> response = ResponseHandler.handleApiResponse(
			ApiSuccess.RESOURCE_CREATED,
			createdRoom
		);

		return new ResponseEntity<>(
			response.getBody(),
			ApiSuccess.RESOURCE_CREATED.getStatus()
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateRoom(
		@PathVariable("id") String id,
		@RequestBody RoomDTO room
	)
	{
		RoomDTO updatedRoom = roomService.updateRoom(Integer.parseInt(id), room);

		ResponseEntity<DataResponse> response = ResponseHandler.handleApiResponse(
			ApiSuccess.RESOURCE_UPDATED,
			updatedRoom
		);

		return new ResponseEntity<>(
			response.getBody(),
			ApiSuccess.RESOURCE_UPDATED.getStatus()
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") String id) {
		roomService.deleteRoom(Integer.parseInt(id));

		ResponseEntity<DataResponse> response = ResponseHandler.handleApiResponse(
			ApiSuccess.RESOURCE_REMOVED,
			null
		);

		return new ResponseEntity<>(
			response.getBody(),
			ApiSuccess.RESOURCE_REMOVED.getStatus()
		);
	}
}
