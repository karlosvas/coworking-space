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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Room", description = "Endpoints para gestionar las salas")
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService service) {
		this.roomService = service;
	}

	@Operation(summary = "Obtener todas las salas", description = "Devuelve una lista con todas las salas")
	@ApiResponse(responseCode = "200", description = "Lista de salas")
	@GetMapping
	public ResponseEntity<DataResponse> findAllRooms() {
		List<RoomDTO> allRooms = roomService.findAllRooms();

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allRooms);
	}

	@Operation(summary = "Obtener sala por id", description = "Devuelve una sala por su ID")
	@ApiResponse(responseCode = "200", description = "Sala encontrada")
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findRoomById(@PathVariable("id") int id) {
		RoomDTO foundRoom = roomService.findRoomById(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundRoom);
	}

	@Operation(summary = "Crear sala", description = "Crea una nueva sala con la informacion enviada")
	@ApiResponse(responseCode = "200", description = "Sala creada")
	@PostMapping
	public ResponseEntity<DataResponse> createRoom(@RequestBody RoomDTO room) {
		RoomDTO createdRoom = roomService.createRoom(room);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_CREATED, createdRoom);
	}

	@Operation(summary = "Actualizar sala", description = "Actualiza una sala con la informacion enviada")
	@ApiResponse(responseCode = "200", description = "Sala actualizada")
	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateRoom(
		@PathVariable("id") int id,
		@RequestBody RoomDTO room
	)
	{
		RoomDTO updatedRoom = roomService.updateRoom(id, room);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedRoom);
	}

	@Operation(summary = "Eliminar sala", description = "Elimina una sala por su ID")
	@ApiResponse(responseCode = "204", description = "Sala eliminada")
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
		roomService.deleteRoom(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}
}
