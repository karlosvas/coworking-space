package com.grupo05.coworking_space.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationDTO createReservation(Reservation reservation) {
        Reservation newReserve = reservationRepository.save(reservation);
        ReservationDTO reservationDTO = convertToDTO(newReserve);
        return reservationDTO;
    }

    public ReservationDTO findReservationByID(int id) {
        Reservation reservation = reservationRepository.findById(id).get();
        ReservationDTO reservationDTO = convertToDTO(reservation);
        return reservationDTO;
    }

    public List<ReservationDTO> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO updateResevation(Reservation reservation, int id) {
        Reservation newReservation = reservationRepository.findById(id).get();
        newReservation.setDateInit(reservation.getDateInit());
        newReservation.setDateEnd(reservation.getDateEnd());
        newReservation.setDescription(reservation.getDescription());

        Reservation saveReserve = reservationRepository.save(reservation);
        ReservationDTO reservationDTO = convertToDTO(saveReserve);
        return reservationDTO;
    }

    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }

    public ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getDateInit(),
                reservation.getDateEnd(),
                reservation.getReserveStatus(),
                reservation.getDescription());
    }

    public Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setDateInit(reservationDTO.getDateInit());
        reservation.setDateEnd(reservationDTO.getDateEnd());
        reservation.setReserveStatus(reservationDTO.getReserveStatus());
        reservation.setDescription(reservationDTO.getDescripcion());
        return reservation;
    }
}