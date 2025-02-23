package com.grupo05.coworking_space.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.model.User;

@Component
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
        List<Room> rooms = roomMapper.getForeignKeys(reservationDTO.getRoomsFK());

        return new Reservation(
                reservationDTO.getId(),
                reservationDTO.getDateInit(),
                reservationDTO.getDateEnd(),
                reservationDTO.getReserveStatus(),
                reservationDTO.getDescription(),
                user,
                rooms);
    }
}
