package com.grupo05.coworking_space.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import com.grupo05.coworking_space.dto.RequestReservationDTO;
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


/**
 * Servicio para la gestión de reservas de salas.
 * Este servicio proporciona operaciones CRUD para reservas, incluyendo
 * la creación, consulta, actualización y eliminación de reservas, así como
 * búsquedas especializadas como filtrado por rango de fechas.
 *
 * @Service para indicar que es un servicio de la aplicacion
 * @Slf4j Logs de la aplicacion
 */
@Slf4j
@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private RoomMapper roomMapper;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param reservationRepository Repositorio para operaciones de reservas
     * @param reservationMapper Mapper para conversión entre entidades y DTOs de reservas
     * @param roomMapper Mapper para manejar salas relacionadas con reservas
     */
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            RoomMapper roomMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.roomMapper = roomMapper;
    }

     /**
     * Crea una nueva reserva en el sistema.
     * <p>
     * Este método garantiza que toda la operación se completa correctamente
     * o no se realiza ningún cambio (transaccionalidad).
     * Verifica la disponibilidad de las salas y actualiza sus estados.
     *
     * @param requestReservationDTO DTO con los datos de la reserva a crear
     * @return DTO con los datos de la reserva creada, incluyendo su ID
     * @throws RequestException Si hay un error de validación o las salas no están disponibles
     * @throws RuntimeException Si ocurre cualquier otro error durante el proceso
     */

        public ReservationDTO createReservation(RequestReservationDTO requestReservationDTO) {
        try {
            // Comprobamos si los datos de la reserva son invalidos
            if (requestReservationDTO == null || requestReservationDTO.getReservationDTO() == null) {
                throw new RequestException(ApiError.BAD_REQUEST);
            }

            ReservationDTO reservationDTO = requestReservationDTO.getReservationDTO();
            List<ReservationDTO> dates =this.findReservationsBetweenDates(reservationDTO.getDateInit(),
                    reservationDTO.getDateEnd());
            if (!dates.isEmpty())
                throw new RequestException(ApiError.DATE_NOT_AVAILABLE);

            List<String> emailsParticipants = requestReservationDTO.getEmailsParticipants();

            if (reservationDTO == null) {
                throw new RequestException(ApiError.BAD_REQUEST);
            }
            // Convertimos el DTO a una entidad y la guardamos en la base de datos
            Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
            Reservation savedReservation = reservationRepository.save(reservation);

            // Obtenemos todas las FK de las habitaciones y las guardamos en la reserva
            List<Room> rooms = roomMapper.getForeignKeys(reservationDTO.getRoomsFK(), savedReservation);
            for (Room room : rooms) {
                // Comprobamos si la habitacion esta disponible y almacenamos la referencia de la reserva en cada habitacion
                String status = room.getRoomStatus().toString().toUpperCase();
                if (status.equals("BUSY")  || status.equals("MAINTENANCE") || status.equals("NOT AVAILABLE"))
                    throw new RequestException(ApiError.ROOM_NOT_AVAILABLE, "Room Not Available",
                            "Room is not available for reservation, because it is" + status);
                room.setReservation(savedReservation);
            }
            // Actuaalizamos las habitaciones en la reserva, y guardamos la reserva
            savedReservation.setRoom(rooms);

            savedReservation=reservationRepository.save(savedReservation);
            log.info("Reserva creada: {}" , savedReservation.getId());

            return reservationMapper.convertToDTO(savedReservation);
        } catch (RequestException error) {
            log.error("Error ", error);
            throw error;
        } catch (Exception e) {
            throw new RuntimeException("Error en la creación de la reserva: " + e.getMessage());
        }
    }

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id ID de la reserva a buscar
     * @return DTO con los datos de la reserva encontrada
     * @throws RequestException Si no se encuentra la reserva
     * @throws RuntimeException Si ocurre cualquier otro error durante la búsqueda
     */
    public ReservationDTO findReservationByID(int id) {
        try {
            // Obtenemos la reserva por su ID, lo convertimos a dto y comprobamos que exista
            Optional<Reservation> reservation = reservationRepository.findById(id);

            // Comprobamos si los datos de la reserva son invalidos
            if (reservation.isEmpty())
                throw new RequestException(ApiError.RECORD_NOT_FOUND);

            // Retornamos la reserva en formato DTO
            log.info("Reserva encontrada: {}" + reservation.get().getId());
            return reservationMapper.convertToDTO(reservation.get());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la reserva: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las reservas existentes en el sistema.
     *
     * @return Lista de DTOs con los datos de todas las reservas
     * @throws RuntimeException Si ocurre algún error durante la búsqueda
     */
    public List<ReservationDTO> findAllReservations() {
        try {
            // Obtenemos todas las reservas, las convertimos a DTO y las retornamos
            List<Reservation> reservations = reservationRepository.findAll();
            log.info("Se han encontrado {} reservas" + reservations.size());
            return reservations
                    .stream()
                    .map(reservationMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las reservas: " + e.getMessage());
        }
    }

     /**
     * Actualiza los datos de una reserva existente.
     *
     * @param reservationDTO DTO con los nuevos datos de la reserva
     * @param id ID de la reserva a actualizar
     * @return DTO con los datos actualizados de la reserva
     * @throws RequestException Si la reserva no existe o hay un error de validación
     * @throws RuntimeException Si ocurre cualquier otro error durante la actualización
     */
    public ReservationDTO updateResevation(ReservationDTO reservationDTO, int id) {
        try {
            // Comprobamos si los datos de la reserva son invalidos
            if (reservationDTO == null)
                throw new RequestException(ApiError.BAD_REQUEST);

            // Obtenemos la reserva por su ID, lo convertimos a dto, no comprabamos que exista porque ya lo controla
            // el metodo findReservationByID por ello se controla en el catch
            ReservationDTO optionalReservation = this.findReservationByID(id);
            Reservation updateReservation = reservationMapper.convertToEntity(optionalReservation);
            updateReservation.setId(id);
            updateReservation.setDateInit(reservationDTO.getDateInit());
            updateReservation.setDateEnd(reservationDTO.getDateEnd());
            updateReservation.setReserveStatus(reservationDTO.getReserveStatus());
            updateReservation.setDescription(reservationDTO.getDescription());
            // Lo guardamos en la base de datos
            Reservation savedReservation = reservationRepository.save(updateReservation);

            log.info("Reserva actualizada: {}" + savedReservation.getId());
            return reservationMapper.convertToDTO(savedReservation);
        }catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la reserva: " + e.getMessage());
        }
    }

     /**
     * Elimina una reserva del sistema por su ID.
     *
     * @param id ID de la reserva a eliminar
     * @throws RequestException Si la reserva no existe
     * @throws RuntimeException Si ocurre cualquier otro error durante la eliminación
     */
    public void deleteReservation(int id) {
        try {
             // Obtenemos la reserva por su ID, lo convertimos a dto, no comprabamos que exista porque ya lo controla
            // el catch, eliminamos la reserva encontrada en la base de datos
            ReservationDTO reservation = this.findReservationByID(id);
            reservationRepository.deleteById(reservation.getId());
            log.info("Reserva eliminada: {}" + reservation.getId());
        }catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva: " + e.getMessage());
        }
    }

     /**
     * Busca reservas dentro de un rango de fechas específico.
     *
     * @param dateInit Fecha inicial del rango de búsqueda
     * @param dateEnd Fecha final del rango de búsqueda
     * @return Lista de DTOs con las reservas encontradas dentro del rango de fechas
     * @throws RequestException Si hay un error de acceso a datos o un error interno
     */
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