package com.grupo05.coworking_space.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.model.User;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReservationMapper {
    private UserMapper userMapper;
    private RoomMapper roomMapper;

    public ReservationMapper(UserMapper userMapper, RoomMapper roomMapper) {
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    public ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getDateInit(),
                reservation.getDateEnd(),
                reservation.getReserveStatus(),
                reservation.getDescription(),
                reservation.getUserFK(),
                reservation.getRoomsFK());
    }

    public Reservation convertToEntity(ReservationDTO reservationDTO) {
        User user = userMapper.getForeignKey(reservationDTO.getUserFK());

        Reservation reservation = new Reservation();
        reservation.setDateInit(reservationDTO.getDateInit());
        reservation.setDateEnd(reservationDTO.getDateEnd());
        reservation.setReserveStatus(reservationDTO.getReserveStatus());
        reservation.setDescription(reservationDTO.getDescription());
        reservation.setUser(user);

        return reservation;
    }
}
