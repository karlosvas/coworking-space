package com.grupo05.coworking_space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Data Transfer Object para crear reservas y notificar a participantes")
public class RequestReservationDTO {

    @Schema(description = "Datos de la reserva", required = true)
    private ReservationDTO reservationDTO;

    @Schema(description = "Lista de correos de participantes", required = false)
    private List<String> emailsParticipants;
}
