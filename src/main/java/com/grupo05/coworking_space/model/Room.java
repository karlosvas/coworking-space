package com.grupo05.coworking_space.model;

import com.grupo05.coworking_space.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "room")
@Table(name = "rooms")
@Data
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", unique = true)
	private Integer id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "room_status", nullable = false, length = 20)
	private RoomStatus roomStatus;

	@Column(name = "capacity", nullable = false)
	private Integer capacity;
}
