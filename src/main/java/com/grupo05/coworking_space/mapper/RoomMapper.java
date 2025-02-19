package com.grupo05.coworking_space.mapper;

import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.model.Room;

@Component
public class RoomMapper {
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
}
