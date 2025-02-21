package com.grupo05.coworking_space.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.exception.ReservationNotFoundException;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.repository.ReservationRepository;
import com.grupo05.coworking_space.mapper.ReservationMapper;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        try {
            if (reservationDTO == null)
                throw new RuntimeException("Los datos de la reserva no pueden ser nulos");
            Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
            Reservation savedReservation = reservationRepository.save(reservation);
            return reservationMapper.convertToDTO(savedReservation);
        } catch (Exception e) {
            throw new RuntimeException("Error en la creación de la reserva: " + e.getMessage());
        }
    }

    public ReservationDTO findReservationByID(int id) {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);
            if (!reservation.isPresent()) {
                Throwable cause = new Throwable(String.valueOf(id));
                throw new ReservationNotFoundException("Reserva no encontrada", cause);
            }
            return reservationMapper.convertToDTO(reservation.get());
        } catch (Exception e) {

            throw new RuntimeException("Error al buscar la reserva: " + e.getMessage());
        }
    }

    // findAll nunca devuelbe null por lo que no es necesario hacer la validación,
    // si esta vacio devuelbe una lista vacia
    public List<ReservationDTO> findAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations.isEmpty())
                throw new RuntimeException("No hay reservas en la base de datos");
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
                throw new RuntimeException("Los datos de la reserva no pueden ser nulos");
            Optional<Reservation> optionalReservation = reservationRepository.findById(id);
            if (!optionalReservation.isPresent()) {
                Throwable cause = new Throwable(String.valueOf(id));
                throw new ReservationNotFoundException("Reserva no encontrada al actualizar", cause);
            }
            Reservation updateReservation = optionalReservation.get();
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
            if (!reservationRepository.existsById(id)) {
                Throwable cause = new Throwable(String.valueOf(id));
                throw new ReservationNotFoundException("Reserva no encontrada al eliminar", cause);
            }
            reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva: " + e.getMessage());
        }
    }

}