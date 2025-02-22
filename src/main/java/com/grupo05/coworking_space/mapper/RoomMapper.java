package com.grupo05.coworking_space.mapper;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.model.Room;

@Component
public class RoomMapper {
	public RoomDTO convertToDTO(Room room) {
		return new RoomDTO(
				room.getId(),
				room.getName(),
				room.getRoomStatus(),
				room.getCapacity());
	}

	public Room convertToEntity(RoomDTO roomDTO) {
		return new Room(
				roomDTO.getName(),
				roomDTO.getRoomStatus(),
				roomDTO.getCapacity());
	}
}
