package com.grupo05.coworking_space.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.grupo05.coworking_space.enums.ReservationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object para Reservas")
public class ReservationDTO implements Serializable {
    @Schema(description = "Identificador único para reservas", type = "integer", required = false, hidden = true)
    private final int id;

    @Schema(description = "Fecha y hora de inicio de la reserva", example = "2025-02-21T10:00:00", format = "date-time", required = true)
    private Date dateInit;

    @Schema(description = "Fecha y hora de fin de la reserva", example = "2025-02-21T12:00:00", format = "date-time", required = true)
    private Date dateEnd;

    @Schema(description = "Estado de la reserva del enum ReservationStatus", allowableValues = {
            "PENDING", "CONFIRMED", "CANCELED", "COMPLETED",
            "Pending", "Confirmed", "Canceled", "Completed" }, type = "string", required = true)
    private ReservationStatus reserveStatus;

    @Schema(description = "Descripcion de la reserva", example = "Reserva de sala de reuniones para presentacion de proyecto", type = "string", required = false)
    private String description;

    @Schema(description = "Identificador único para usuarios FK", type = "integer", required = true)
    private int userFK;

    @Schema(description = "Identificador único para salas FK", type = "array", required = true)
    private List<Integer> roomsFK;
}