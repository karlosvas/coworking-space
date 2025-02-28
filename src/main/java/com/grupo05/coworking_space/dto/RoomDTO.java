package com.grupo05.coworking_space.dto;

import java.io.Serializable;

import com.grupo05.coworking_space.enums.RoomStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para representar y transportar información de salas.
 * 
 * Implementa Serializable para permitir la serialización de objetos en transmisiones de red.
 * Incluye anotaciones de Lombok para generación automática de métodos y builder.
 *
 * @Schema Permite configurar la documentación de Swagger para este DTO.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object para Salas")
public class RoomDTO implements Serializable {
	@Schema(
		description = "Identificador único para sala",
		type = "integer",
		required = false,
		hidden = true
	)
	private int id;

	@Schema(
		description = "Nombre de sala",
		example = "Sala de reuniones",
		type = "string",
		required = true
	)
	private String name;

	@Schema(
		description = "Estado de la sala enum RoomStatus", allowableValues = {
		"AVAILABLE",
		"BUSY",
		"MAINTENANCE",
		"NOT_AVAILABLE",
		"Available",
		"Busy",
		"Maintenance",
		"Not Available"
	}, type = "string", required = true
	)
	private RoomStatus roomStatus;

	@Schema(description = "Capacidad de la sala", example = "10", type = "integer", required = true)
	private int capacity;
}
