package com.grupo05.coworking_space.mapper;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
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
		room.setId(roomDTO.getId());
		room.setName(roomDTO.getName());
		room.setRoomStatus(roomDTO.getRoomStatus());
		room.setCapacity(roomDTO.getCapacity());
		return room;
	}

	public List<Room> getForeignKeys(List<Integer> listRooms) {
		List<Room> rooms = new ArrayList<>();
		for (Integer i : listRooms) {
			Optional<Room> room = roomRepository.findById(i);
			if (room.isPresent())
				rooms.add(room.get());
			else
				throw new RequestException(ApiError.BAD_REQUEST);

		}
		return rooms;
	}
}