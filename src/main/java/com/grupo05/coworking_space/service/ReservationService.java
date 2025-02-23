package com.grupo05.coworking_space.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.ReservationRepository;

import lombok.extern.slf4j.Slf4j;

import com.grupo05.coworking_space.mapper.ReservationMapper;

@Service
@Slf4j
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        try {
            if (reservationDTO == null) {
                throw new RequestException(ApiError.BAD_REQUEST);
            }
            Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
            // Foren keys bidireccionales
            // for (Room room : reservation.getRoom()) {
            // room.setReservation(reservation);
            // }

            log.info("Reserva creada: {}" + reservation);
            Reservation savedReservation = reservationRepository.save(reservation);
            return reservationMapper.convertToDTO(savedReservation);
        } catch (RequestException e) {
            // Puede ahver un error en la validación de los datos en convertToEntity
            // por eso se captura la excepción y se lanza nuevamente
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error en la creación de la reserva: " + e.getMessage());
        }
    }

    public ReservationDTO findReservationByID(int id) {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);

            if (!reservation.isPresent()) {
                throw new RequestException(ApiError.RECORD_NOT_FOUND);
            }

            return reservationMapper.convertToDTO(reservation.get());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la reserva: " + e.getMessage());
        }
    }

    public List<ReservationDTO> findAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            return reservations.stream()
                    .map(reservationMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las reservas: " + e.getMessage());
        }
    }

    public ReservationDTO updateResevation(ReservationDTO reservationDTO, int id) {
        try {
            if (reservationDTO == null)
                throw new RequestException(ApiError.BAD_REQUEST);

            ReservationDTO optionalReservation = this.findReservationByID(id);
            Reservation updateReservation = reservationMapper.convertToEntity(optionalReservation);
            updateReservation.setId(id);
            updateReservation.setDateInit(reservationDTO.getDateInit());
            updateReservation.setDateEnd(reservationDTO.getDateEnd());
            updateReservation.setReserveStatus(reservationDTO.getReserveStatus());
            updateReservation.setDescription(reservationDTO.getDescription());
            Reservation savedReservation = reservationRepository.save(updateReservation);
            return reservationMapper.convertToDTO(savedReservation);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la reserva: " + e.getMessage());
        }
    }

    public void deleteReservation(int id) {
        try {
            ReservationDTO reservation = this.findReservationByID(id);
            reservationRepository.deleteById(reservation.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva: " + e.getMessage());
        }
    }

}