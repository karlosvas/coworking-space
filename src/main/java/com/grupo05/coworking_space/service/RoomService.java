package com.grupo05.coworking_space.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.mapper.RoomMapper;
import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.RoomRepository;

@Service
public class RoomService {
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;

	public RoomService(RoomRepository repository, RoomMapper mapper) {
		this.roomRepository = repository;
		this.roomMapper = mapper;
	}

	public List<RoomDTO> findAllRooms() {
		List<Room> rooms = roomRepository.findAll();
		return rooms.stream().map((room) -> roomMapper.convertToDTO(room)).collect(Collectors.toList());
	}

	public RoomDTO findRoomById(int id) {
		Room room = roomRepository.findById(id).get();
		return roomMapper.convertToDTO(room);
	}

	public RoomDTO createRoom(RoomDTO room) {
		Room roomToSave = roomMapper.convertToEntity(room);
		return roomMapper.convertToDTO(roomRepository.save(roomToSave));
	}

	public RoomDTO updateRoom(int id, RoomDTO room) {
		Room roomFound = roomMapper.convertToEntity(this.findRoomById(id));

		roomFound.setName(room.getName());
		roomFound.setRoomStatus(room.getRoomStatus());
		roomFound.setCapacity(room.getCapacity());

		return roomMapper.convertToDTO(roomRepository.save(roomFound));
	}

	public void deleteRoom(int id) {
		roomRepository.deleteById(id);
	}

//	public RoomDTO convertToDTO(Room room) {
//		return new RoomDTO(room.getId(), room.getName(), room.getRoomStatus(), room.getCapacity());
//	}

//	public Room convertToEntity(RoomDTO roomDTO) {
//		Room room = new Room();
//		room.setId(roomDTO.getId());
//		room.setName(roomDTO.getName());
//		room.setRoomStatus(roomDTO.getRoomStatus());
//		room.setCapacity(roomDTO.getCapacity());
//		return room;
//	}
}
