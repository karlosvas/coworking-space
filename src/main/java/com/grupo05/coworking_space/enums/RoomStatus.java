package com.grupo05.coworking_space.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum RoomStatus {
	AVAILABLE("Available"), BUSY("Busy"), MAINTENANCE("Maintenance"), NOT_AVAILABLE("Not Available");

	private String state;

	RoomStatus(String state) {
		this.state = state;
	}

	@JsonCreator
	public static RoomStatus fromString(String value) {
		for (RoomStatus status : RoomStatus.values()) {
			if (status.getState().equalsIgnoreCase(value))
				return status;
		}
		throw new IllegalArgumentException("Invalid status: " + value);
	}

	@JsonValue
	public String getState() {
		return state;
	}
}
