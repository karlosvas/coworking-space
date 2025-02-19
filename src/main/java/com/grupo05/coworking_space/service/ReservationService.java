package com.grupo05.coworking_space.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.ReservationDTO;
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
        Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
        Reservation newReserve = reservationRepository.save(reservation);
        return reservationMapper.convertToDTO(newReserve);
    }

    public ReservationDTO findReservationByID(int id) {
        Reservation reservation = reservationRepository.findById(id).get();
        return reservationMapper.convertToDTO(reservation);
    }

    public List<ReservationDTO> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservationMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReservationDTO updateResevation(ReservationDTO reservationDTO, int id) {
        Reservation updateReservation = reservationRepository.findById(id).get();
        updateReservation.setDateInit(reservationDTO.getDateInit());
        updateReservation.setDateEnd(reservationDTO.getDateEnd());
        updateReservation.setReserveStatus(reservationDTO.getReserveStatus());
        updateReservation.setDescription(reservationDTO.getDescription());

        Reservation savedReservation = reservationRepository.save(updateReservation);
        return reservationMapper.convertToDTO(savedReservation);
    }

    public void deleteReservation(int id) {
        reservationRepository.deleteById(id);
    }

}