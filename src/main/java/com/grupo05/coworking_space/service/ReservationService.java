package com.grupo05.coworking_space.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.grupo05.coworking_space.model.User;
import com.grupo05.coworking_space.dto.RequestReservationDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.enums.ApiSuccess;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.mapper.ReservationMapper;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.ReservationRepository;
import com.grupo05.coworking_space.repository.RoomRepository;
import com.grupo05.coworking_space.utils.DataResponse;
import com.grupo05.coworking_space.utils.ResponseHandler;
import com.grupo05.coworking_space.enums.RoomStatus;
import org.springframework.security.core.userdetails.UserDetails;

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
    private UserDetailsServiceImpl userDatailsServiiceImpl;
    private RoomRepository roomRepository;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param reservationRepository Repositorio para operaciones de reservas
     * @param reservationMapper Mapper para conversión entre entidades y DTOs de reservas
     * @param roomMapper Mapper para manejar salas relacionadas con reservas
     */
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            RoomMapper roomMapper, UserDetailsServiceImpl userDatailsServiiceImpl,RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.roomMapper = roomMapper;
        this.userDatailsServiiceImpl = userDatailsServiiceImpl;
        this.roomRepository=roomRepository;
    }

     /**
     * Crea una nueva reserva en el sistema.
     * Este método garantiza que toda la operación se completa correctamente 
     * o no se realiza ningún cambio (transaccionalidad).
     * Verifica la disponibilidad de las salas y actualiza sus estados.
     *
     * @param requestReservationDTO DTO con los datos de la reserva a crear
     * @return DTO con los datos de la reserva creada, incluyendo su ID
     * @throws RequestException Si hay un error de validación o las salas no están disponibles
     * @throws RuntimeException Si ocurre cualquier otro error durante el proceso
     * @see Transactional para garantizar la atomicidad de la operación
     */

    @Transactional
    public ReservationDTO createReservation(RequestReservationDTO requestReservationDTO) {
        try {
            if (requestReservationDTO == null || requestReservationDTO.getReservationDTO() == null) {
                throw new RequestException(ApiError.BAD_REQUEST);
            }

            if (!verificationSameUser(requestReservationDTO.getReservationDTO().getUserFK())) {
                throw new RequestException(ApiError.AUTHENTICATION_FAILED, "Error de permisos",
                        "No puedes crear reservas para otros usuarios");
            }

            ReservationDTO reservationDTO = requestReservationDTO.getReservationDTO();
            List<ReservationDTO> dates = this.findReservationsBetweenDates(reservationDTO.getDateInit(), reservationDTO.getDateEnd());

            if (!dates.isEmpty()) {
                throw new RequestException(ApiError.DATE_NOT_AVAILABLE);
            }

            List<Integer> roomsFK = reservationDTO.getRoomsFK();
            List<Room> rooms = roomMapper.getForeignKeys(roomsFK, null);

            if (rooms.isEmpty()) {
                throw new RequestException(ApiError.ROOM_NOT_AVAILABLE, "Room Not Available",
                        "No rooms found for the given IDs");
            }

            // Verificar disponibilidad antes de crear la reserva
            for (Room room : rooms) {
                String status = room.getRoomStatus().getState().toUpperCase();
                if (status.equals("BUSY") || status.equals("MAINTENANCE") || status.equals("NOT AVAILABLE")) {
                    throw new RequestException(ApiError.ROOM_NOT_AVAILABLE, "Room Not Available",
                            "Room is not available for reservation because it is " + status);
                }
            }

            // Convertir y guardar la reserva, ya que sabemos que todo es válido
            Reservation reservation = reservationMapper.convertToEntity(reservationDTO);
            Reservation savedReservation = reservationRepository.save(reservation);

            // Asociar habitaciones a la reserva
            for (Room room : rooms) {
                if (room.getReservations() == null) {
                    room.setReservations(new ArrayList<>());
                }
                room.getReservations().add(savedReservation);
                roomRepository.save(room);
            }

            savedReservation.setRooms(rooms);
            savedReservation = reservationRepository.save(savedReservation);

            log.info("Reserva creada: {}", savedReservation.getId());
            return reservationMapper.convertToDTO(savedReservation);
        } catch (RequestException e) {
            log.error("Error en la reserva: ", e);
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado en la creación de la reserva", e);
            throw new RuntimeException("Error en la creación de la reserva: " + e.getMessage(), e);
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
     * Obtiene todas las reservas existentes en el sistema, que pertenezcan al usuario logueado.
     *
     * @return Lista de DTOs con los datos de todas las reservas
     * @throws RuntimeException Si ocurre algún error durante la búsqueda
     */
    public List<ReservationDTO> findAllReservationsFiltered() {
        try {
            // Obtenemos todas las reservas, las convertimos a DTO y las retornamos
            List<Reservation> reservations = reservationRepository.findAll();
            List<Reservation> filterReservations= new ArrayList<>();
            for (Reservation reservation : reservations) {
                int id = reservation.getUserFK();
                if(verificationSameUser(id)){
                    filterReservations.add(reservation);
                }   
            }

            // Verificar que solo se muestren las reservas del usuario que hizo la consulta
            log.info("Se han encontrado {} reservas" + filterReservations.size());
            return filterReservations
                    .stream()
                    .map(reservationMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las reservas: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las reservas existentes en el sistema.
     * 
     * @param id
     * @return
     */
    public List<ReservationDTO>  findAllReservations(@PathVariable("id") int id) {
        try{
            // Obtenemos todas las reservas
            List<ReservationDTO> allReservations = reservationRepository.findAll().stream()
            .map(reservationMapper::convertToDTO)
            .collect(Collectors.toList());
            
            // Creamos una nueva lista para almacenar las reservas del usuario
            List<ReservationDTO> newAllReservation = new ArrayList<>();
            
            // Comprobamos si la reserva pertenece al usuario, si es asi la añadimos a la lista
            for (ReservationDTO reservationDTO : allReservations) 
            if (reservationDTO.getUserFK() == id) 
            newAllReservation.add(reservationDTO);
            
            return newAllReservation;
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
    public ReservationDTO updateResevation(ReservationDTO reservationDTO) {
        try {
            // Comprobamos si los datos de la reserva son invalidos
            if (reservationDTO == null)
                throw new RequestException(ApiError.BAD_REQUEST);

            // Obtenemos la reserva por su ID, lo convertimos a dto, no comprabamos que exista porque ya lo controla
            // el metodo findReservationByID por ello se controla en el catch
            ReservationDTO optionalReservation = this.findReservationByID(reservationDTO.getId());

            // Obtenemos el DTO de la reserva que se quiere actualizar
            Reservation updateReservation = reservationMapper.convertToEntity(optionalReservation);

            // Actualizamos los datos de la reserva
            updateReservation.setId(reservationDTO.getId());
            updateReservation.setDateInit(reservationDTO.getDateInit());
            updateReservation.setDateEnd(reservationDTO.getDateEnd());
            updateReservation.setReserveStatus(reservationDTO.getReserveStatus());
            updateReservation.setDescription(reservationDTO.getDescription());
            // Convertimos el DTO de la reserva a una entidad para pdoer obtener el user asociado
            updateReservation.setUser(reservationMapper.convertToEntity(reservationDTO).getUser());
            
            // Obtenemos las habitaciones asociadas a la reserva y las guardamos en la reserva
            List<Room> rooms = roomMapper.getForeignKeys(reservationDTO.getRoomsFK(), updateReservation);

            if(rooms.isEmpty())
                throw new RequestException(ApiError.CONFLICT, "Room Not Found",
                            "Reservation is not updated, because dont found the room");

            updateReservation.setRooms(rooms);
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
            ReservationDTO reservationDTO = this.findReservationByID(id);
            reservationRepository.deleteById(reservationDTO.getId());
            log.info("Reserva eliminada: {}", reservationDTO.getId());
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

    /**
     * Actualiza los datos de un usuario en el sistema.
     * 
     * @param id id del usuario que se dessea eliminar las reservas
     * @return DTO con los datos del usuario actualizado
     * @throws RequestException Si no se encuentra el usuario con el ID proporcionado
     * @throws RuntimeException Si ocurre otro tipo de error durante la actualización
     */
    public void deleteAllReservationsByUser(int id) {
        try {
           List<Reservation> reservations = reservationRepository.findAll();

            for (Reservation actualReservation : reservations) {
                if(actualReservation.getUserFK() == id){
                    this.deleteReservation(actualReservation.getId());
                    log.info("Reserva eliminada: {}, de el usurio {}", actualReservation.getId(), actualReservation.getUserFK());
                }
            }

            log.info("Reservas eliminadas con exito del usuario: {}", id);
        } catch (DataAccessException ex) {
            throw new RequestException(ApiError.DATABASE_ERROR);
        } catch (Exception ex) {
            throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verifica si el usuario que esta logueado es el mismo que el que se esta buscando
     * 
     * @param id
     * @return true si es el mismo usuario, false si no lo es
     */
    public boolean verificationSameUser(int id){
            UserDTO userDTO = userDatailsServiiceImpl.findUserById(id);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!userDTO.getUsername().equals(authentication.getName())){
                return false;
            }
            return true;
    }
}