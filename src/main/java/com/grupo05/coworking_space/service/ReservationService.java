package com.grupo05.coworking_space.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.mapper.ReservationMapper;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.ReservationRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.grupo05.coworking_space.mapper.RoomMapper;

@Service
@Slf4j
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private RoomMapper roomMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            RoomMapper roomMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        try {
            if (reservationDTO == null) {
                throw new RequestException(ApiError.BAD_REQUEST);
            }
            Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
            Reservation savedReservation = reservationRepository.save(reservation);

            List<Room> rooms = roomMapper.getForeignKeys(reservationDTO.getRoomsFK(), savedReservation);
            for (Room room : rooms) {
                String status = room.getRoomStatus().toString().toUpperCase();
                if (status == "BUSY" || status == "MANTENIENCE" || status == "NOT AVIABLE")
                    throw new RequestException(ApiError.ROOM_NOT_AVAILABLE, "Room Not Available",
                            "Room is not available for reservation, because it is" + status);
                room.setReservation(reservation);
            }
            savedReservation.setRoom(rooms);

            log.info("Reserva creada: {}" + savedReservation.getId());
            reservationRepository.save(savedReservation);

            return reservationMapper.convertToDTO(savedReservation);
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error en la creación de la reserva: " + e.getMessage());
        }
    }

    public ReservationDTO findReservationByID(int id) {
        try {
            Optional<Reservation> reservation = reservationRepository.findById(id);

            if (reservation.isEmpty()) {
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
            return reservations
                    .stream()
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

    public List<ReservationDTO> findReservationsBetweenDates(LocalDateTime dateInit, LocalDateTime dateEnd) {
        try {
            List<Reservation> reservations = reservationRepository.findReservationsBetweenDates(dateInit, dateEnd);
            return reservations
                    .stream()
                    .map(reservationMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new RequestException(ApiError.DATABASE_ERROR);
        } catch (Exception ex) {
            throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
        }
    }
}