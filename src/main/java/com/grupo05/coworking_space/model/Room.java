package com.grupo05.coworking_space.model;

import java.util.List;

import com.grupo05.coworking_space.enums.RoomStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity(name = "ROOM")
@Table(name = "ROOM", schema = "coworking_space")
@Data
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id", unique = true)
	private int id;

	@NotNull(message = "{field.null}")
	@NotEmpty(message = "{field.empty}")
	@Size(min = 2, max = 20, message = "{room.name.size}")
	@Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]*$", message="{room.name.pattern}")
	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@NotNull(message = "{field.null}")
	@Enumerated(EnumType.STRING)
	@Column(name = "room_status", nullable = false, length = 20)
	private RoomStatus roomStatus;

	@NotNull(message = "{field.null}")
	@Min(value = 1, message = "{room.min.capacity}")
	@Column(name = "capacity", nullable = false)
	private int capacity;

	@Valid
	@ManyToMany(mappedBy = "rooms")
	private List<Reservation> reservations;

	public List<Reservation> getReservation() {
		return reservations;
	}

	public void setReservation(List<Reservation> reservations) {
		this.reservations = reservations;
	}
}
