package com.grupo05.coworking_space.service;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.mapper.RoomMapper;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.RoomRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;

	public RoomService(RoomRepository repository, RoomMapper mapper) {
		this.roomRepository = repository;
		this.roomMapper = mapper;
	}

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

	public RoomDTO createRoom(RoomDTO room) {
		try {
			Room roomToSave = roomMapper.convertToEntity(room);
			return roomMapper.convertToDTO(roomRepository.save(roomToSave));
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

	public RoomDTO updateRoom(int id, RoomDTO room) {
		try {
			Room roomFound = roomMapper.convertToEntity(this.findRoomById(id));

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
