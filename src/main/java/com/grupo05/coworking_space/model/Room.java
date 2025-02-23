package com.grupo05.coworking_space.model;

import com.grupo05.coworking_space.enums.RoomStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "ROOM")
@Table(name = "ROOM")
@Data
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "room_id", unique = true)
	private int id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "room_status", nullable = false, length = 20)
	private RoomStatus roomStatus;

	@Column(name = "capacity", nullable = false)
	private int capacity;

	public Room(String name, RoomStatus roomStatus, int capacity) {
		this.name = name;
		this.roomStatus = roomStatus;
		this.capacity = capacity;
	}
}
