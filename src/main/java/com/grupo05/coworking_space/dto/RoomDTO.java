package com.grupo05.coworking_space.dto;

import java.io.Serializable;
import com.grupo05.coworking_space.enums.RoomStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objeto de Transferencia de Datos (DTO) para representar y transportar información de salas.
 *
 * Implementa Serializable para permitir la serialización de objetos en transmisiones de red.
 * Incluye anotaciones de Lombok para generación automática de métodos y builder.
 * Para más infromacion sobre el dto, ver la documentacion de Swagger en: localhost:8080/api/swagger-ui.html
 * @Schema Permite configurar la documentación de Swagger para este DTO.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, max = 20, message = "{room.name.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message="{room.name.pattern}")
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
	@NotNull(message = "{field.null}")
	private RoomStatus roomStatus;

	@Schema(description = "Capacidad de la sala", example = "10", type = "integer", required = true)
	@NotNull(message = "{field.null}")
	@Min(value = 10, message = "{room.min.capacity}")
	private int capacity;
}
