package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.annotations.SwaggerApiResponses;
import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.service.RoomService;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;
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

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de salas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
			@ApiResponse(responseCode = "204", description = "No hay ninguna sala", content = @Content)
	})
	@GetMapping
	public ResponseEntity<DataResponse> findAllRooms() {
		List<RoomDTO> allRooms = roomService.findAllRooms();

		if (allRooms.isEmpty())
			return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_NO_CONTENT, allRooms);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, allRooms);
	}

	@Operation(summary = "Obtener sala por id", description = "Devuelve una sala por su ID")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sala encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@GetMapping("/{id}")
	public ResponseEntity<DataResponse> findRoomById(@PathVariable("id") int id) {
		RoomDTO foundRoom = roomService.findRoomById(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_RETRIEVED, foundRoom);
	}

	@Operation(summary = "Crear sala", description = "Crea una nueva sala con la informacion enviada")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sala creada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@PostMapping
	public ResponseEntity<DataResponse> createRoom(@RequestBody RoomDTO roomDTO) {
		RoomDTO createdRoom = roomService.createRoom(roomDTO);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_CREATED, createdRoom);
	}

	@Operation(summary = "Actualizar sala", description = "Actualiza una sala con la informacion enviada")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sala actualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class))),
	})
	@PutMapping("/{id}")
	public ResponseEntity<DataResponse> updateRoom(
			@PathVariable("id") int id,
			@RequestBody RoomDTO room) {
		RoomDTO updatedRoom = roomService.updateRoom(id, room);
		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_UPDATED, updatedRoom);
	}

	@Operation(summary = "Eliminar sala", description = "Elimina una sala por su ID")
	@SwaggerApiResponses
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Sala eliminada", content = @Content)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<DataResponse> deleteRoom(@PathVariable("id") int id) {
		roomService.deleteRoom(id);

		return ResponseHandler.handleApiResponse(ApiSuccess.RESOURCE_REMOVED, null);
	}
}
