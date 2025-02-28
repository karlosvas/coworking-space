package com.grupo05.coworking_space.service;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.mapper.RoomMapper;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.RoomRepository;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;

	/**
	 * Constructor principal para la inyección de dependencias.
	 *
	 * @param repository Repositorio de acceso a datos para habitaciones
	 * @param mapper     Mapper para conversión entre entidades y DTOs
	 */
	public RoomService(RoomRepository repository, RoomMapper mapper) {
		this.roomRepository = repository;
		this.roomMapper = mapper;
	}

	/**
	 * Obtiene todas las habitaciones registradas en el sistema.
	 *
	 * @return Lista de DTOs con todas las habitaciones
	 * @throws RequestException Si ocurre un error en la base de datos (DATABASE_ERROR)
	 *                          o un error interno inesperado (INTERNAL_SERVER_ERROR)
	 */
	
	public List<RoomDTO> findAllRooms() {
		try {
			
			List<Room> rooms = roomRepository.findAll();

			return rooms.stream().map(roomMapper::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException ex) {
			throw new RequestException(ApiError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una habitación por su identificador único.
	 *
	 * @param id Identificador de la habitación a buscar
	 * @return DTO con los datos de la habitación encontrada
	 * @throws RequestException Si no se encuentra la habitación (RECORD_NOT_FOUND),
	 *                          error en la base de datos (DATABASE_ERROR)
	 *                          o error interno inesperado (INTERNAL_SERVER_ERROR)
	 */
	public RoomDTO findRoomById(int id) {
		try {
			Optional<Room> optionalRoom = roomRepository.findById(id);

			if (optionalRoom.isEmpty()) {
				throw new RequestException(ApiError.RECORD_NOT_FOUND);
			}

			return roomMapper.convertToDTO(optionalRoom.get());
		} catch (RequestException ex) {
			throw ex;
		} catch (DataAccessException ex) {
			throw new RequestException(ApiError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Crea una nueva habitación en la base de datos.
	 *
	 * @param room DTO con los datos de la habitación a crear
	 * @return DTO con los datos de la habitación creada
	 * @throws RequestException Si hay conflicto de datos (CONFLICT),
	 *                          error de recursos asociados (ASSOCIATED_RESOURCES),
	 *                          error en la base de datos (DATABASE_ERROR)
	 *                          o error interno inesperado (INTERNAL_SERVER_ERROR)
	 */
	public RoomDTO createRoom(RoomDTO room) {
		try {
			Room roomToSave = roomMapper.convertToEntity(room);

			Room savedRoom = roomRepository.save(roomToSave);

			return roomMapper.convertToDTO(savedRoom);
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (rootCause instanceof ConstraintViolationException ||
				rootCause instanceof DataIntegrityViolationException) throw new RequestException(
				ApiError.CONFLICT);
			else throw new RequestException(ApiError.ASSOCIATED_RESOURCES);
		} catch (DataAccessException ex) {
			throw new RequestException(ApiError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Actualiza los datos de una habitación existente.
	 *
	 * @param id   Identificador de la habitación a actualizar
	 * @param room DTO con los nuevos datos de la habitación
	 * @return DTO con los datos actualizados de la habitación
	 * @throws RequestException Si no se encuentra la habitación (RECORD_NOT_FOUND),
	 *                          conflicto de datos (CONFLICT),
	 *                          error de recursos asociados (ASSOCIATED_RESOURCES),
	 *                          error en la base de datos (DATABASE_ERROR)
	 *                          o error interno inesperado (INTERNAL_SERVER_ERROR)
	 */
	public RoomDTO updateRoom(int id, RoomDTO room) {
		try {
			Room roomFound = roomMapper.convertToEntity(this.findRoomById(id));
			roomFound.setId(id);
			roomFound.setName(room.getName());
			roomFound.setRoomStatus(room.getRoomStatus());
			roomFound.setCapacity(room.getCapacity());

			return roomMapper.convertToDTO(roomRepository.save(roomFound));
		} catch (RequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof ConstraintViolationException ||
				rootCause instanceof DataIntegrityViolationException) throw new RequestException(
				ApiError.CONFLICT);
			else throw new RequestException(ApiError.ASSOCIATED_RESOURCES);
		} catch (DataAccessException ex) {
			throw new RequestException(ApiError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Elimina una habitación de la base de datos.
	 *
	 * @param id Identificador de la habitación a eliminar
	 * @throws RequestException Si no se encuentra la habitación (RECORD_NOT_FOUND),
	 *                          conflicto de datos (CONFLICT),
	 *                          error de recursos asociados (ASSOCIATED_RESOURCES),
	 *                          error en la base de datos (DATABASE_ERROR)
	 *                          o error interno inesperado (INTERNAL_SERVER_ERROR)
	 */
	public void deleteRoom(int id) {
		try {
			RoomDTO result = this.findRoomById(id);

			roomRepository.deleteById(result.getId());
		} catch (RequestException ex) {
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			Throwable cause = ex.getCause();
			Throwable rootCause = cause.getCause();
			if (cause instanceof ConstraintViolationException ||
				rootCause instanceof DataIntegrityViolationException) throw new RequestException(
				ApiError.CONFLICT);
			else throw new RequestException(ApiError.ASSOCIATED_RESOURCES);
		} catch (DataAccessException ex) {
			throw new RequestException(ApiError.DATABASE_ERROR);
		} catch (Exception ex) {
			throw new RequestException(ApiError.INTERNAL_SERVER_ERROR);
		}
	}
}
