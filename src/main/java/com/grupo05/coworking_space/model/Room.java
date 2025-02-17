package com.grupo05.coworking_space.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "room")
@Table(name = "rooms")
@Data
public class Room {
	@Id
	@Column(name = "id", unique = true)
	private Integer id;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "state", nullable = false, length = 20)
	private String state;

	@Column(name = "capacity", nullable = false)
	private Integer capacity;
}
