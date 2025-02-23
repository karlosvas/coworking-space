package com.grupo05.coworking_space.mapper;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.model.Reservation;

@Component
public class ReservationMapper {
    public ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getDateInit(),
                reservation.getDateEnd(),
                reservation.getReserveStatus(),
                reservation.getDescription());
    }

    public Reservation convertToEntity(ReservationDTO reservationDTO) {
        return new Reservation(
                reservationDTO.getDateInit(),
                reservationDTO.getDateEnd(),
                reservationDTO.getReserveStatus(),
                reservationDTO.getDescription());
    }
}
