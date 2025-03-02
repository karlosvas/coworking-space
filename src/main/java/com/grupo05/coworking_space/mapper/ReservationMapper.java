package com.grupo05.coworking_space.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.User;

/**
 * Clase encargada de convertir entre entidades de Reservation y sus correspondientes DTOs.
 * Este componente facilita la transformación bidireccional entre los objetos de dominio
 * Reservation y los objetos de transferencia de datos ReservationDTO, permitiendo
 * separar la capa de persistencia de la capa de presentación.
 */
@Component
public class ReservationMapper {

    private UserMapper userMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param userMapper Componente para mapear entidades User
     */
    public ReservationMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Convierte una entidad Reservation a su correspondiente objeto DTO.
     * Este método extrae los datos relevantes de la entidad y los encapsula
     * en un objeto DTO para su transferencia a la capa de presentación.
     * 
     * @param reservation Entidad de reserva a convertir
     * @return Objeto DTO con los datos de la reserva
     */
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

     /**
     * Convierte un objeto DTO a su correspondiente entidad Reservation.
     * Este método crea una nueva entidad Reservation basada en los datos
     * proporcionados por el DTO. También resuelve las referencias a entidades
     * relacionadas como User.
     * 
     * @param reservationDTO DTO de reserva a convertir
     * @return Entidad Reservation con los datos del DTO
     */
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
