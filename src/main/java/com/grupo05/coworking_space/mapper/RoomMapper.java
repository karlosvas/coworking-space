package com.grupo05.coworking_space.mapper;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.model.Reservation;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.RoomRepository;

@Component
public class RoomMapper {

	private RoomRepository roomRepository;

	public RoomMapper(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	public RoomDTO convertToDTO(Room room) {
		return new RoomDTO(room.getId(), room.getName(), room.getRoomStatus(), room.getCapacity());
	}

	public Room convertToEntity(RoomDTO roomDTO) {
		Room room = new Room();
		room.setName(roomDTO.getName());
		room.setRoomStatus(roomDTO.getRoomStatus());
		room.setCapacity(roomDTO.getCapacity());
		return room;
	}

	public List<Room> getForeignKeys(List<Integer> listRooms, Reservation reservation) {
		List<Room> rooms = new ArrayList<>();
		for (Integer i : listRooms) {
			Optional<Room> roomOptional = roomRepository.findById(i);
			if (roomOptional.isPresent()) {
				Room room = roomOptional.get();
				List<Reservation> reservations = room.getReservations();
				reservations.add(reservation);
				room.setReservations(reservations);
				rooms.add(room);
			} else {
				throw new RequestException(ApiError.BAD_REQUEST);
			}
		}
		return rooms;
	}
}