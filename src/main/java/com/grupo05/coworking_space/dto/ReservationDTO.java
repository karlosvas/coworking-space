package com.grupo05.coworking_space.dto;

import com.grupo05.coworking_space.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object para Reservas
 * @Schema Permite configurar la documentación de Swagger para este DTO
 * @Getter, @Setter y @AllArgsConstructor son anotaciones de Lombok para generar automáticamente los getters, setters y constructores con todos los argumentos
 * Para más infromacion sobre el dto, ver la documentacion de Swagger en: localhost:8080/api/swagger-ui.html
 */
@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object para Reservas")
public class ReservationDTO implements Serializable {
    @Schema(description = "Identificador único para reservas", type = "integer", required = false, hidden = true)
    private final int id;

	@Schema(description = "Fecha y hora de inicio de la reserva", example = "2025-02-21T10:00:00", format = "date-time", required = true)
	@NotNull(message = "{field.null}")
	@FutureOrPresent(message = "{reservation.date}")
	private LocalDateTime dateInit;

	@Schema(description = "Fecha y hora de fin de la reserva", example = "2025-02-21T12:00:00", format = "date-time", required = true)
	@NotNull(message = "{field.null}")
	@FutureOrPresent(message = "{reservation.date}")
	private LocalDateTime dateEnd;

    @Schema(description = "Estado de la reserva del enum ReservationStatus", allowableValues = {
            "PENDING", "CONFIRMED", "CANCELED", "COMPLETED",
            "Pending", "Confirmed", "Canceled", "Completed" }, type = "string", required = true)
    @NotNull(message = "{field.null}")
    private ReservationStatus reserveStatus;

    @Schema(description = "Descripcion de la reserva", example = "Reserva de sala de reuniones para presentacion de proyecto", type = "string", required = false)
    @Size(max = 255, message = "{reservation.max.description}")
    private String description;

    @Schema(description = "Identificador único para usuarios FK", type = "integer", required = true)
    @NotNull(message = "{field.null}")
    private int userFK;

    @Schema(description = "Identificador único para salas FK", type = "array", required = true)
    @NotEmpty(message = "{field.empty}")
    private List<Integer> roomsFK;
}