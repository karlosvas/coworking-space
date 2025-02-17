package com.grupo05.coworking_space.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo05.coworking_space.model.Room;
import com.grupo05.coworking_space.repository.RoomRepository;

@Service
public class RoomService {
	private final RoomRepository roomRepository;

	public RoomService(RoomRepository repository) {
		this.roomRepository = repository;
	}

	public List<Room> findAllRooms() {
		return null;
	}

	public Room findRoomById(Integer id) {
		return roomRepository.findById(id).orElseThrow();
	}

	public Room createRoom(Room room) {
		return roomRepository.save(room);
	}

	public Room updateRoom(Integer id, Room room) {
		Room roomFound = this.findRoomById(id);

		if (roomFound == null) {
			System.out.println("Not found");
		}

		return roomRepository.save(room);
	}

	public void deleteRoom(Integer id) {
		roomRepository.deleteById(id);
	}
}
